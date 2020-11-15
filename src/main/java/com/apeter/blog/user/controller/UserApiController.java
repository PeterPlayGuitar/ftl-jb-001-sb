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
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.jni.Local;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserApiController {
    private final UserApiService userApiService;

    @PostMapping(UserApiRoutes.ROOT)
    public OkResponse<UserFullResponse> registration(@RequestBody RegistrationRequest request) throws UserExistException {
//        Integer i = 3/0;

        return OkResponse.of(UserMapping.getInstance().getResponseFullMapping().convert(userApiService.registration(request)));
    }

    @GetMapping(UserApiRoutes.BY_ID)
    public OkResponse<UserFullResponse> byId(@PathVariable ObjectId id) throws ChangeSetPersister.NotFoundException {
        return OkResponse.of(UserMapping.getInstance().getResponseFullMapping().convert(
                userApiService.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new)
        ));
    }

    @GetMapping(UserApiRoutes.ROOT)
    public OkResponse<SearchResponse<UserResponse>> search(
            @ModelAttribute SearchRequest request
    ) {
        return OkResponse.of(UserMapping.getInstance().getSearchMapping().convert(
                userApiService.search(request)
        ));
    }

    @PutMapping(UserApiRoutes.BY_ID)
    public OkResponse<UserFullResponse> update(
            @PathVariable String id,
            @RequestBody UserRequest request
    ) throws UserNoExistException {
        return OkResponse.of(UserMapping.getInstance().getResponseFullMapping().convert(
                userApiService.update(request)
        ));
    }

    @DeleteMapping(UserApiRoutes.BY_ID)
    public OkResponse<String> deleteById(@PathVariable ObjectId id) {
        userApiService.deleteById(id);
        return OkResponse.of(HttpStatus.OK.toString());
    }
}
