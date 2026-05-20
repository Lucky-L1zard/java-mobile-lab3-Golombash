package com.example.pizzaapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "pizzas")
public class Pizza {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String description;
    private double price;
    private double diameter;
    private int calories;

    public Pizza(String name, String description, double price, double diameter, int calories) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.diameter = diameter;
        this.calories = calories;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public double getDiameter() { return diameter; }
    public void setDiameter(double diameter) { this.diameter = diameter; }

    public int getCalories() { return calories; }
    public void setCalories(int calories) { this.calories = calories; }
}