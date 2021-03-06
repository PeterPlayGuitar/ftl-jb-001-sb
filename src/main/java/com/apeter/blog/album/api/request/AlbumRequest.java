package com.apeter.blog.album.api.request;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
@ApiModel(value = "Album request", description = "model for update album")
public class AlbumRequest {
    private ObjectId id;
    private String title;
}
