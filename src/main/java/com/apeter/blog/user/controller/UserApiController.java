package com.apeter.blog.user.controller;

import com.apeter.blog.base.api.request.SearchRequest;
import com.apeter.blog.base.api.response.OkResponse;
import com.apeter.blog.base.api.response.SearchResponse;
import com.apeter.blog.user.api.request.UserRequest;
import com.apeter.blog.user.api.response.UserFullResponse;
import com.apeter.blog.user.exception.UserNoExistException;
import com.apeter.blog.user.routes.UserApiRoutes;
import com.apeter.blog.user.api.request.RegistrationRequest;
import com.apeter.blog.user.api.response.UserResponse;
import com.apeter.blog.user.exception.UserExistException;
import com.apeter.blog.user.mapping.UserMapping;
import com.apeter.blog.user.service.UserApiService;
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
@Api(value = "User API")
public class UserApiController {
    private final UserApiService userApiService;

    @PostMapping(UserApiRoutes.ROOT)
    @ApiOperation(value = "Register", notes = "Use this when you need to register new user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "User already exists")
    })
    public OkResponse<UserFullResponse> registration(@RequestBody RegistrationRequest request) throws UserExistException {
//        Integer i = 3/0;

        return OkResponse.of(UserMapping.getInstance().getResponseFullMapping().convert(userApiService.registration(request)));
    }

    @GetMapping(UserApiRoutes.BY_ID)
    @ApiOperation(value = "Find user by if", notes = "Use this when you need to find user by id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Success"),
                    @ApiResponse(code = 404, message = "User not found")
            }
    )
    public OkResponse<UserFullResponse> byId(
            @ApiParam(value = "User id") @PathVariable ObjectId id
    ) throws ChangeSetPersister.NotFoundException {
        return OkResponse.of(UserMapping.getInstance().getResponseFullMapping().convert(
                userApiService.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new)
        ));
    }

    @GetMapping(UserApiRoutes.ROOT)
    @ApiOperation(value = "Search users", notes = "Use this when you need to search users by last name first name or email with skip and size of the response")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Success")
            }
    )
    public OkResponse<SearchResponse<UserResponse>> search(
            @ModelAttribute SearchRequest request
    ) {
        return OkResponse.of(UserMapping.getInstance().getSearchMapping().convert(
                userApiService.search(request)
        ));
    }

    @PutMapping(UserApiRoutes.BY_ID)
    @ApiOperation(value = "update user", notes = "Use this when you need to update user")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Success")
            }
    )
    public OkResponse<UserFullResponse> update(
            @ApiParam(value = "User id") @PathVariable String id,
            @RequestBody UserRequest request
    ) throws UserNoExistException {
        return OkResponse.of(UserMapping.getInstance().getResponseFullMapping().convert(
                userApiService.update(request)
        ));
    }

    @DeleteMapping(UserApiRoutes.BY_ID)
    @ApiOperation(value = "delete user by id", notes = "Use this when you need to delete user by id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Success")
            }
    )
    public OkResponse<String> deleteById(
            @ApiParam(value = "User id")
            @PathVariable ObjectId id
    ) {
        userApiService.deleteById(id);
        return OkResponse.of(HttpStatus.OK.toString());
    }
}
