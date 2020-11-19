package com.apeter.blog.todoTask.api.request;

import com.apeter.blog.base.api.request.SearchRequest;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
public class TodoTaskSearchRequest extends SearchRequest {
    @ApiParam(name = "Owner Id", value = "Search by ownerId", required = true)
    private ObjectId ownerId;
}
