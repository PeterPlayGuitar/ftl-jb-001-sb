package com.apeter.blog.file.api.response;

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
@ApiModel(value = "file response", description = "file short data")
public class FileResponse {
    protected String id;
    protected String title;
    protected String ownerId;
    protected String contentType;
}
