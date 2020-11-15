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
@ApiModel(value = "Company", description = "Company")
public class Company {
    private String name;
    private Address address;
}
