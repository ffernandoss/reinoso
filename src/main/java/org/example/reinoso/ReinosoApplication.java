package org.example.reinoso;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReinosoApplication implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(ReinosoApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ReinosoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        RobotFluxGenerator robotFluxGenerator = new RobotFluxGenerator();
        robotFluxGenerator.generateRobotFlux()
                          .subscribe(robot -> logger.info("Generated: " + robot));
    }
}