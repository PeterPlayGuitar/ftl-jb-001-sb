package com.apeter.blog.user.api.request;

import com.apeter.blog.user.model.Address;
import com.apeter.blog.user.model.Company;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
@ApiModel(value = "User request", description = "model for update user")
public class UserRequest {
    private ObjectId id;
    private String firstName;
    private String lastName;
    private String email;
    private Address address = new Address();
    private Company company = new Company();
}
