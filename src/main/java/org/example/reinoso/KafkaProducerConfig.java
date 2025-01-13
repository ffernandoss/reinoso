package org.example.reinoso;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import java.util.HashMap;
import java.util.Map;

//este @ indica que es una clase de configuracion, la cual luego
//contendrá diferentes @, en este caso es beans, esto lo que hace es que se puedan crear objetos para ser luego manejados por spring
@Configuration

//esta clase se encarga de almacenar todos los beans que luego serán utilizados por spring
public class KafkaProducerConfig {

    //el bean de Producerfactory devuelve un objeto de tipo ProducerFactory, este recibe dos parametros, el string y el objeto de tipo robot

    @Bean
    public ProducerFactory<String, Robot> robotProducerFactory() {

        //se crea un Hashmap donde se van a añadir tres campos
        //bootstrap_servers_config: se enccarga de indicar donde se encuentra el servidor de kafkaç
        //key_serializer_class_config: se encarga de serializar la clave para que kafka pueda entenderla, en nuestro caso la clave es el String, y lo pasa a string serializado (bytes)
        //value_serializer_class_config: se encarga de serializar el valor para que kafka pueda entenderla, en nuestro caso el valor es el objeto de tipo robot, y lo pasa a JSON serializado (bytes)
        //finalmente devuelve un objeto de tipo DefaultKafkaProducerFactory con los parametros del hashmap
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    //se crea otro bean pero en este caso de tipo KafkaTemplate, el cual recibe un string y un objeto de tipo robot
    //kafkaTemplate se usa para poder comunicarnos de forma sencilla con Apache Kafka
    //en este caso lo que se enviará en los dos mensajes es una clave (String) y un valor (Robot)
    //se devuelve un KafkaTemplate con la configuracion de robotProducerFactory
    @Bean
    public KafkaTemplate<String, Robot> robotKafkaTemplate() {
        return new KafkaTemplate<>(robotProducerFactory());
    }

    //creamos un tercer bean ingual que el primero pero en este caso el valor tambien es un string
    //esto se hace por si tenemos que enviar mensajes donde ambos campos sean de tipo string
    //en cuanto a los parametros de configuracion son los mismos que en el primer bean, pero en este caso VALUE_SERIALIZER_CLASS_CONFIG obviamente será de tipo StringSerializable
    @Bean
    public ProducerFactory<String, String> stringProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    //se crea otro bean igual que el segundo pero en este caso el valor es un string, por la misma razon que el bean anterior,
    //en este caso se devuelve un KafkaTemplate con la configuracion de stringProducerFactory
    @Bean
    public KafkaTemplate<String, String> stringKafkaTemplate() {
        return new KafkaTemplate<>(stringProducerFactory());
    }
}