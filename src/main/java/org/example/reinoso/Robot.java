package org.example.reinoso;

public class Robot {
    private int id;
    private String category;

    // No-argument constructor
    public Robot() {}

    // Constructor with parameters
    public Robot(int id, String category) {
        this.id = id;
        this.category = category;
    }

    // Getters and setters
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
        return "Robot " + id + " - Task: " + category;
    }
}