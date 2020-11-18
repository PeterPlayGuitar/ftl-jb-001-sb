package com.apeter.blog.file.repository;

import com.apeter.blog.file.model.FileDoc;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends MongoRepository<FileDoc, ObjectId> {
}
