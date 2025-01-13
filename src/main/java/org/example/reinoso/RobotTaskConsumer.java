package org.example.reinoso;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class RobotTaskConsumer {

    private static final Logger logger = LoggerFactory.getLogger(RobotTaskConsumer.class);

    @KafkaListener(topics = "robot-tasks-cortar", groupId = "robot-group")
    public void processCortarTask(Robot robot) {
        logger.info("Procesando tarea de cortar para: " + robot);
        // Lógica específica para cortar
    }

    @KafkaListener(topics = "robot-tasks-doblar", groupId = "robot-group")
    public void processDoblarTask(Robot robot) {
        logger.info("Procesando tarea de doblar para: " + robot);
        // Lógica específica para doblar
    }

    @KafkaListener(topics = "robot-tasks-apilar", groupId = "robot-group")
    public void processApilarTask(Robot robot) {
        logger.info("Procesando tarea de apilar para: " + robot);
        // Lógica específica para apilar
    }

    @KafkaListener(topics = "robot-tasks-transportar", groupId = "robot-group")
    public void processTransportarTask(Robot robot) {
        logger.info("Procesando tarea de transportar para: " + robot);
        // Lógica específica para transportar
    }
}