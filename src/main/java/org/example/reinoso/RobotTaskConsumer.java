package org.example.reinoso;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class RobotTaskConsumer {

    //creamos un objeto de tipo Logger para poder imprimir luego los mensajes por terminal y que se vean de donde provienen
    private static final Logger logger = LoggerFactory.getLogger(RobotTaskConsumer.class);

    //aqui lo que hemos hecho ha sido crear 4 diferentes listeners, cada uno tiene una diferente tarea
    //robot-tasks-cortar
    //robot-tasks-doblar
    //robot-tasks-apilar
    //robot-tasks-transportar
    //por eso se hace en la clase service lo de poner robot-tasks delante, para que eso junto con la categoria random coincida con 1 de los 4 listeners
    // cada listener necesita recibir un topic y un grupo, en este caso el grupo es robot-group, al que todos pertenecen al mismo, el topico es la tarea
    //en cada listener se imprime un mensaje con la tarea que se esta realizando
    //esto hace que si el flujo crea por ejemplo uno de cortar, que se lleve al listener de robot-tasks-cortar, para que luego se imprima la tarea que está realizando

    @KafkaListener(topics = "robot-tasks-cortar", groupId = "robot-group")
    public void processCortarTask(Robot robot) {
        logger.info("Procesando tarea de cortar para: " + robot);

    }

    @KafkaListener(topics = "robot-tasks-doblar", groupId = "robot-group")
    public void processDoblarTask(Robot robot) {
        logger.info("Procesando tarea de doblar para: " + robot);

    }

    @KafkaListener(topics = "robot-tasks-apilar", groupId = "robot-group")
    public void processApilarTask(Robot robot) {
        logger.info("Procesando tarea de apilar para: " + robot);

    }

    @KafkaListener(topics = "robot-tasks-transportar", groupId = "robot-group")
    public void processTransportarTask(Robot robot) {
        logger.info("Procesando tarea de transportar para: " + robot);

    }
}