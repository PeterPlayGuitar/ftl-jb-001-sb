package com.apeter.blog.user.model;

import io.swagger.annotations.ApiModel;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
@NoArgsConstructor
@ApiModel(value = "Adress", description = "User or company physicaladress")
public class Address {
    private String city;
    private String street;
    private String suite;
    private String zipcode;
    private Point point;
}
