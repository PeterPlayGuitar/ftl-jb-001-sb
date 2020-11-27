package com.apeter.blog.todoTask.service;

import com.apeter.blog.auth.exceptions.AuthException;
import com.apeter.blog.auth.exceptions.NoAccessException;
import com.apeter.blog.auth.service.AuthService;
import com.apeter.blog.base.api.request.SearchRequest;
import com.apeter.blog.base.api.response.SearchResponse;
import com.apeter.blog.base.service.CheckAccess;
import com.apeter.blog.todoTask.api.request.TodoTaskRequest;
import com.apeter.blog.todoTask.api.request.TodoTaskSearchRequest;
import com.apeter.blog.todoTask.exception.TodoTaskExistException;
import com.apeter.blog.todoTask.exception.TodoTaskNoExistException;
import com.apeter.blog.todoTask.mapping.TodoTaskMapping;
import com.apeter.blog.todoTask.model.TodoTaskDoc;
import com.apeter.blog.todoTask.repository.TodoTaskRepository;
import com.apeter.blog.user.exception.UserNoExistException;
import com.apeter.blog.user.model.UserDoc;
import com.apeter.blog.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
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
public class TodoTaskApiService extends CheckAccess<TodoTaskDoc> {
    private final TodoTaskRepository todoTaskRepository;
    private final MongoTemplate mongoTemplate;
    private final UserRepository userRepository;
    private final AuthService authService;

    public TodoTaskDoc create(TodoTaskRequest request) throws AuthException {

        UserDoc userDoc = authService.currentUser();

        TodoTaskDoc todoTaskDoc = TodoTaskMapping.getInstance().getRequestMapping().convert(request, userDoc.getId());
        todoTaskRepository.save(todoTaskDoc);
        return todoTaskDoc;
    }

    public Optional<TodoTaskDoc> findById(ObjectId id) {
        return todoTaskRepository.findById(id);
    }

    public SearchResponse<TodoTaskDoc> search(
            TodoTaskSearchRequest request
    ) throws AuthException {
        UserDoc userDoc = authService.currentUser();

        Criteria criteria = Criteria.where("ownerId").is(userDoc.getId());
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

    public TodoTaskDoc update(TodoTaskRequest request) throws TodoTaskNoExistException, NoAccessException, AuthException {
        Optional<TodoTaskDoc> todoTaskDocOptional = todoTaskRepository.findById(request.getId());
        if (!todoTaskDocOptional.isPresent()) {
            throw new TodoTaskNoExistException();
        }

        TodoTaskDoc oldTask = todoTaskDocOptional.get();
        UserDoc userDoc = checkAccess(oldTask);

        TodoTaskDoc todoTaskDoc = TodoTaskMapping.getInstance().getRequestMapping().convert(request, userDoc.getId());

        todoTaskDoc.setId(request.getId());
        todoTaskDoc.setOwnerId(oldTask.getOwnerId());
        todoTaskRepository.save(todoTaskDoc);

        return todoTaskDoc;
    }

    public void deleteById(ObjectId id) throws NoAccessException, AuthException, ChangeSetPersister.NotFoundException {
        checkAccess(todoTaskRepository.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new));
        todoTaskRepository.deleteById(id);
    }

    @Override
    protected ObjectId getOwnerFromEntity(TodoTaskDoc entity) {
        return entity.getOwnerId();
    }

    @Override
    protected AuthService authService() {
        return authService;
    }
}
