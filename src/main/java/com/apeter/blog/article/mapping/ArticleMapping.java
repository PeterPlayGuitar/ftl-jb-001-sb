package com.apeter.blog.article.mapping;

import com.apeter.blog.base.api.response.SearchResponse;
import com.apeter.blog.base.mapping.BaseMapping;
import com.apeter.blog.article.api.request.ArticleRequest;
import com.apeter.blog.article.api.response.ArticleResponse;
import com.apeter.blog.article.model.ArticleDoc;
import lombok.Getter;
import org.bson.types.ObjectId;

import java.util.stream.Collectors;

@Getter
public class ArticleMapping {
    public static class RequestMapping {

        public ArticleDoc convert(ArticleRequest articleRequest, ObjectId ownerId) {
            return ArticleDoc.builder()
                    .id(articleRequest.getId())
                    .title(articleRequest.getTitle())
                    .body(articleRequest.getBody())
                    .ownerId(ownerId)
                    .build();
        }
    }


    public static class ResponseMapping extends BaseMapping<ArticleDoc, ArticleResponse> {

        @Override
        public ArticleResponse convert(ArticleDoc articleDoc) {
            return ArticleResponse.builder()
                    .id(articleDoc.getId().toString())
                    .title(articleDoc.getTitle())
                    .body(articleDoc.getBody())
                    .ownerId(articleDoc.getOwnerId() != null ? articleDoc.getOwnerId().toString() : null)
                    .build();
        }

        @Override
        public ArticleDoc revert(ArticleResponse articleResponse) {
            throw new RuntimeException("don't use this");
        }
    }

    public static class SearchMapping extends BaseMapping<SearchResponse<ArticleDoc>, SearchResponse<ArticleResponse>> {

        private ResponseMapping responseMapping = new ResponseMapping();

        @Override
        public SearchResponse<ArticleResponse> convert(SearchResponse<ArticleDoc> searchResponse) {
            return SearchResponse.of(
                    searchResponse.getList().stream().map(responseMapping::convert).collect(Collectors.toList()),
                    searchResponse.getCount()
            );
        }

        @Override
        public SearchResponse<ArticleDoc> revert(SearchResponse<ArticleResponse> articleResponses) {
            throw new RuntimeException("don't use this");
        }
    }

    private final RequestMapping requestMapping = new RequestMapping();
    private final ResponseMapping responseMapping = new ResponseMapping();
    private final SearchMapping searchMapping = new SearchMapping();

    public static ArticleMapping getInstance() {
        return new ArticleMapping();
    }
}
