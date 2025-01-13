package org.example.reinoso;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class RobotTaskConsumer {

    private static final Logger logger = LoggerFactory.getLogger(RobotTaskConsumer.class);

    @KafkaListener(topics = "robot-tasks-cutting", groupId = "robot-group")
    public void processCuttingTask(Robot robot) {
        logger.info("Processing cutting task for: " + robot);
        // Lógica específica para cortar
    }

    @KafkaListener(topics = "robot-tasks-folding", groupId = "robot-group")
    public void processFoldingTask(Robot robot) {
        logger.info("Processing folding task for: " + robot);
        // Lógica específica para doblar
    }

    @KafkaListener(topics = "robot-tasks-stacking", groupId = "robot-group")
    public void processStackingTask(Robot robot) {
        logger.info("Processing stacking task for: " + robot);
        // Lógica específica para apilar
    }

    @KafkaListener(topics = "robot-tasks-transporting", groupId = "robot-group")
    public void processTransportingTask(Robot robot) {
        logger.info("Processing transporting task for: " + robot);
        // Lógica específica para transportar
    }
}
