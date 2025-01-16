package org.example.reinoso;

public class Robot {
    private int id;
    private String category;


    public Robot() {}

    //constructor de la clase Robot, con los parametros id (int), para saber que robot es y diferenciarlo del resto y category (string) para saber su tarea encargada
    public Robot(int id, String category) {
        this.id = id;
        this.category = category;
    }

    //todos los getters y setters de los parametros de la clase Robot, aunque haya algunos que no se usan, al igual que el constructor vacio
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Robot " + id + " - Tarea: " + category;
    }
}