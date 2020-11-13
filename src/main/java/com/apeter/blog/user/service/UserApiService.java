package com.apeter.blog.user.service;

import com.apeter.blog.user.api.request.RegistrationRequest;
import com.apeter.blog.user.exception.UserExistException;
import com.apeter.blog.user.model.UserDoc;
import com.apeter.blog.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
@RequiredArgsConstructor
public class UserApiService {
    private final UserRepository userRepository;
    private final MongoTemplate mongoTemplate;

    public UserDoc registration(RegistrationRequest request) throws UserExistException {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserExistException();
        }

        UserDoc userDoc = new UserDoc();
        userDoc.setEmail(request.getEmail());
        userDoc.setPassword(DigestUtils.md5DigestAsHex(request.getPassword().getBytes()));
        userDoc = userRepository.save(userDoc);

        return userDoc;
    }
}