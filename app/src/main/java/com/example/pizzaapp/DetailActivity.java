package com.example.pizzaapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    private EditText editName, editDescription, editPrice, editDiameter, editCalories;
    private AppDatabase db;
    private int pizzaId = -1; // нехай -1 означає режим створення, інакше — редагування (за id)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        db = AppDatabase.getInstance(this);

        editName = findViewById(R.id.editPizzaName);
        editDescription = findViewById(R.id.editPizzaDescription);
        editPrice = findViewById(R.id.editPizzaPrice);
        editDiameter = findViewById(R.id.editPizzaDiameter);
        editCalories = findViewById(R.id.editPizzaCalories);
        Button btnSave = findViewById(R.id.btnSavePizza);
        TextView textTitle = findViewById(R.id.textFormTitle);

        // Перевіряємо, чи передали ID для редагування
        if (getIntent().hasExtra("PIZZA_ID")) {
            pizzaId = getIntent().getIntExtra("PIZZA_ID", -1);
            textTitle.setText("Редагування піци");
            loadPizzaData(pizzaId);
        } else {
            textTitle.setText("Створення нової піци");
        }

        btnSave.setOnClickListener(v -> savePizza());
    }

    private void loadPizzaData(int id) {
        new Thread(() -> {
            Pizza pizza = db.pizzaDao().getPizzaById(id);
            if (pizza != null) {
                runOnUiThread(() -> {
                    editName.setText(pizza.getName());
                    editDescription.setText(pizza.getDescription());
                    editPrice.setText(String.valueOf(pizza.getPrice()));
                    editDiameter.setText(String.valueOf(pizza.getDiameter()));
                    editCalories.setText(String.valueOf(pizza.getCalories()));
                });
            }
        }).start();
    }

    private void savePizza() {
        // Захищений блок опрацювання даних користувача
        try {
            String name = editName.getText().toString().trim();
            String description = editDescription.getText().toString().trim();
            String priceStr = editPrice.getText().toString().trim();
            String diameterStr = editDiameter.getText().toString().trim();
            String caloriesStr = editCalories.getText().toString().trim();

            // Валідація на порожні поля
            if (name.isEmpty() || description.isEmpty() || priceStr.isEmpty() || diameterStr.isEmpty() || caloriesStr.isEmpty()) {
                throw new IllegalArgumentException("Усі поля повинні бути заповнені");
            }

            // Парсинг числових значень (може викликати NumberFormatException)
            double price = Double.parseDouble(priceStr);
            int diameter = Integer.parseInt(diameterStr);
            int calories = Integer.parseInt(caloriesStr);

            if (price <= 0 || diameter <= 0 || calories < 0) {
                throw new IllegalArgumentException("Введено некоректні числові значення");
            }

            // Збереження або оновлення в базі даних (у фоновому потоці)
            new Thread(() -> {
                if (pizzaId == -1) {
                    // Передаємо в конструктор параметри без ID
                    Pizza newPizza = new Pizza(name, description, price, diameter, calories);
                    db.pizzaDao().insert(newPizza);
                } else {
                    Pizza existingPizza = new Pizza(name, description, price, diameter, calories);
                    existingPizza.setId(pizzaId); // Призначаємо існуючий ID для оновлення
                    db.pizzaDao().update(existingPizza);
                }
                runOnUiThread(() -> {
                    Toast.makeText(DetailActivity.this, "Дані успішно збережено", Toast.LENGTH_SHORT).show();
                    finish(); // Повертаємось на попередній екран
                });
            }).start();

        } catch (NumberFormatException e) {
            // Перехоплення помилок невірного формату чисел
            Toast.makeText(this, "Помилка в опрацюванні запиту: перевірте формат чисел!", Toast.LENGTH_LONG).show();
        } catch (IllegalArgumentException e) {
            // Перехоплення помилок бізнес-валідації (порожні або від'ємні значення)
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            // Загальний перехоплювач для непередбачуваних критичних помилок
            Toast.makeText(this, "Помилка в опрацюванні запиту", Toast.LENGTH_LONG).show();
        }
    }
}