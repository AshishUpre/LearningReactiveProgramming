package com.ashupre.learningreactiveprogramming.controller;

import com.ashupre.learningreactiveprogramming.model.Customer;
import com.ashupre.learningreactiveprogramming.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;


@RestController // means return is JSON (not html like @Controller)
@RequiredArgsConstructor
public class DataController {

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    @PostMapping("/customer/create")
    public Mono<Customer> createCustomer(@RequestBody Customer customer) {
        // save to db, as it is reactive, it will return a publisher, can return that
        return reactiveMongoTemplate.save(customer);
    }

    @GetMapping("/customer/findById")
    public Mono<Customer> findCustomerById(@RequestParam("customer-id") String id) {
        return getCustomerById(id);
    }

    private Mono<Customer> getCustomerById(String id) {
        // criteria, query and then template - mongodb process
        Criteria criteria = Criteria.where("id").is(id);
        Query query = new Query(criteria);

        return reactiveMongoTemplate.findOne(query, Customer.class)
                .log();
    }

    @PostMapping("/order/create")
    public Mono<Order> createOrder(@RequestBody Order order) {
        // save to db, as it is reactive, it will return a publisher, can return that
        return reactiveMongoTemplate.save(order);
    }

    /**
     * Expected output is sum of orders amount for each customer
     */
    @GetMapping("/sales/summary")
    public Mono<Map<String, Double>> calculateSummary() {
        return reactiveMongoTemplate.findAll(Customer.class)
                .flatMap(customer -> Mono.zip(Mono.just(customer), calculateOrderSum(customer.getId())))
                .collectMap(tuple2 -> tuple2.getT1().getName(),
                        tuple2 -> tuple2.getT2())
                .log();
    }

    public Mono<Double> calculateOrderSum(String customerId) {
        Criteria criteria = Criteria.where("customerId").is(customerId);
        Query query = new Query(criteria);
        return reactiveMongoTemplate.find(query, Order.class)
                // order obj converted to Double obj
                .map(order -> order.getTotal())
                .reduce((total, amount) -> total + amount)
                .log();
    }
}
