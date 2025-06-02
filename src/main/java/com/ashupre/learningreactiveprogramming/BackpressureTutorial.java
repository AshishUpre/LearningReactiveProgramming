package com.ashupre.learningreactiveprogramming;

import reactor.core.publisher.BufferOverflowStrategy;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class BackpressureTutorial {

    private Flux<Long> createNoOverflowFlux() {
        return Flux.range(1, Integer.MAX_VALUE)
                .log()
                // in real world -> we generally do some processing kind of operations that taek time
                // we are simulating that my adding delay here
                .concatMap(x -> Mono.delay(Duration.ofMillis(100)));
    }

    /**
     * Fails with error message:
     *
     * 22:38:04.550 [main] INFO reactor.Flux.Interval.1 -- onSubscribe(FluxInterval.IntervalRunnable)
     * 22:38:04.558 [main] INFO reactor.Flux.Interval.1 -- request(1)
     * 22:38:04.563 [parallel-1] INFO reactor.Flux.Interval.1 -- onNext(0)
     * 22:38:04.571 [parallel-1] ERROR reactor.Flux.Interval.1 -- onError(reactor.core.Exceptions$OverflowException: Could not emit tick 1 due to lack of requests (interval doesn't support small downstream requests that replenish slower than the ticks))
     * 22:38:04.571 [parallel-1] ERROR reactor.Flux.Interval.1 --
     * reactor.core.Exceptions$OverflowException: Could not emit tick 1 due to lack of requests (interval doesn't support small downstream requests that replenish slower than the ticks)
    `*
     * fails cause before processing curr data, new data is coming
     * one way to handle -- drop the data that cannot be handled
     */
    private Flux<Long> createOverflowFlux() {
        // every 1ms data will be generated
        return Flux.interval(Duration.ofMillis(1))
                .log()
                .concatMap(x -> Mono.delay(Duration.ofMillis(100)));
    }

    /**
     * drops additional data
     */
    private Flux<Long> createDropOnBackpressure() {
        return Flux.interval(Duration.ofMillis(1))
                // this drop should be just before our processing (concatmap)
                .onBackpressureDrop()
                .concatMap(x -> Mono.delay(Duration.ofMillis(100)).thenReturn(x))
                // if it reached here means it was processed by concatmap and sent to the consumer
                .doOnNext(x -> System.out.println("processed: " + x));
    }

    /**
     * drops additional data
     */
    private Flux<Long> createBufferOnBackpressure() {
        return Flux.interval(Duration.ofMillis(1))
                // this drop should be just before our processing (concatmap)
                //!.onBackpressureBuffer(50) ----------- not enough to fully buffer extra requests, will crash

                // below is a buffering strategy that will drop the latest requests when the buffer is full
                // still not able to process many requests but better than dropping all or crashing
                .onBackpressureBuffer(50, BufferOverflowStrategy.DROP_LATEST)

                .concatMap(x -> Mono.delay(Duration.ofMillis(100)).thenReturn(x))
                // if it reached here means it was processed by concatmap and sent to the consumer
                .doOnNext(x -> System.out.println("processed: " + x));
    }

    public static void main(String[] args) {
        BackpressureTutorial tutorial = new BackpressureTutorial();
        tutorial.createBufferOnBackpressure()
                // blocks until flux is complete, note - it will also create subscription so no need to subscribe
                .blockLast();
    }
}
