package com.apeter.blog.album.controller;

import com.apeter.blog.auth.exceptions.AuthException;
import com.apeter.blog.auth.exceptions.NoAccessException;
import com.apeter.blog.base.api.request.SearchRequest;
import com.apeter.blog.base.api.response.OkResponse;
import com.apeter.blog.base.api.response.SearchResponse;
import com.apeter.blog.album.api.request.AlbumRequest;
import com.apeter.blog.album.exception.AlbumNoExistException;
import com.apeter.blog.album.routes.AlbumApiRoutes;
import com.apeter.blog.album.api.response.AlbumResponse;
import com.apeter.blog.album.exception.AlbumExistException;
import com.apeter.blog.album.mapping.AlbumMapping;
import com.apeter.blog.album.service.AlbumApiService;
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
@Api(value = "Album API")
public class AlbumApiController {
    private final AlbumApiService albumApiService;

    @PostMapping(AlbumApiRoutes.ROOT)
    @ApiOperation(value = "Create", notes = "Use this when you need to create new album")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Album already exists")
    })
    public OkResponse<AlbumResponse> create(@RequestBody AlbumRequest request) throws AuthException {
//        Integer i = 3/0;

        return OkResponse.of(AlbumMapping.getInstance().getResponseMapping().convert(albumApiService.create(request)));
    }

    @GetMapping(AlbumApiRoutes.BY_ID)
    @ApiOperation(value = "Find album by if", notes = "Use this when you need to find album by id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Success"),
                    @ApiResponse(code = 404, message = "Album not found")
            }
    )
    public OkResponse<AlbumResponse> byId(
            @ApiParam(value = "Album id") @PathVariable ObjectId id
    ) throws ChangeSetPersister.NotFoundException {
        return OkResponse.of(AlbumMapping.getInstance().getResponseMapping().convert(
                albumApiService.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new)
        ));
    }

    @GetMapping(AlbumApiRoutes.ROOT)
    @ApiOperation(value = "Search albums", notes = "Use this when you need to search albums by last name first name or email with skip and size of the response")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Success")
            }
    )
    public OkResponse<SearchResponse<AlbumResponse>> search(
            @ModelAttribute SearchRequest request
    ) {
        return OkResponse.of(AlbumMapping.getInstance().getSearchMapping().convert(
                albumApiService.search(request)
        ));
    }

    @PutMapping(AlbumApiRoutes.BY_ID)
    @ApiOperation(value = "update album", notes = "Use this when you need to update album")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Success"),
                    @ApiResponse(code = 400, message = "Album id is invalid"),
                    @ApiResponse(code = 401, message = "Need Auth"),
                    @ApiResponse(code = 403, message = "Not Access")
            }
    )
    public OkResponse<AlbumResponse> update(
            @ApiParam(value = "Album id") @PathVariable String id,
            @RequestBody AlbumRequest request
    ) throws AlbumNoExistException, NoAccessException, AuthException {
        return OkResponse.of(AlbumMapping.getInstance().getResponseMapping().convert(
                albumApiService.update(request)
        ));
    }

    @DeleteMapping(AlbumApiRoutes.BY_ID)
    @ApiOperation(value = "delete album by id", notes = "Use this when you need to delete album by id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Success")
            }
    )
    public OkResponse<String> deleteById(
            @ApiParam(value = "Album id")
            @PathVariable ObjectId id
    ) throws AuthException, NoAccessException, ChangeSetPersister.NotFoundException {
        albumApiService.deleteById(id);
        return OkResponse.of(HttpStatus.OK.toString());
    }
}
