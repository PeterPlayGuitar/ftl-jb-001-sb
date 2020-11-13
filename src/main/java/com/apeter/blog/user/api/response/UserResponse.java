package com.apeter.blog.user.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
public class UserResponse {
    protected ObjectId id;
    protected String firstName;
    protected String lastName;
    protected String email;

}