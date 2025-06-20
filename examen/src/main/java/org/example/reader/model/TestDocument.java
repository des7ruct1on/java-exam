package com.example.reader.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("test_collection")
public class TestDocument {

    @Id
    private String id;

    @Field("test_field")
    private String testField;

    @Field("test_value_field")
    private String testValueField;

    public String getId() {
        return id;
    }

    public String getTestField() {
        return testField;
    }

    public void setTestField(String testField) {
        this.testField = testField;
    }

    public String getTestValueField() {
        return testValueField;
    }

    public void setTestValueField(String testValueField) {
        this.testValueField = testValueField;
    }
}
