package com.ashupre.learningreactiveprogramming.controller;

import com.ashupre.learningreactiveprogramming.model.Customer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


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

}
