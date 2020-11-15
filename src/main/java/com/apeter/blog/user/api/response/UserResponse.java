package com.apeter.blog.user.api.response;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@ApiModel(value = "user response", description = "user short data")
public class UserResponse {
    protected String id;
    protected String firstName;
    protected String lastName;
    protected String email;

}
