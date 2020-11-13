package com.apeter.blog.user.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@Builder
public class Point {
    private Double lat;
    private Double lng;

}
