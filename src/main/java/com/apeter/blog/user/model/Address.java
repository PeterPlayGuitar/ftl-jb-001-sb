package com.apeter.blog.user.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Address {
    private String city;
    private String street;
    private String suite;
    private String zipcode;
    private String Point;
}
