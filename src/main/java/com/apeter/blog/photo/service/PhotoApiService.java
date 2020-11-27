package com.apeter.blog.photo.service;

import com.apeter.blog.album.api.response.AlbumResponse;
import com.apeter.blog.album.exception.AlbumNoExistException;
import com.apeter.blog.album.model.AlbumDoc;
import com.apeter.blog.album.repository.AlbumRepository;
import com.apeter.blog.auth.exceptions.AuthException;
import com.apeter.blog.auth.exceptions.NoAccessException;
import com.apeter.blog.auth.service.AuthService;
import com.apeter.blog.base.api.request.SearchRequest;
import com.apeter.blog.base.api.response.SearchResponse;
import com.apeter.blog.base.service.CheckAccess;
import com.apeter.blog.photo.api.request.PhotoRequest;
import com.apeter.blog.photo.api.request.PhotoSearchRequest;
import com.apeter.blog.photo.exception.PhotoExistException;
import com.apeter.blog.photo.exception.PhotoNoExistException;
import com.apeter.blog.photo.mapping.PhotoMapping;
import com.apeter.blog.photo.model.PhotoDoc;
import com.apeter.blog.photo.repository.PhotoRepository;
import com.apeter.blog.user.api.response.UserResponse;
import com.apeter.blog.user.exception.UserNoExistException;
import com.apeter.blog.user.model.UserDoc;
import com.apeter.blog.user.repository.UserRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PhotoApiService extends CheckAccess<PhotoDoc> {
    private final PhotoRepository photoRepository;
    private final MongoTemplate mongoTemplate;
    private final AlbumRepository albumRepository;
    private final UserRepository userRepository;
    private final AuthService authService;

    private final GridFsTemplate gridFsTemplate;
    private final GridFsOperations gridFsOperations;

    public PhotoDoc create(MultipartFile file, ObjectId albumId) throws  AlbumNoExistException, IOException, AuthException, NoAccessException {

        UserDoc userDoc = authService.currentUser();
        AlbumDoc albumDoc = albumRepository.findById(albumId).orElseThrow(AlbumNoExistException::new);

        if(!albumDoc.getOwnerId().equals(userDoc.getId()))
            throw new NoAccessException();

        DBObject metaData = new BasicDBObject();
        metaData.put("type", file.getContentType());
        metaData.put("title", file.getOriginalFilename());

        ObjectId id = gridFsTemplate.store(
                file.getInputStream(), file.getOriginalFilename(), file.getContentType(), metaData
        );

        PhotoDoc photoDoc = PhotoDoc.builder()
                .id(id)
                .albumId(albumId)
                .title(file.getOriginalFilename())
                .ownerId(userDoc.getId())
                .contentType(file.getContentType())
                .build();

        photoRepository.save(photoDoc);
        return photoDoc;
    }

    public Optional<PhotoDoc> findById(ObjectId id) {
        return photoRepository.findById(id);
    }

    public SearchResponse<PhotoDoc> search(
            PhotoSearchRequest request
    ) {
        Criteria criteria = Criteria.where("albumId").is(request.getAlbumId());
        if (request.getQuery() != null && !request.getQuery().equals("")) {
            criteria = criteria.orOperator(
                    Criteria.where("title").regex(request.getQuery(), "i")
            );
        }

        Query query = new Query(criteria);
        Long count = mongoTemplate.count(query, PhotoDoc.class);


        query.limit(request.getSize());
        query.skip(request.getSkip());

        List<PhotoDoc> photoDocs = mongoTemplate.find(query, PhotoDoc.class);
        return SearchResponse.of(photoDocs, count);
    }

    public PhotoDoc update(PhotoRequest request) throws PhotoNoExistException, NoAccessException, AuthException {
        Optional<PhotoDoc> photoDocOptional = photoRepository.findById(request.getId());
        if (!photoDocOptional.isPresent()) {
            throw new PhotoNoExistException();
        }

        PhotoDoc oldDoc = photoDocOptional.get();
        UserDoc owner = checkAccess(oldDoc);

        PhotoDoc photoDoc = PhotoMapping.getInstance().getRequestMapping().convert(request, owner.getId());
        photoDoc.setId(request.getId());
        photoDoc.setAlbumId(oldDoc.getAlbumId());
        photoDoc.setOwnerId(oldDoc.getOwnerId());
        photoDoc.setContentType(oldDoc.getContentType());
        photoRepository.save(photoDoc);

        return photoDoc;
    }

    public InputStream downloadById(ObjectId id) throws ChangeSetPersister.NotFoundException, IOException {
        GridFSFile file = gridFsTemplate.findOne(new Query(
                Criteria.where("_id").is(id)
        ));
        if (file == null) throw new ChangeSetPersister.NotFoundException();
        return gridFsOperations.getResource(file).getInputStream();
    }

    public void deleteById(ObjectId id) throws NoAccessException, AuthException, ChangeSetPersister.NotFoundException {
        checkAccess(photoRepository.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new));

        gridFsTemplate.delete(new Query(
                Criteria.where("_id").is(id)
        ));
        photoRepository.deleteById(id);
    }

    @Override
    protected ObjectId getOwnerFromEntity(PhotoDoc entity) {
        return entity.getOwnerId();
    }

    @Override
    protected AuthService authService() {
        return authService;
    }
}
