package org.example.reinoso;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.Scanner;

@Component
public class Menu implements Runnable {

    private KafkaConsumer<String, String> consumer;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private RobotFluxGenerator robotFluxGenerator;

    public Menu() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "menu-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        this.consumer = new KafkaConsumer<>(props);
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Menu:");
            System.out.println("1. Cortar");
            System.out.println("2. Doblar");
            System.out.println("3. Apilar");
            System.out.println("4. Transportar");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");
            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    displayRobots("cortar");
                    break;
                case 2:
                    displayRobots("doblar");
                    break;
                case 3:
                    displayRobots("apilar");
                    break;
                case 4:
                    displayRobots("transportar");
                    break;
                case 5:
                    System.out.println("Saliendo...");
                    robotFluxGenerator.stop();
                    executeDockerComposeDown();
                    consumer.close();
                    SpringApplication.exit(context, () -> 0);
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    private void displayRobots(String category) {
        consumer.subscribe(Collections.singletonList("robot-tasks-" + category));
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
        if (records.isEmpty()) {
            System.out.println("No hay robots para la tarea de " + category);
        } else {
            for (ConsumerRecord<String, String> record : records) {
                System.out.println(record.value());
            }
        }
        consumer.unsubscribe();
    }

    private void executeDockerComposeDown() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("docker-compose", "down");

        try {
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("docker-compose down ha sido ejecutado correctamente");
            } else {
                System.out.println("error al ejecutar docker-compose down");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}