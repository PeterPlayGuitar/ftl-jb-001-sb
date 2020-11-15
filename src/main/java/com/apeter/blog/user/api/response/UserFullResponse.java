package com.apeter.blog.user.api.response;

import com.apeter.blog.user.model.Address;
import com.apeter.blog.user.model.Company;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@ApiModel(value = "user full response", description = "user full response extended by UserResponse class")
public class UserFullResponse extends UserResponse {
    private Address address;
    private Company company;
}
