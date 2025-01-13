package org.example.reinoso;

public class Robot {
    private final int id;
    private final String category;

    public Robot(int id, String category) {
        this.id = id;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "Robot " + id + " - Task: " + category;
    }
}
