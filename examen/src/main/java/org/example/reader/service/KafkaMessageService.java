package com.example.reader.service;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

@Service
public class KafkaMessageService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaMessageService.class);

    @Value("${spring.kafka.bootstrap-servers}")
    private String defaultBootstrapServers;

    public String readFirstMessage(String bootstrapServers, String topic) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapServers != null && !bootstrapServers.isBlank()
                        ? bootstrapServers
                        : defaultBootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");

        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(Arrays.asList(topic));

            ConsumerRecords<String, String> records;
            int attempts = 0;
            do {
                records = consumer.poll(Duration.ofSeconds(1));
                attempts++;
            } while (records.isEmpty() && attempts < 5);

            if (!records.isEmpty()) {
                return records.iterator().next().value();
            } else {
                logger.warn("No records found in topic [{}]", topic);
            }
        } catch (Exception e) {
            logger.error("Kafka read error on topic [{}]: {}", topic, e.getMessage(), e);
        }

        return null;
    }
}
