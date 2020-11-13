package com.apeter.blog.user.mapping;

import com.apeter.blog.user.api.response.UserResponse;
import com.apeter.blog.user.model.UserDoc;
import lombok.Getter;

@Getter
public class UserMapping {
    public static class ResponseMapping{
        public UserResponse convert(UserDoc userDoc){
            return UserResponse.builder()
                    .id(userDoc.getId())
                    .firstName(userDoc.getFirstName())
                    .lastName(userDoc.getLastName())
                    .email(userDoc.getEmail())
                    .build();
        }
    }

    private final ResponseMapping responseMapping = new ResponseMapping();

    public static UserMapping getInstance(){
        return new UserMapping();
    }
}
