package com.apeter.blog.user.api.request;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "Registration request", description = "Model for registration")
public class RegistrationRequest {
    private String email;
    private String password;
}
