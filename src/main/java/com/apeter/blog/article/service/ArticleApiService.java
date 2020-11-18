package com.apeter.blog.article.service;

import com.apeter.blog.article.mapping.ArticleMapping;
import com.apeter.blog.base.api.request.SearchRequest;
import com.apeter.blog.base.api.response.SearchResponse;
import com.apeter.blog.article.api.request.ArticleRequest;
import com.apeter.blog.article.exception.ArticleExistException;
import com.apeter.blog.article.exception.ArticleNoExistException;
import com.apeter.blog.article.model.ArticleDoc;
import com.apeter.blog.article.repository.ArticleRepository;
import com.apeter.blog.user.exception.UserNoExistException;
import com.apeter.blog.user.model.UserDoc;
import com.apeter.blog.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleApiService {
    private final ArticleRepository articleRepository;
    private final MongoTemplate mongoTemplate;
    private final UserRepository userRepository;

    public ArticleDoc create(ArticleRequest request) throws ArticleExistException, UserNoExistException {

        Optional<UserDoc> userDoc = userRepository.findById(request.getOwnerId());
        if(!userDoc.isPresent()) throw new UserNoExistException();

        ArticleDoc articleDoc = ArticleMapping.getInstance().getRequestMapping().convert(request);
        articleRepository.save(articleDoc);
        return articleDoc;
    }

    public Optional<ArticleDoc> findById(ObjectId id) {
        return articleRepository.findById(id);
    }

    public SearchResponse<ArticleDoc> search(
            SearchRequest request
    ) {
        Criteria criteria = new Criteria();
        if (request.getQuery() != null && !request.getQuery().equals("")) {
            criteria = criteria.orOperator(
                    Criteria.where("title").regex(request.getQuery(), "i"),
                    Criteria.where("body").regex(request.getQuery(), "i")
            );
        }

        Query query = new Query(criteria);
        Long count = mongoTemplate.count(query, ArticleDoc.class);


        query.limit(request.getSize());
        query.skip(request.getSkip());

        List<ArticleDoc> articleDocs = mongoTemplate.find(query, ArticleDoc.class);
        return SearchResponse.of(articleDocs, count);
    }

    public ArticleDoc update(ArticleRequest request) throws ArticleNoExistException {
        Optional<ArticleDoc> articleDocOptional = articleRepository.findById(request.getId());
        if (!articleDocOptional.isPresent()) {
            throw new ArticleNoExistException();
        }

        ArticleDoc oldDoc = articleDocOptional.get();

        ArticleDoc articleDoc = ArticleMapping.getInstance().getRequestMapping().convert(request);
        articleDoc.setId(request.getId());
        articleDoc.setOwnerId(oldDoc.getOwnerId());
        articleRepository.save(articleDoc);

        return articleDoc;
    }

    public void deleteById(ObjectId id) {
        articleRepository.deleteById(id);
    }
}
