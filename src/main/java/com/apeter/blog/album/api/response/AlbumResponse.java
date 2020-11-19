package com.apeter.blog.album.api.response;

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
@ApiModel(value = "album response", description = "album short data")
public class AlbumResponse {
        protected String id;
        protected String title;
        protected String ownerId;
}
