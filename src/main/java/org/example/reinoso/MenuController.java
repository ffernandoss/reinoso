package org.example.reinoso;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Controller
public class MenuController {

    @Autowired
    private RobotFluxGenerator robotFluxGenerator;

    private KafkaConsumer<String, Robot> consumer;

    public MenuController() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "menu-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.springframework.kafka.support.serializer.JsonDeserializer");
        props.put("spring.json.value.default.type", Robot.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        this.consumer = new KafkaConsumer<>(props);
    }

    @GetMapping("/menu")
    public String showMenu() {
        return "menu";
    }

    @PostMapping("/menu")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleMenuOption(@RequestParam("option") int option) {
        List<Robot> robots = new ArrayList<>();
        String category = "";
        Map<String, Object> response = new HashMap<>();
        switch (option) {
            case 1:
                category = "cortar";
                robots = getRobots(category);
                break;
            case 2:
                category = "doblar";
                robots = getRobots(category);
                break;
            case 3:
                category = "apilar";
                robots = getRobots(category);
                break;
            case 4:
                category = "transportar";
                robots = getRobots(category);
                break;
            case 5:
                robotFluxGenerator.stop();
                SpringApplication.exit(SpringApplication.run(MenuController.class), () -> 0);
                System.exit(0);
                return null;
            case 6:
                robots = getAllRobots();
                break;
            default:
                response.put("message", "Opción no válida.");
                return ResponseEntity.badRequest().body(response);
        }
        if (robots.isEmpty()) {
            response.put("noRobotsMessage", "No hay ningún robot registrado para " + category + ".");
        } else {
            response.put("robots", robots);
            response.put("category", category);
        }
        return ResponseEntity.ok(response);
    }

    private List<Robot> getRobots(String category) {
        consumer.subscribe(Collections.singletonList("robot-tasks-" + category));
        ConsumerRecords<String, Robot> records = consumer.poll(Duration.ofSeconds(1));
        List<Robot> robots = new ArrayList<>();
        for (ConsumerRecord<String, Robot> record : records) {
            robots.add(record.value());
        }
        consumer.unsubscribe();
        return robots;
    }

    private List<Robot> getAllRobots() {
        List<Robot> allRobots = new ArrayList<>();
        for (String category : new String[]{"cortar", "doblar", "apilar", "transportar"}) {
            allRobots.addAll(getRobots(category));
        }
        return allRobots;
    }
}