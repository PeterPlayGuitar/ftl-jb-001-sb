package com.apeter.blog.photo.repository;

import com.apeter.blog.photo.model.PhotoDoc;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhotoRepository extends MongoRepository<PhotoDoc, ObjectId> {
}
