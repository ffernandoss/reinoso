package org.example.reinoso;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//el @SpringBootApplication indica que el proyecto es de tipo Spring Boot
@SpringBootApplication
public class ReinosoApplication implements CommandLineRunner {

    //creamos un objeto de tipo Logger para poder imprimir luego los mensajes por terminal y que se vean de donde provienen
    private static final Logger logger = LoggerFactory.getLogger(ReinosoApplication.class);

    //usamos @autowired para inyectar la dependencia de robotFluxGenerator, usamos esto porque robotFluxGenerator es un componente de Spring
    @Autowired
    private RobotFluxGenerator robotFluxGenerator;

    //hacemos lo mismo con Menu porque es otro componente
    @Autowired
    private Menu menu;

    public static void main(String[] args) {
        SpringApplication.run(ReinosoApplication.class, args);
    }

    //aqui inicializamos el flujo de robots, donde suscribimos cada robot generado al flujo de robots
    @Override
    public void run(String... args) {
        new Thread(menu).start();
        robotFluxGenerator.generateRobotFlux()
                .subscribe(robot -> logger.info("Se ha creado: " + robot));
    }
}