package com.example.reader.model;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TestRepository extends MongoRepository<TestDocument, String> {
    Optional<TestDocument> findByTestField(String testField);
}
