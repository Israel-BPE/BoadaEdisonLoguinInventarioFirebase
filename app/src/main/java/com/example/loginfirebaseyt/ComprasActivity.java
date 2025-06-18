package com.example.loginfirebaseyt;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ComprasActivity extends AppCompatActivity {
    EditText etCodigo, etCantidad;
    TextView tvNombre, tvStock, tvPrecio, tvTotal;
    Button btnBuscar, btnComprar;
    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compras);

        etCodigo = findViewById(R.id.etCodigo);
        etCantidad = findViewById(R.id.etCantidad);
        tvNombre = findViewById(R.id.tvNombre);
        tvStock = findViewById(R.id.tvStock);
        tvPrecio = findViewById(R.id.tvPrecio);
        tvTotal = findViewById(R.id.tvTotal);
        btnBuscar = findViewById(R.id.btnBuscar);
        btnComprar = findViewById(R.id.btnComprar);

        db = FirebaseDatabase.getInstance().getReference("productos");

        btnBuscar.setOnClickListener(v -> {
            String codigo = etCodigo.getText().toString();
            db.child(codigo).get().addOnSuccessListener(snapshot -> {
                if (snapshot.exists()) {
                    tvNombre.setText(snapshot.child("nombre").getValue(String.class));
                    tvStock.setText(String.valueOf(snapshot.child("stock").getValue(Long.class)));
                    tvPrecio.setText(String.valueOf(snapshot.child("precioCosto").getValue(Double.class)));
                } else {
                    Toast.makeText(this, "Producto no encontrado", Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnComprar.setOnClickListener(v -> {
            String codigo = etCodigo.getText().toString();
            int cantidad = Integer.parseInt(etCantidad.getText().toString());
            db.child(codigo).get().addOnSuccessListener(snapshot -> {
                if (snapshot.exists()) {
                    long stockActual = snapshot.child("stock").getValue(Long.class);
                    double precio = snapshot.child("precioCosto").getValue(Double.class);
                    db.child(codigo).child("stock").setValue(stockActual + cantidad);
                    tvTotal.setText("Total a pagar: $" + (cantidad * precio));
                }
            });
        });
    }


}