package org.example.reinoso;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

//indicamos que esta clase es un servicio de Spring
//el servicio sirve para que spring sepa que aqui sentro se encuentra la logica de la aplicacion, para que puedan ser inyectados en otros sitios del codigo
@Service
public class RobotService {

    //se inyecta un objeto de tipo KafkaTemplate,con clave de tipo string y valor de tipo Robot, este objeto es el encargado de comunicarse con el Kafka
    @Autowired
    private KafkaTemplate<String, Robot> kafkaTemplate;

    //este metodo se usa en robotFluxGenerator, donde se le envia un robot y este lo envia a Kafka, ademas envia la categoria obtenida en el flujo
    //se le pone delante de la categoria lo de robot-tasks- para que luego en la clase del consumidor coincida con los listeners creados
    public void sendRobot(Robot robot) {
        String topic = "robot-tasks-" + robot.getCategory();
        kafkaTemplate.send(topic, robot);
    }
}