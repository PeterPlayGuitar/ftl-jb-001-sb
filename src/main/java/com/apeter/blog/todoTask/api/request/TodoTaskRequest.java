package com.apeter.blog.todoTask.api.request;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.List;

@Getter
@Setter
@ApiModel(value = "TodoTask request", description = "model for update todoTask")
public class TodoTaskRequest {
            private ObjectId id;
            private String title;
            private ObjectId ownerId;
            private Boolean completed;
            private List<ObjectId> files;
}
