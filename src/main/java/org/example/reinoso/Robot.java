package org.example.reinoso;

public class Robot {
    private final int id;

    public Robot(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Robot " + id;
    }
}