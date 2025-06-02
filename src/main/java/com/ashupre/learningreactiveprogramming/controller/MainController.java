package com.ashupre.learningreactiveprogramming.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/")
public class MainController {

    @GetMapping("/")
    public Mono<String> handleMain() {
        // in normal programming, we just return "home" (template name)
        return Mono.just("home");
    }
}
