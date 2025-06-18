package com.example.loginfirebaseyt;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ListaProductosActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseRecyclerAdapter<Product, ProductViewHolder> adapter;
    DatabaseReference productosDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_productos);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productosDB = FirebaseDatabase.getInstance().getReference("productos");

        FirebaseRecyclerOptions<Product> options = new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(productosDB, Product.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Product, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(ProductViewHolder holder, int position, Product model) {
                holder.codigo.setText("Código: " + (model.codigo != null ? model.codigo : "N/A"));
                holder.nombre.setText("Nombre: " + model.nombre);
                holder.stock.setText("Stock: " + model.stock);
                holder.precio.setText("Precio: $" + model.precioVenta);
            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto, parent, false);
                return new ProductViewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView codigo, nombre, stock, precio;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            codigo = itemView.findViewById(R.id.tvCodigo);
            nombre = itemView.findViewById(R.id.tvNombre);
            stock = itemView.findViewById(R.id.tvStock);
            precio = itemView.findViewById(R.id.tvPrecio);
        }
    }

    public static class Product {
        public String codigo;
        public String nombre;
        public long stock;
        public double precioVenta;

        public Product() {}
    }
}