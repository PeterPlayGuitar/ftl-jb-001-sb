package com.apeter.blog.comment.api.response;

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
@ApiModel(value = "comment response", description = "comment short data")
public class CommentResponse {
        protected String id;
        protected String articleId;
        protected String userId;
        protected String message;
}
