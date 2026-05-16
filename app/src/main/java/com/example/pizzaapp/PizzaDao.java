package com.example.pizzaapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PizzaDao {

    @Insert
    void insert(Pizza pizza);

    @Query("SELECT * FROM pizzas ORDER BY id DESC")
    List<Pizza> getAllPizzas();

    @Query("SELECT * FROM pizzas WHERE id = :id LIMIT 1")
    Pizza getPizzaById(int id);

    @Update
    void update(Pizza pizza);

    @Delete
    void delete(Pizza pizza);
}