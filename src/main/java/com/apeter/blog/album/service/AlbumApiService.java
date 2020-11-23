package com.apeter.blog.album.service;

import com.apeter.blog.album.mapping.AlbumMapping;
import com.apeter.blog.base.api.request.SearchRequest;
import com.apeter.blog.base.api.response.SearchResponse;
import com.apeter.blog.album.api.request.AlbumRequest;
import com.apeter.blog.album.exception.AlbumExistException;
import com.apeter.blog.album.exception.AlbumNoExistException;
import com.apeter.blog.album.model.AlbumDoc;
import com.apeter.blog.album.repository.AlbumRepository;
import com.apeter.blog.photo.api.request.PhotoSearchRequest;
import com.apeter.blog.photo.model.PhotoDoc;
import com.apeter.blog.photo.service.PhotoApiService;
import com.apeter.blog.user.exception.UserNoExistException;
import com.apeter.blog.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AlbumApiService {
    private final AlbumRepository albumRepository;
    private final MongoTemplate mongoTemplate;
    private final UserRepository userRepository;
    private final PhotoApiService photoApiService;

    public AlbumDoc create(AlbumRequest request) throws AlbumExistException, UserNoExistException {

        if (userRepository.findById(request.getOwnerId()).isEmpty())
            throw new UserNoExistException();

        AlbumDoc albumDoc = AlbumMapping.getInstance().getRequestMapping().convert(request);
        albumRepository.save(albumDoc);
        return albumDoc;
    }

    public Optional<AlbumDoc> findById(ObjectId id) {
        return albumRepository.findById(id);
    }

    public SearchResponse<AlbumDoc> search(
            SearchRequest request
    ) {
        Criteria criteria = new Criteria();
        if (request.getQuery() != null && !request.getQuery().equals("")) {
            criteria = criteria.orOperator(
                    Criteria.where("title").regex(request.getQuery(), "i")
            );
        }

        Query query = new Query(criteria);
        Long count = mongoTemplate.count(query, AlbumDoc.class);


        query.limit(request.getSize());
        query.skip(request.getSkip());

        List<AlbumDoc> albumDocs = mongoTemplate.find(query, AlbumDoc.class);
        return SearchResponse.of(albumDocs, count);
    }

    public AlbumDoc update(AlbumRequest request) throws AlbumNoExistException {
        Optional<AlbumDoc> albumDocOptional = albumRepository.findById(request.getId());
        if (!albumDocOptional.isPresent()) {
            throw new AlbumNoExistException();
        }

        AlbumDoc oldAlbum = albumDocOptional.get();

        AlbumDoc albumDoc = AlbumMapping.getInstance().getRequestMapping().convert(request);

        albumDoc.setOwnerId(oldAlbum.getOwnerId());
        albumDoc.setId(request.getId());
        albumRepository.save(albumDoc);

        return albumDoc;
    }

    public void deleteById(ObjectId id) {
        val photoDocs = photoApiService.search(
                PhotoSearchRequest.builder()
                        .albumId(id)
                        .size(10000)
                        .build()
        ).getList();
        for (PhotoDoc photoDoc : photoDocs)
            photoApiService.deleteById(photoDoc.getId());
        albumRepository.deleteById(id);
    }
}
