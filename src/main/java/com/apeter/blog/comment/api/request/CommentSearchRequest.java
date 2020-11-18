package com.apeter.blog.comment.api.request;

import com.apeter.blog.base.api.request.SearchRequest;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
public class CommentSearchRequest extends SearchRequest {
    @ApiParam(name = "articleId", value = "Search by article Id", required = false)
    private ObjectId articleId;
    @ApiParam(name = "userId", value = "Search by user Id Id", required = false)
    private ObjectId userId;
}
