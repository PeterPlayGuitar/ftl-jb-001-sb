package com.apeter.blog.user.repository;

import com.apeter.blog.user.model.UserDoc;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<UserDoc, ObjectId> {
    Optional<UserDoc> findByEmail(String email);
}
