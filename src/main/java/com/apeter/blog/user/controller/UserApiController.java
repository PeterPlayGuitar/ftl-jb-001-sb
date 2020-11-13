package com.apeter.blog.user.controller;

import com.apeter.blog.user.routes.UserApiRoutes;
import com.apeter.blog.user.api.request.RegistrationRequest;
import com.apeter.blog.user.api.response.UserResponse;
import com.apeter.blog.user.exception.UserExistException;
import com.apeter.blog.user.mapping.UserMapping;
import com.apeter.blog.user.service.UserApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserApiController {
    private final UserApiService userApiService;

    @PostMapping(UserApiRoutes.ROOT)
    public UserResponse registration(@RequestBody RegistrationRequest request) throws UserExistException {
        return UserMapping.getInstance().getResponseMapping().convert(userApiService.registration(request));
    }
}
