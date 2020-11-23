package com.apeter.blog.file.mapping;

import com.apeter.blog.base.api.response.SearchResponse;
import com.apeter.blog.base.mapping.BaseMapping;
import com.apeter.blog.file.api.response.FileResponse;
import com.apeter.blog.file.model.FileDoc;
import lombok.Getter;

import java.util.stream.Collectors;

@Getter
public class FileMapping {
    public static class ResponseMapping extends BaseMapping<FileDoc, FileResponse> {

        @Override
        public FileResponse convert(FileDoc fileDoc) {
            return FileResponse.builder()
                    .id(fileDoc.getId().toString())
                    .title(fileDoc.getTitle())
                    .ownerId(fileDoc.getOwnerId().toString())
                    .contentType(fileDoc.getContentType())
                    .build();
        }

        @Override
        public FileDoc revert(FileResponse fileResponse) {
            throw new RuntimeException("don't use this");
        }
    }

    public static class SearchMapping extends BaseMapping<SearchResponse<FileDoc>, SearchResponse<FileResponse>> {

        private ResponseMapping responseMapping = new ResponseMapping();

        @Override
        public SearchResponse<FileResponse> convert(SearchResponse<FileDoc> searchResponse) {
            return SearchResponse.of(
                    searchResponse.getList().stream().map(responseMapping::convert).collect(Collectors.toList()),
                    searchResponse.getCount()
            );
        }

        @Override
        public SearchResponse<FileDoc> revert(SearchResponse<FileResponse> fileResponses) {
            throw new RuntimeException("don't use this");
        }
    }

    private final ResponseMapping responseMapping = new ResponseMapping();
    private final SearchMapping searchMapping = new SearchMapping();

    public static FileMapping getInstance() {
        return new FileMapping();
    }
}
