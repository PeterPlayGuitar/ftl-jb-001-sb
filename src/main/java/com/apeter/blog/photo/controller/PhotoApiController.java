package com.apeter.blog.photo.controller;

import com.apeter.blog.album.exception.AlbumNoExistException;
import com.apeter.blog.auth.exceptions.AuthException;
import com.apeter.blog.auth.exceptions.NoAccessException;
import com.apeter.blog.base.api.request.SearchRequest;
import com.apeter.blog.base.api.response.OkResponse;
import com.apeter.blog.base.api.response.SearchResponse;
import com.apeter.blog.photo.api.request.PhotoRequest;
import com.apeter.blog.photo.api.request.PhotoSearchRequest;
import com.apeter.blog.photo.exception.PhotoNoExistException;
import com.apeter.blog.photo.routes.PhotoApiRoutes;
import com.apeter.blog.photo.api.response.PhotoResponse;
import com.apeter.blog.photo.exception.PhotoExistException;
import com.apeter.blog.photo.mapping.PhotoMapping;
import com.apeter.blog.photo.service.PhotoApiService;
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
@Api(value = "Photo API")
public class PhotoApiController {
    private final PhotoApiService photoApiService;

    @GetMapping(PhotoApiRoutes.BY_ID)
    @ApiOperation(value = "Find photo by if", notes = "Use this when you need to find photo by id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Success"),
                    @ApiResponse(code = 404, message = "Photo not found")
            }
    )
    public OkResponse<PhotoResponse> byId(
            @ApiParam(value = "Photo id") @PathVariable ObjectId id
    ) throws ChangeSetPersister.NotFoundException {
        return OkResponse.of(PhotoMapping.getInstance().getResponseMapping().convert(
                photoApiService.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new)
        ));
    }

    @GetMapping(PhotoApiRoutes.ROOT)
    @ApiOperation(value = "Search photos", notes = "Use this when you need to search photos by last name first name or email with skip and size of the response")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Success")
            }
    )
    public OkResponse<SearchResponse<PhotoResponse>> search(
            @ModelAttribute PhotoSearchRequest request
    ) {
        return OkResponse.of(PhotoMapping.getInstance().getSearchMapping().convert(
                photoApiService.search(request)
        ));
    }

    @PutMapping(PhotoApiRoutes.BY_ID)
    @ApiOperation(value = "update photo", notes = "Use this when you need to update photo")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Success")
            }
    )
    public OkResponse<PhotoResponse> update(
            @ApiParam(value = "Photo id") @PathVariable String id,
            @RequestBody PhotoRequest request
    ) throws PhotoNoExistException, NoAccessException, AuthException {
        return OkResponse.of(PhotoMapping.getInstance().getResponseMapping().convert(
                photoApiService.update(request)
        ));
    }

    @DeleteMapping(PhotoApiRoutes.BY_ID)
    @ApiOperation(value = "delete photo by id", notes = "Use this when you need to delete photo by id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Success")
            }
    )
    public OkResponse<String> deleteById(
            @ApiParam(value = "Photo id")
            @PathVariable ObjectId id
    ) throws NoAccessException, AuthException, ChangeSetPersister.NotFoundException {
        photoApiService.deleteById(id);
        return OkResponse.of(HttpStatus.OK.toString());
    }
}
