package com.apeter.blog.photo.mapping;

import com.apeter.blog.base.api.response.SearchResponse;
import com.apeter.blog.base.mapping.BaseMapping;
import com.apeter.blog.photo.api.request.PhotoRequest;
import com.apeter.blog.photo.api.response.PhotoResponse;
import com.apeter.blog.photo.model.PhotoDoc;
import lombok.Getter;
import org.bson.types.ObjectId;

import java.util.stream.Collectors;

@Getter
public class PhotoMapping {
    public static class RequestMapping{

        public PhotoDoc convert(PhotoRequest photoRequest, ObjectId owenerId) {
            return PhotoDoc.builder()
                    .id(photoRequest.getId())
                    .title(photoRequest.getTitle())
                    .ownerId(owenerId)
                    .albumId(photoRequest.getAlbumId())
                    .contentType(photoRequest.getContentType())
                    .build();
        }
    }


    public static class ResponseMapping extends BaseMapping<PhotoDoc, PhotoResponse> {

        @Override
        public PhotoResponse convert(PhotoDoc photoDoc) {
            return PhotoResponse.builder()
                    .id(photoDoc.getId().toString())
                    .title(photoDoc.getTitle())
                    .ownerId(photoDoc.getOwnerId().toString())
                    .albumId(photoDoc.getAlbumId().toString())
                    .contentType(photoDoc.getContentType())
                    .build();
        }

        @Override
        public PhotoDoc revert(PhotoResponse photoResponse) {
            throw new RuntimeException("don't use this");
        }
    }

    public static class SearchMapping extends BaseMapping<SearchResponse<PhotoDoc>, SearchResponse<PhotoResponse>> {

        private ResponseMapping responseMapping = new ResponseMapping();

        @Override
        public SearchResponse<PhotoResponse> convert(SearchResponse<PhotoDoc> searchResponse) {
            return SearchResponse.of(
                    searchResponse.getList().stream().map(responseMapping::convert).collect(Collectors.toList()),
                    searchResponse.getCount()
            );
        }

        @Override
        public SearchResponse<PhotoDoc> revert(SearchResponse<PhotoResponse> photoResponses) {
            throw new RuntimeException("don't use this");
        }
    }

    private final RequestMapping requestMapping = new RequestMapping();
    private final ResponseMapping responseMapping = new ResponseMapping();
    private final SearchMapping searchMapping = new SearchMapping();

    public static PhotoMapping getInstance() {
        return new PhotoMapping();
    }
}
