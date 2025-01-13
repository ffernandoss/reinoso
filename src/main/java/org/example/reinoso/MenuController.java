package org.example.reinoso;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

@Controller
public class MenuController {

    @Autowired
    private RobotFluxGenerator robotFluxGenerator;

    private KafkaConsumer<String, String> consumer;

    public MenuController() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "menu-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        this.consumer = new KafkaConsumer<>(props);
    }

    @GetMapping("/menu")
    public String showMenu() {
        return "menu";
    }

    @PostMapping("/menu")
public String handleMenuOption(@RequestParam("option") int option, Model model) {
    List<String> robots;
    switch (option) {
        case 1:
            robots = getRobots("cortar");
            model.addAttribute("robots", robots);
            if (robots.isEmpty()) {
                model.addAttribute("noRobotsMessage", "No hay ningún robot registrado para cortar.");
            }
            break;
        case 2:
            robots = getRobots("doblar");
            model.addAttribute("robots", robots);
            if (robots.isEmpty()) {
                model.addAttribute("noRobotsMessage", "No hay ningún robot registrado para doblar.");
            }
            break;
        case 3:
            robots = getRobots("apilar");
            model.addAttribute("robots", robots);
            if (robots.isEmpty()) {
                model.addAttribute("noRobotsMessage", "No hay ningún robot registrado para apilar.");
            }
            break;
        case 4:
            robots = getRobots("transportar");
            model.addAttribute("robots", robots);
            if (robots.isEmpty()) {
                model.addAttribute("noRobotsMessage", "No hay ningún robot registrado para transportar.");
            }
            break;
        case 5:
            robotFluxGenerator.stop();
            SpringApplication.exit(SpringApplication.run(MenuController.class), () -> 0);
            System.exit(0);
            break;
        default:
            model.addAttribute("message", "Opción no válida.");
    }
    return "menu";
}

    private List<String> getRobots(String category) {
        consumer.subscribe(Collections.singletonList("robot-tasks-" + category));
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
        List<String> robots = new ArrayList<>();
        for (ConsumerRecord<String, String> record : records) {
            robots.add(record.value());
        }
        consumer.unsubscribe();
        return robots;
    }
}