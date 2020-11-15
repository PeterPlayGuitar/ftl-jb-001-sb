package com.apeter.blog.user.model;

import io.swagger.annotations.ApiModel;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@Builder
@ApiModel(value = "Point", description = "Geo point coordinate with lat and lng")
public class Point {
    private Double lat;
    private Double lng;

}
