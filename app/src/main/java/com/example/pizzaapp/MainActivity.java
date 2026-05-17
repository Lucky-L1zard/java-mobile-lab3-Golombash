package com.example.pizzaapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PizzaAdapter.OnPizzaClickListener {

    private RecyclerView recyclerView;
    private PizzaAdapter adapter;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDatabase.getInstance(this);

        recyclerView = findViewById(R.id.recyclerViewPizzas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Ініціалізуємо адаптер порожнім списком
        adapter = new PizzaAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fabAdd = findViewById(R.id.fabAddPizza);
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPizzas(); // Оновлюємо список щоразу, коли повертаємось на це вікно
    }

    private void loadPizzas() {
        new Thread(() -> {
            try {
                List<Pizza> pizzas = db.pizzaDao().getAllPizzas();
                runOnUiThread(() -> adapter.updateData(pizzas));
            } catch (Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(MainActivity.this, "Помилка в опрацюванні запиту при завантаженні даних", Toast.LENGTH_LONG).show()
                );
            }
        }).start();
    }

    @Override
    public void onEditClick(Pizza pizza) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("PIZZA_ID", pizza.getId());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Pizza pizza) {
        new Thread(() -> {
            try {
                db.pizzaDao().delete(pizza);
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, "Піцу видалено", Toast.LENGTH_SHORT).show();
                    loadPizzas(); // Перезавантажуємо список після видалення
                });
            } catch (Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(MainActivity.this, "Помилка в опрацюванні запиту при видаленні", Toast.LENGTH_LONG).show()
                );
            }
        }).start();
    }
}