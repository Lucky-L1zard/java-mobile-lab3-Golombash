package com.example.pizzaapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PizzaAdapter extends RecyclerView.Adapter<PizzaAdapter.PizzaViewHolder> {

    private List<Pizza> pizzaList;
    private OnPizzaClickListener listener;

    public interface OnPizzaClickListener {
        void onEditClick(Pizza pizza);
        void onDeleteClick(Pizza pizza);
    }

    public PizzaAdapter(List<Pizza> pizzaList, OnPizzaClickListener listener) {
        this.pizzaList = pizzaList;
        this.listener = listener;
    }

    public void updateData(List<Pizza> newList) {
        this.pizzaList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PizzaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pizza, parent, false);
        return new PizzaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PizzaViewHolder holder, int position) {
        Pizza pizza = pizzaList.get(position);
        holder.textName.setText(pizza.getName());
        holder.textDetails.setText(pizza.getDescription() + " | " + pizza.getDiameter() + " см | " + pizza.getCalories() + " ккал");
        holder.textPrice.setText(String.format("%.2f грн", pizza.getPrice()));

        holder.btnEdit.setOnClickListener(v -> listener.onEditClick(pizza));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(pizza));
    }

    @Override
    public int getItemCount() {
        return pizzaList != null ? pizzaList.size() : 0;
    }

    static class PizzaViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textDetails, textPrice;
        ImageButton btnEdit, btnDelete;

        public PizzaViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textPizzaName);
            textDetails = itemView.findViewById(R.id.textPizzaDetails);
            textPrice = itemView.findViewById(R.id.textPizzaPrice);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}