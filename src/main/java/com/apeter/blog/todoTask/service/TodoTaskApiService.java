package com.apeter.blog.todoTask.service;

import com.apeter.blog.base.api.request.SearchRequest;
import com.apeter.blog.base.api.response.SearchResponse;
import com.apeter.blog.todoTask.api.request.TodoTaskRequest;
import com.apeter.blog.todoTask.api.request.TodoTaskSearchRequest;
import com.apeter.blog.todoTask.exception.TodoTaskExistException;
import com.apeter.blog.todoTask.exception.TodoTaskNoExistException;
import com.apeter.blog.todoTask.mapping.TodoTaskMapping;
import com.apeter.blog.todoTask.model.TodoTaskDoc;
import com.apeter.blog.todoTask.repository.TodoTaskRepository;
import com.apeter.blog.user.exception.UserNoExistException;
import com.apeter.blog.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TodoTaskApiService {
    private final TodoTaskRepository todoTaskRepository;
    private final MongoTemplate mongoTemplate;
    private final UserRepository userRepository;

    public TodoTaskDoc create(TodoTaskRequest request) throws TodoTaskExistException, UserNoExistException {

        if (userRepository.findById(request.getOwnerId()).isEmpty())
            throw new UserNoExistException();

        TodoTaskDoc todoTaskDoc = TodoTaskMapping.getInstance().getRequestMapping().convert(request);
        todoTaskRepository.save(todoTaskDoc);
        return todoTaskDoc;
    }

    public Optional<TodoTaskDoc> findById(ObjectId id) {
        return todoTaskRepository.findById(id);
    }

    public SearchResponse<TodoTaskDoc> search(
            TodoTaskSearchRequest request
    ) {
        if(request.getOwnerId() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        Criteria criteria = Criteria.where("ownerId").is(request.getOwnerId());
        if (request.getQuery() != null && !request.getQuery().equals("")) {
            criteria = criteria.orOperator(
                    Criteria.where("title").regex(request.getQuery(), "i")
            );
        }

        Query query = new Query(criteria);
        Long count = mongoTemplate.count(query, TodoTaskDoc.class);


        query.limit(request.getSize());
        query.skip(request.getSkip());

        List<TodoTaskDoc> todoTaskDocs = mongoTemplate.find(query, TodoTaskDoc.class);
        return SearchResponse.of(todoTaskDocs, count);
    }

    public TodoTaskDoc update(TodoTaskRequest request) throws TodoTaskNoExistException {
        Optional<TodoTaskDoc> todoTaskDocOptional = todoTaskRepository.findById(request.getId());
        if (!todoTaskDocOptional.isPresent()) {
            throw new TodoTaskNoExistException();
        }

        TodoTaskDoc oldTask = todoTaskDocOptional.get();

        TodoTaskDoc todoTaskDoc = TodoTaskMapping.getInstance().getRequestMapping().convert(request);

        todoTaskDoc.setId(request.getId());
        todoTaskDoc.setOwnerId(oldTask.getOwnerId());
        todoTaskRepository.save(todoTaskDoc);

        return todoTaskDoc;
    }

    public void deleteById(ObjectId id) {
        todoTaskRepository.deleteById(id);
    }
}
