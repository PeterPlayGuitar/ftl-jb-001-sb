package com.apeter.blog.article.controller;

import com.apeter.blog.auth.exceptions.AuthException;
import com.apeter.blog.auth.exceptions.NoAccessException;
import com.apeter.blog.base.api.request.SearchRequest;
import com.apeter.blog.base.api.response.OkResponse;
import com.apeter.blog.base.api.response.SearchResponse;
import com.apeter.blog.article.api.request.ArticleRequest;
import com.apeter.blog.article.exception.ArticleNoExistException;
import com.apeter.blog.article.routes.ArticleApiRoutes;
import com.apeter.blog.article.api.response.ArticleResponse;
import com.apeter.blog.article.exception.ArticleExistException;
import com.apeter.blog.article.mapping.ArticleMapping;
import com.apeter.blog.article.service.ArticleApiService;
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
@Api(value = "Article API")
public class ArticleApiController {
    private final ArticleApiService articleApiService;

    @GetMapping(ArticleApiRoutes.BY_ID)
    @ApiOperation(value = "Find article by if", notes = "Use this when you need to find article by id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Success"),
                    @ApiResponse(code = 404, message = "Article not found")
            }
    )
    public OkResponse<ArticleResponse> byId(
            @ApiParam(value = "Article id") @PathVariable ObjectId id
    ) throws ChangeSetPersister.NotFoundException {
        return OkResponse.of(ArticleMapping.getInstance().getResponseMapping().convert(
                articleApiService.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new)
        ));
    }

    @PostMapping(ArticleApiRoutes.ROOT)
    @ApiOperation(value = "Create", notes = "Use this when you need to create new article")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Article already exists")
    })
    public OkResponse<ArticleResponse> create(@RequestBody ArticleRequest request) throws AuthException {
        return OkResponse.of(ArticleMapping.getInstance().getResponseMapping().convert(articleApiService.create(request)));
    }

    @GetMapping(ArticleApiRoutes.ROOT)
    @ApiOperation(value = "Search articles", notes = "Use this when you need to search articles by last name first name or email with skip and size of the response")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Success")
            }
    )
    public OkResponse<SearchResponse<ArticleResponse>> search(
            @ModelAttribute SearchRequest request
    ) {
        return OkResponse.of(ArticleMapping.getInstance().getSearchMapping().convert(
                articleApiService.search(request)
        ));
    }

    @PutMapping(ArticleApiRoutes.BY_ID)
    @ApiOperation(value = "update article", notes = "Use this when you need to update article")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Success")
            }
    )
    public OkResponse<ArticleResponse> update(
            @ApiParam(value = "Article id") @PathVariable String id,
            @RequestBody ArticleRequest request
    ) throws ArticleNoExistException, AuthException, NoAccessException {
        return OkResponse.of(ArticleMapping.getInstance().getResponseMapping().convert(
                articleApiService.update(request)
        ));
    }

    @DeleteMapping(ArticleApiRoutes.BY_ID)
    @ApiOperation(value = "delete article by id", notes = "Use this when you need to delete article by id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Success")
            }
    )
    public OkResponse<String> deleteById(
            @ApiParam(value = "Article id")
            @PathVariable ObjectId id
    ) throws NoAccessException, AuthException, ChangeSetPersister.NotFoundException {
        articleApiService.deleteById(id);
        return OkResponse.of(HttpStatus.OK.toString());
    }
}
