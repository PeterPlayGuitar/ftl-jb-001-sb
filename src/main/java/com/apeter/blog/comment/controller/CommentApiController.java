package com.apeter.blog.comment.controller;

import com.apeter.blog.article.exception.ArticleNoExistException;
import com.apeter.blog.auth.exceptions.AuthException;
import com.apeter.blog.auth.exceptions.NoAccessException;
import com.apeter.blog.base.api.request.SearchRequest;
import com.apeter.blog.base.api.response.OkResponse;
import com.apeter.blog.base.api.response.SearchResponse;
import com.apeter.blog.comment.api.request.CommentRequest;
import com.apeter.blog.comment.api.request.CommentSearchRequest;
import com.apeter.blog.comment.exception.CommentNoExistException;
import com.apeter.blog.comment.routes.CommentApiRoutes;
import com.apeter.blog.comment.api.response.CommentResponse;
import com.apeter.blog.comment.exception.CommentExistException;
import com.apeter.blog.comment.mapping.CommentMapping;
import com.apeter.blog.comment.service.CommentApiService;
import com.apeter.blog.user.exception.UserNoExistException;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.jni.Local;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(value = "Comment API")
public class CommentApiController {
    private final CommentApiService commentApiService;

    @PostMapping(CommentApiRoutes.ROOT)
    @ApiOperation(value = "Create", notes = "Use this when you need to create new comment")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Comment already exists")
    })
    public OkResponse<CommentResponse> create(@RequestBody CommentRequest request) throws ArticleNoExistException, AuthException {
//        Integer i = 3/0;

        return OkResponse.of(CommentMapping.getInstance().getResponseMapping().convert(commentApiService.create(request)));
    }

    @GetMapping(CommentApiRoutes.BY_ID)
    @ApiOperation(value = "Find comment by if", notes = "Use this when you need to find comment by id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Success"),
                    @ApiResponse(code = 404, message = "Comment not found")
            }
    )
    public OkResponse<CommentResponse> byId(
            @ApiParam(value = "Comment id") @PathVariable ObjectId id
    ) throws ChangeSetPersister.NotFoundException {
        return OkResponse.of(CommentMapping.getInstance().getResponseMapping().convert(
                commentApiService.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new)
        ));
    }

    @GetMapping(CommentApiRoutes.ROOT)
    @ApiOperation(value = "Search comments", notes = "Use this when you need to search comments by last name first name or email with skip and size of the response")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Success")
            }
    )
    public OkResponse<SearchResponse<CommentResponse>> search(
            @ModelAttribute CommentSearchRequest request
    ) {
        return OkResponse.of(CommentMapping.getInstance().getSearchMapping().convert(
                commentApiService.search(request)
        ));
    }

    @PutMapping(CommentApiRoutes.BY_ID)
    @ApiOperation(value = "update comment", notes = "Use this when you need to update comment")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Success")
            }
    )
    public OkResponse<CommentResponse> update(
            @ApiParam(value = "Comment id") @PathVariable String id,
            @RequestBody CommentRequest request
    ) throws CommentNoExistException, NoAccessException, AuthException {
        return OkResponse.of(CommentMapping.getInstance().getResponseMapping().convert(
                commentApiService.update(request)
        ));
    }

    @DeleteMapping(CommentApiRoutes.BY_ID)
    @ApiOperation(value = "delete comment by id", notes = "Use this when you need to delete comment by id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Success")
            }
    )
    public OkResponse<String> deleteById(
            @ApiParam(value = "Comment id")
            @PathVariable ObjectId id
    ) throws NoAccessException, AuthException, ChangeSetPersister.NotFoundException {
        commentApiService.deleteById(id);
        return OkResponse.of(HttpStatus.OK.toString());
    }
}
