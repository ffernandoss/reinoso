package org.example.reinoso;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

public class RobotFluxGenerator {

    private static final Logger logger = LoggerFactory.getLogger(RobotFluxGenerator.class);

    public Flux<Robot> generateRobotFlux() {
        AtomicInteger robotCounter = new AtomicInteger(1);

        return Flux.interval(Duration.ofSeconds(1))
                   .filter(tick -> tick % 5 == 0)
                   .map(tick -> new Robot(robotCounter.getAndIncrement()));
    }
}