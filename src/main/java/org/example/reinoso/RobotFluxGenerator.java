package org.example.reinoso;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

//se crea un componente de Spring, el cual luego estará instanciado en la case principal para iniciar el flujo
@Component
public class RobotFluxGenerator {

    //se inyecta la dependencia de robotService, aqui se vuelve a usar autowired porque es un servicio (@service), pasa igual que con los componentes
    @Autowired
    private RobotService robotService;

    //se crea un array de strings con las tareas que puede realizar un robot
    private static final String[] TASK_CATEGORIES = {"cortar", "doblar", "apilar", "transportar"};

    //aqui se crea un booleano para comprobar que el flujo esta en funcionamiento, es de tipo volatil para que todos los robots vean inmediatamente si esa variable ha cambiado
    //en nuestro caso running cambia a false en el metodo stop, esto hace que el flujo se detenga, este metodo se llama en el caso 5 del menu, el de salir
    private volatile boolean running = true;

    //se crea un flujo de robots, el cual cada segundo aumenta su contador, asi cuando el contador sea divisible entre 5 (es decir que pasen 5 segundos)
    //genera un nuevo robot con una tarea aleatoria, este robot se envia al servicio de robotService, se le devuelve el contador (para el id del robot), y la caterogia de la tarea
    public Flux<Robot> generateRobotFlux() {
        AtomicInteger robotCounter = new AtomicInteger(1);

        return Flux.interval(Duration.ofSeconds(1))
                .filter(tick -> running && tick % 5 == 0)
                .map(tick -> {
                    String category = TASK_CATEGORIES[(int) (Math.random() * TASK_CATEGORIES.length)];
                    return new Robot(robotCounter.getAndIncrement(), category);
                })
                .doOnNext(robotService::sendRobot);
    }

    //este metodo cambia el estado de running a false, lo que hace que el flujo de robots se detenga
    public void stop() {
        running = false;
    }
}