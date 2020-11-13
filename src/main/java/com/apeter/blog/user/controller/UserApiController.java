package com.apeter.blog.user.controller;

import com.apeter.blog.user.api.response.UserFullResponse;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserApiController {
    private final UserApiService userApiService;

    @PostMapping(UserApiRoutes.ROOT)
    public UserFullResponse registration(@RequestBody RegistrationRequest request) throws UserExistException {
        return UserMapping.getInstance().getResponseFullMapping().convert(userApiService.registration(request));
    }

    @GetMapping(UserApiRoutes.BY_ID)
    public UserFullResponse byId(@PathVariable ObjectId id) throws ChangeSetPersister.NotFoundException {
        return UserMapping.getInstance().getResponseFullMapping().convert(
                userApiService.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new)
        );
    }

    @GetMapping(UserApiRoutes.ROOT)
    public List<UserResponse> search(
            @RequestParam(required = false) String query,
            @RequestParam(required = false, defaultValue = "0") Integer size,
            @RequestParam(required = false, defaultValue = "0") Long skip
    ) {
        return UserMapping.getInstance().getSearchMapping().convert(
                userApiService.search(query, size, skip)
        );
    }

}
