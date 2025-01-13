package org.example.reinoso;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RobotFluxGenerator {

    @Autowired
    private RobotService robotService;

    private static final String[] TASK_CATEGORIES = {"cortar", "doblar", "apilar", "transportar"};
    private volatile boolean running = true;

    public Flux<Robot> generateRobotFlux() {
        AtomicInteger robotCounter = new AtomicInteger(1);

        return Flux.interval(Duration.ofSeconds(1))
                .filter(tick -> running && tick % 5 == 0)
                .map(tick -> {
                    String category = TASK_CATEGORIES[(int) (Math.random() * TASK_CATEGORIES.length)];
                    return new Robot(robotCounter.getAndIncrement(), category);
                })
                .doOnNext(robotService::sendRobot);
    }

    public void stop() {
        running = false;
    }
}