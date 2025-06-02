package com.ashupre.learningreactiveprogramming.model;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document
@Getter
public class Customer {
    @Id
    private String id;
    private String name;
    private String job;

    public Customer(String name, String job) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.job = job;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", job='" + job + '\'' +
                '}';
    }
}
