package org.example.reinoso;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

//con la anotacion @Controller indicamos que esta clase es un controlador de Spring,
//esto significa que se encarga de manejar peticiones HTTP, en este caso es porque
//nosotros hemos creado un menu visual en el cual se comunica con el codigo mediante peticiones HTTP (usa @getMapping)
@Controller
public class MenuController {

    //inyectamos una dependencia de tipo RobotFluxGenerator, se le pone autowired porque es un componente
    @Autowired
    private RobotFluxGenerator robotFluxGenerator;

    //inyectamos una dependencia de tipo ApplicationContext, se usará posteriormente para cerrar la aplicacion
    @Autowired
    private ApplicationContext context;

    //creamos un objeto de tipo KafkaConsumer
    private KafkaConsumer<String, Robot> consumer;

    //en el constructor de la clase creamos un objeto de tipo Properties para almacenar la configuracion del consumidor que hemos creado antes
    //aqui hemos hecho con el la clase KafkaProducer añadiendo los parametros, pero en este caso no son ProducerConfig, sino ConsumerConfig
    //porque kafkaProducer es el Productor y menuController es el Consumidor
    //en este caso se le añaden diferentes parametro
    //BOOTSTRAP_SERVERS_CONFIG: al igual que en el productor
    //GROUP_ID_CONFIG: es el identificador del grupo al que pertenece el consumidor
    //KEY_DESERIALIZER_CLASS_CONFIG: al igual que en el productor
    //VALUE_DESERIALIZER_CLASS_CONFIG: al igual que en el productor
    //SPRING_JSON_VALUE_DEFAULT_TYPE: indica que se va a recibir un objeto de tipo Robot
    //AUTO_OFFSET_RESET_CONFIG: se usa para que se lean todos los mensajes desde el principio
    //retorna un objeto de tipo KafkaConsumer con la configuracion anterior
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

    //se indica que cada vez que se haga un get a la ruta /menu se ejecute este metodo que devuelve menu.html
    @GetMapping("/menu")
    public String showMenu() {
        return "menu";
    }

    //se indica que cada vez que se haga un post a la ruta /menu se ejecute este metodo +
    //en este menu se le pasa una opcion por parametro, esta opcion la recoge @requestParam
    //dependiendo de la opcion que se haya pasado se ejecutará un case u otro
    //esto devuelve una lista de robots dependiendo el caso introducido, aunque aqui salgan numeros, en la parte visual es con un menu desplazable
    //donde por ejemplo si seleccionas cortar, aqui se le manda la opcion 1, eso devuelve una lista de los robots encargados de cortar
    //asi con las 4 opciones
    //luego la opcion 6 es la de ver toda la lista de robots en directo
    //la opcion 5 es la que cierra la ejecucion del programam, aqui es donde se usa el contexto, pasandole la opcion -1 para indicar que se cierra el programa
    //tambien tenemeos mensajes  por si no hay robots de alguna cateogria y cosas asi
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
            case 6:
                robots = getAllRobots();
                break;
            case 5:
                robotFluxGenerator.stop();
                executeDockerComposeDown();
                response.put("message", "El contenedor de Docker ha sido eliminado y el programa se ha detenido.");
                new Thread(() -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    SpringApplication.exit(context, () -> -1);
                    System.exit(-1);
                }).start();
                return ResponseEntity.ok(response);
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

    //se crea un metodo que ejecuta el comando docker-compose down para cerrar los contenedores y asi eliminar el contenido de previas compilaciones
    private void executeDockerComposeDown() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("docker-compose", "down");

        try {
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("docker-compose down executed successfully.");
            } else {
                System.out.println("Failed to execute docker-compose down.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    //este metodo se encarga de obtener los robots de una categoria en concreto
    //en la parte visual cuando seleccionas un robot sale una lista de esa categoria con los nombres de los robots
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