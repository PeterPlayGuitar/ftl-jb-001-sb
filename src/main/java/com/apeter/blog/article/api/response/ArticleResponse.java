package com.apeter.blog.article.api.response;

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
@ApiModel(value = "article response", description = "article short data")
public class ArticleResponse {
        protected String id;
        protected String title;
        protected String body;
        protected String ownerId;
}
