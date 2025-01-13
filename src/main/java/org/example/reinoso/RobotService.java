package org.example.reinoso;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class RobotService {

    @Autowired
    private KafkaTemplate<String, Robot> kafkaTemplate;

    public void sendRobot(Robot robot) {
        String topic = "robot-tasks-" + robot.getCategory();
        kafkaTemplate.send(topic, robot);
    }
}
