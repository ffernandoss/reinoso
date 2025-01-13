package org.example.reinoso;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.awt.*;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RobotFluxGenerator {

    @Autowired
    private RobotService robotService;

    private static final String[] TASK_CATEGORIES = {"cutting", "folding", "stacking", "transporting"};

    public Flux<Robot> generateRobotFlux() {
        AtomicInteger robotCounter = new AtomicInteger(1);

        return Flux.interval(Duration.ofSeconds(1))
                .filter(tick -> tick % 5 == 0)
                .map(tick -> {
                    String category = TASK_CATEGORIES[(int) (Math.random() * TASK_CATEGORIES.length)];
                    return new Robot(robotCounter.getAndIncrement(), category);
                })
                .doOnNext(robotService::sendRobot);
    }
}
