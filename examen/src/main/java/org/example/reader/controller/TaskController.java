package com.example.reader.controller;

import com.example.reader.model.TestDocument;
import com.example.reader.model.TestRepository;
import com.example.reader.service.KafkaMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private KafkaMessageService kafkaMessageService;

    @Autowired
    private TestRepository testRepository;

    @GetMapping("/read-topic")
    public ResponseEntity<String> readFromTopic(@RequestParam String bootstrapServers, @RequestParam String topic) {

        String testValue = kafkaMessageService.readFirstMessage(bootstrapServers, topic);
        if (testValue == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Kafka message not found");
        }

        Optional<TestDocument> doc = testRepository.findByTestField(testValue);
        if (doc.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Document not found");
        }

        String result = new StringBuilder(doc.get().getTestValueField().toUpperCase()).reverse().toString();
        return ResponseEntity.ok(result);
    }
}
