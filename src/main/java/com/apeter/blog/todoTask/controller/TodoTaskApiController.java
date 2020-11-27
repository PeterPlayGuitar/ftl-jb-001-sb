package com.apeter.blog.todoTask.controller;

import com.apeter.blog.auth.exceptions.AuthException;
import com.apeter.blog.auth.exceptions.NoAccessException;
import com.apeter.blog.base.api.request.SearchRequest;
import com.apeter.blog.base.api.response.OkResponse;
import com.apeter.blog.base.api.response.SearchResponse;
import com.apeter.blog.todoTask.api.request.TodoTaskRequest;
import com.apeter.blog.todoTask.api.request.TodoTaskSearchRequest;
import com.apeter.blog.todoTask.exception.TodoTaskNoExistException;
import com.apeter.blog.todoTask.routes.TodoTaskApiRoutes;
import com.apeter.blog.todoTask.api.response.TodoTaskResponse;
import com.apeter.blog.todoTask.exception.TodoTaskExistException;
import com.apeter.blog.todoTask.mapping.TodoTaskMapping;
import com.apeter.blog.todoTask.service.TodoTaskApiService;
import com.apeter.blog.user.exception.UserNoExistException;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@Api(value = "Todo Task API")
public class TodoTaskApiController {
    private final TodoTaskApiService todoTaskApiService;

    @PostMapping(TodoTaskApiRoutes.ROOT)
    @ApiOperation(value = "Create", notes = "Use this when you need to create new todoTask")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "TodoTask already exists")
    })
    public OkResponse<TodoTaskResponse> create(@RequestBody TodoTaskRequest request) throws AuthException {
        return OkResponse.of(TodoTaskMapping.getInstance().getResponseMapping().convert(todoTaskApiService.create(request)));
    }

    @GetMapping(TodoTaskApiRoutes.BY_ID)
    @ApiOperation(value = "Find todoTask by if", notes = "Use this when you need to find todoTask by id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Success"),
                    @ApiResponse(code = 404, message = "TodoTask not found")
            }
    )
    public OkResponse<TodoTaskResponse> byId(
            @ApiParam(value = "TodoTask id") @PathVariable ObjectId id
    ) throws ChangeSetPersister.NotFoundException {
        return OkResponse.of(TodoTaskMapping.getInstance().getResponseMapping().convert(
                todoTaskApiService.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new)
        ));
    }

    @GetMapping(TodoTaskApiRoutes.ROOT)
    @ApiOperation(value = "Search todoTasks", notes = "Use this when you need to search todoTasks by last name first name or email with skip and size of the response")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Success")
            }
    )
    public OkResponse<SearchResponse<TodoTaskResponse>> search(
            @ModelAttribute TodoTaskSearchRequest request
    ) throws ResponseStatusException, AuthException {
        return OkResponse.of(TodoTaskMapping.getInstance().getSearchMapping().convert(
                todoTaskApiService.search(request)
        ));
    }

    @PutMapping(TodoTaskApiRoutes.BY_ID)
    @ApiOperation(value = "update todoTask", notes = "Use this when you need to update todoTask")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Success")
            }
    )
    public OkResponse<TodoTaskResponse> update(
            @ApiParam(value = "TodoTask id") @PathVariable String id,
            @RequestBody TodoTaskRequest request
    ) throws TodoTaskNoExistException, NoAccessException, AuthException {
        return OkResponse.of(TodoTaskMapping.getInstance().getResponseMapping().convert(
                todoTaskApiService.update(request)
        ));
    }

    @DeleteMapping(TodoTaskApiRoutes.BY_ID)
    @ApiOperation(value = "delete todoTask by id", notes = "Use this when you need to delete todoTask by id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Success")
            }
    )
    public OkResponse<String> deleteById(
            @ApiParam(value = "TodoTask id")
            @PathVariable ObjectId id
    ) throws NoAccessException, AuthException, ChangeSetPersister.NotFoundException {
        todoTaskApiService.deleteById(id);
        return OkResponse.of(HttpStatus.OK.toString());
    }
}
