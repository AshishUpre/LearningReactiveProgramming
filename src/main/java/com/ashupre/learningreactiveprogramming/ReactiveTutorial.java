package com.ashupre.learningreactiveprogramming;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

public class ReactiveTutorial {

    private Mono<String> testMono() {
        return Mono.empty();
    }

    private Flux<String> testFlux() {
        List<String> languages = List.of("java", "go", "rust", "kotlin");

        return Flux.fromIterable(languages).log();
    }

    private Flux<List<Integer>> testBuffer() {
        return Flux.range(1, 100).delayElements(Duration.ofMillis(100)).buffer(Duration.ofMillis(510));
    }

    public static void main(String[] args) throws InterruptedException {
        ReactiveTutorial reactiveTutorial = new ReactiveTutorial();
//        reactiveTutorial.testMono()
//                .subscribe(data -> System.out.println(data));
        reactiveTutorial.testBuffer().subscribe(data -> System.out.println(data));

        Thread.sleep(50000);
    }
}
