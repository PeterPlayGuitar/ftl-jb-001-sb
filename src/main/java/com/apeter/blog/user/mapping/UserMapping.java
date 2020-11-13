package com.apeter.blog.user.mapping;

import com.apeter.blog.base.mapping.BaseMapping;
import com.apeter.blog.user.api.response.UserFullResponse;
import com.apeter.blog.user.api.response.UserResponse;
import com.apeter.blog.user.model.UserDoc;
import lombok.Getter;

@Getter
public class UserMapping {
    public static class ResponseMapping extends BaseMapping<UserDoc, UserResponse> {

        @Override
        public UserResponse convert(UserDoc userDoc) {
            return UserResponse.builder()
                    .id(userDoc.getId().toString())
                    .firstName(userDoc.getFirstName())
                    .lastName(userDoc.getLastName())
                    .email(userDoc.getEmail())
                    .build();
        }

        @Override
        public UserDoc revert(UserResponse userResponse) {
            throw new RuntimeException("don't use this");
        }
    }

    public static class ResponseFullMapping extends BaseMapping<UserDoc, UserFullResponse> {

        @Override
        public UserFullResponse convert(UserDoc userDoc) {
            return UserFullResponse.builder()
                    .id(userDoc.getId().toString())
                    .firstName(userDoc.getFirstName())
                    .lastName(userDoc.getLastName())
                    .email(userDoc.getEmail())
                    .address(userDoc.getAddress())
                    .company(userDoc.getCompany())
                    .build();
        }

        @Override
        public UserDoc revert(UserFullResponse userFullResponse) {
            throw new RuntimeException("don't use this");
        }
    }

    private final ResponseMapping responseMapping = new ResponseMapping();
    private final ResponseFullMapping responseFullMapping = new ResponseFullMapping();

    public static UserMapping getInstance() {
        return new UserMapping();
    }
}
