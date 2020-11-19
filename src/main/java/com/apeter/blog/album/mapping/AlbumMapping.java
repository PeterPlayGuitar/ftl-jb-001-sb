package com.apeter.blog.album.mapping;

import com.apeter.blog.base.api.response.SearchResponse;
import com.apeter.blog.base.mapping.BaseMapping;
import com.apeter.blog.album.api.request.AlbumRequest;
import com.apeter.blog.album.api.response.AlbumResponse;
import com.apeter.blog.album.model.AlbumDoc;
import lombok.Getter;

import java.util.stream.Collectors;

@Getter
public class AlbumMapping {
    public static class RequestMapping extends BaseMapping<AlbumRequest, AlbumDoc> {

        @Override
        public AlbumDoc convert(AlbumRequest albumRequest) {
            return AlbumDoc.builder()
                    .id(albumRequest.getId())
                    .title(albumRequest.getTitle())
                    .ownerId(albumRequest.getOwnerId())
                    .build();
        }

        @Override
        public AlbumRequest revert(AlbumDoc albumDoc) {
            throw new RuntimeException("don't use this");
        }
    }


    public static class ResponseMapping extends BaseMapping<AlbumDoc, AlbumResponse> {

        @Override
        public AlbumResponse convert(AlbumDoc albumDoc) {
            return AlbumResponse.builder()
                    .id(albumDoc.getId().toString())
                    .title(albumDoc.getTitle())
                    .ownerId(albumDoc.getOwnerId().toString())
                    .build();
        }

        @Override
        public AlbumDoc revert(AlbumResponse albumResponse) {
            throw new RuntimeException("don't use this");
        }
    }

    public static class SearchMapping extends BaseMapping<SearchResponse<AlbumDoc>, SearchResponse<AlbumResponse>> {

        private ResponseMapping responseMapping = new ResponseMapping();

        @Override
        public SearchResponse<AlbumResponse> convert(SearchResponse<AlbumDoc> searchResponse) {
            return SearchResponse.of(
                    searchResponse.getList().stream().map(responseMapping::convert).collect(Collectors.toList()),
                    searchResponse.getCount()
            );
        }

        @Override
        public SearchResponse<AlbumDoc> revert(SearchResponse<AlbumResponse> albumResponses) {
            throw new RuntimeException("don't use this");
        }
    }

    private final RequestMapping requestMapping = new RequestMapping();
    private final ResponseMapping responseMapping = new ResponseMapping();
    private final SearchMapping searchMapping = new SearchMapping();

    public static AlbumMapping getInstance() {
        return new AlbumMapping();
    }
}
