package com.apeter.blog.auth.controller;

import com.apeter.blog.auth.api.request.AuthRequest;
import com.apeter.blog.auth.api.response.AuthResponse;
import com.apeter.blog.auth.exceptions.AuthException;
import com.apeter.blog.auth.routes.AuthRoutes;
import com.apeter.blog.auth.service.AuthService;
import com.apeter.blog.base.api.response.OkResponse;
import com.apeter.blog.user.api.request.RegistrationRequest;
import com.apeter.blog.user.api.response.UserFullResponse;
import com.apeter.blog.user.exception.UserExistException;
import com.apeter.blog.user.exception.UserNoExistException;
import com.apeter.blog.user.mapping.UserMapping;
import com.apeter.blog.user.service.UserApiService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final UserApiService userApiService;
    private final AuthService authService;

    @PostMapping(AuthRoutes.REGISTRATION)
    @ApiOperation(value = "Register", notes = "Use this when you need to register new user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "User already exists")
    })
    public OkResponse<UserFullResponse> registration(@RequestBody RegistrationRequest request) throws UserExistException {
        return OkResponse.of(UserMapping.getInstance().getResponseFullMapping().convert(userApiService.registration(request)));
    }

    @PostMapping(AuthRoutes.AUTH)
    @ApiOperation(value = "Authorize", notes = "Use this when you need to authorize")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "User not exist"),
            @ApiResponse(code = 402, message = "Bad password")
    })
    public OkResponse<AuthResponse> auth(@RequestBody AuthRequest request) throws AuthException, UserNoExistException {
        return OkResponse.of(authService.auth(request));
    }
}
