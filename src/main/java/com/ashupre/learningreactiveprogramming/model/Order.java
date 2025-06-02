package com.ashupre.learningreactiveprogramming.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document
@Data
public class Order {

    @Id
    private String id;
    private String customerId;
    private Double total;
    private Double discount;

    public Order(String customerId, Double total, Double discount) {
        this.id = UUID.randomUUID().toString();
        this.customerId = customerId;
        this.total = total;
        this.discount = discount;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", customerId='" + customerId + '\'' +
                ", total=" + total +
                ", discount=" + discount +
                '}';
    }
}
