package com.apeter.blog.user.mapping;

import com.apeter.blog.base.api.response.SearchResponse;
import com.apeter.blog.base.mapping.BaseMapping;
import com.apeter.blog.user.api.request.UserRequest;
import com.apeter.blog.user.api.response.UserFullResponse;
import com.apeter.blog.user.api.response.UserResponse;
import com.apeter.blog.user.model.UserDoc;
import lombok.Getter;
import org.bson.types.ObjectId;

import java.util.stream.Collectors;

@Getter
public class UserMapping {
    public static class RequestMapping {

        public UserDoc convert(UserRequest userRequest, ObjectId objectId) {
            return UserDoc.builder()
                    .id(objectId)
                    .firstName(userRequest.getFirstName())
                    .lastName(userRequest.getLastName())
                    .email(userRequest.getEmail())
                    .build();
        }
    }


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

    public static class SearchMapping extends BaseMapping<SearchResponse<UserDoc>, SearchResponse<UserResponse>> {

        private ResponseMapping responseMapping = new ResponseMapping();

        @Override
        public SearchResponse<UserResponse> convert(SearchResponse<UserDoc> searchResponse) {
            return SearchResponse.of(
                    searchResponse.getList().stream().map(responseMapping::convert).collect(Collectors.toList()),
                    searchResponse.getCount()
            );
        }

        @Override
        public SearchResponse<UserDoc> revert(SearchResponse<UserResponse> userResponses) {
            throw new RuntimeException("don't use this");
        }
    }

    private final RequestMapping requestMapping = new RequestMapping();
    private final ResponseMapping responseMapping = new ResponseMapping();
    private final ResponseFullMapping responseFullMapping = new ResponseFullMapping();
    private final SearchMapping searchMapping = new SearchMapping();

    public static UserMapping getInstance() {
        return new UserMapping();
    }
}
