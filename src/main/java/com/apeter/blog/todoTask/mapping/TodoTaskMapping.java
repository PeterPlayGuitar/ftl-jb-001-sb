package com.apeter.blog.todoTask.mapping;

import com.apeter.blog.base.api.response.SearchResponse;
import com.apeter.blog.base.mapping.BaseMapping;
import com.apeter.blog.todoTask.api.request.TodoTaskRequest;
import com.apeter.blog.todoTask.api.response.TodoTaskResponse;
import com.apeter.blog.todoTask.model.TodoTaskDoc;
import lombok.Getter;
import org.bson.types.ObjectId;

import java.util.stream.Collectors;

@Getter
public class TodoTaskMapping {
    public static class RequestMapping extends BaseMapping<TodoTaskRequest, TodoTaskDoc> {

        @Override
        public TodoTaskDoc convert(TodoTaskRequest todoTaskRequest) {
            return TodoTaskDoc.builder()
                    .id(todoTaskRequest.getId())
                    .title(todoTaskRequest.getTitle())
                    .ownerId(todoTaskRequest.getOwnerId())
                    .completed(todoTaskRequest.getCompleted())
                    .files(todoTaskRequest.getFiles())
                    .build();
        }

        @Override
        public TodoTaskRequest revert(TodoTaskDoc todoTaskDoc) {
            throw new RuntimeException("don't use this");
        }
    }



    public static class ResponseMapping extends BaseMapping<TodoTaskDoc, TodoTaskResponse> {

        @Override
        public TodoTaskResponse convert(TodoTaskDoc todoTaskDoc) {
            return TodoTaskResponse.builder()
                    .id(todoTaskDoc.getId().toString())
                    .title(todoTaskDoc.getTitle())
                    .ownerId(todoTaskDoc.getOwnerId().toString())
                    .completed(todoTaskDoc.getCompleted())
                    .files(todoTaskDoc.getFiles().stream().map(ObjectId::toString).collect(Collectors.toList()))
                    .build();
        }

        @Override
        public TodoTaskDoc revert(TodoTaskResponse todoTaskResponse) {
            throw new RuntimeException("don't use this");
        }
    }

    public static class SearchMapping extends BaseMapping<SearchResponse<TodoTaskDoc>, SearchResponse<TodoTaskResponse>> {

        private ResponseMapping responseMapping = new ResponseMapping();

        @Override
        public SearchResponse<TodoTaskResponse> convert(SearchResponse<TodoTaskDoc> searchResponse) {
            return SearchResponse.of(
                    searchResponse.getList().stream().map(responseMapping::convert).collect(Collectors.toList()),
                    searchResponse.getCount()
            );
        }

        @Override
        public SearchResponse<TodoTaskDoc> revert(SearchResponse<TodoTaskResponse> todoTaskResponses) {
            throw new RuntimeException("don't use this");
        }
    }

    private final RequestMapping requestMapping = new RequestMapping();
    private final ResponseMapping responseMapping = new ResponseMapping();
    private final SearchMapping searchMapping = new SearchMapping();

    public static TodoTaskMapping getInstance() {
        return new TodoTaskMapping();
    }
}
