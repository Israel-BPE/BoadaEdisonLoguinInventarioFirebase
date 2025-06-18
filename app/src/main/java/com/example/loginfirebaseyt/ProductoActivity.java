package com.example.loginfirebaseyt;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ProductoActivity extends AppCompatActivity {
    EditText codigo, nombre, stock, precioCosto, precioVenta;
    Button guardar, buscar, actualizar, borrar, btnRegresar;
    DatabaseReference productosDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);

        // Inicializar vistas
        codigo = findViewById(R.id.etCodigo);
        nombre = findViewById(R.id.etNombre);
        stock = findViewById(R.id.etStock);
        precioCosto = findViewById(R.id.etPrecioCosto);
        precioVenta = findViewById(R.id.etPrecioVenta);
        guardar = findViewById(R.id.btnGuardar);
        buscar = findViewById(R.id.btnBuscar);
        actualizar = findViewById(R.id.btnActualizar);
        borrar = findViewById(R.id.btnBorrar);
        btnRegresar = findViewById(R.id.btnRegresar);

        // Inicializar Firebase
        productosDB = FirebaseDatabase.getInstance().getReference("productos");

        // Configurar listeners
        guardar.setOnClickListener(v -> guardarProducto());
        buscar.setOnClickListener(v -> buscarProducto());
        actualizar.setOnClickListener(v -> actualizarProducto());
        borrar.setOnClickListener(v -> borrarProducto());
        btnRegresar.setOnClickListener(v -> {
            startActivity(new Intent(ProductoActivity.this, MainActivity.class));
            finish();
        });
    }

    private boolean validarCampos() {
        if (codigo.getText().toString().isEmpty()) {
            codigo.setError("Ingrese un código");
            return false;
        }
        if (nombre.getText().toString().isEmpty()) {
            nombre.setError("Ingrese un nombre");
            return false;
        }
        try {
            Integer.parseInt(stock.getText().toString());
        } catch (NumberFormatException e) {
            stock.setError("Stock debe ser número entero");
            return false;
        }
        try {
            Double.parseDouble(precioCosto.getText().toString());
            Double.parseDouble(precioVenta.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Precios deben ser números válidos", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void guardarProducto() {
        if (!validarCampos()) return;

        String cod = codigo.getText().toString();
        Map<String, Object> producto = new HashMap<>();
        producto.put("codigo", cod);
        producto.put("nombre", nombre.getText().toString());
        producto.put("stock", Integer.parseInt(stock.getText().toString()));
        producto.put("precioCosto", Double.parseDouble(precioCosto.getText().toString()));
        producto.put("precioVenta", Double.parseDouble(precioVenta.getText().toString()));

        productosDB.child(cod).setValue(producto)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Guardado con éxito", Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error al guardar: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void buscarProducto() {
        String cod = codigo.getText().toString();
        if (cod.isEmpty()) {
            Toast.makeText(this, "Ingrese un código para buscar", Toast.LENGTH_SHORT).show();
            return;
        }

        productosDB.child(cod).get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                nombre.setText(snapshot.child("nombre").getValue(String.class) != null ?
                        snapshot.child("nombre").getValue(String.class) : "");

                stock.setText(snapshot.child("stock").getValue(Long.class) != null ?
                        String.valueOf(snapshot.child("stock").getValue(Long.class)) : "0");

                precioCosto.setText(snapshot.child("precioCosto").getValue(Double.class) != null ?
                        String.format("%.2f", snapshot.child("precioCosto").getValue(Double.class)) : "0.00");

                precioVenta.setText(snapshot.child("precioVenta").getValue(Double.class) != null ?
                        String.format("%.2f", snapshot.child("precioVenta").getValue(Double.class)) : "0.00");
            } else {
                Toast.makeText(this, "Producto no encontrado", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Error al buscar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void actualizarProducto() {
        if (!validarCampos()) return;

        String cod = codigo.getText().toString();
        if (cod.isEmpty()) {
            Toast.makeText(this, "Primero busque un producto para actualizar", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar si el producto existe antes de actualizar
        productosDB.child(cod).get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                Map<String, Object> producto = new HashMap<>();
                producto.put("codigo", cod);
                producto.put("nombre", nombre.getText().toString());
                producto.put("stock", Integer.parseInt(stock.getText().toString()));
                producto.put("precioCosto", Double.parseDouble(precioCosto.getText().toString()));
                producto.put("precioVenta", Double.parseDouble(precioVenta.getText().toString()));

                productosDB.child(cod).setValue(producto)
                        .addOnSuccessListener(aVoid -> Toast.makeText(this, "Actualizado con éxito", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show());
            } else {
                Toast.makeText(this, "Producto no existe. Use Guardar en lugar de Actualizar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void borrarProducto() {
        String cod = codigo.getText().toString();
        if (cod.isEmpty()) {
            Toast.makeText(this, "Ingrese un código para eliminar", Toast.LENGTH_SHORT).show();
            return;
        }

        productosDB.child(cod).removeValue()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Producto eliminado", Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show());
    }

    private void limpiarCampos() {
        codigo.setText("");
        nombre.setText("");
        stock.setText("");
        precioCosto.setText("");
        precioVenta.setText("");
        codigo.requestFocus();
    }
}