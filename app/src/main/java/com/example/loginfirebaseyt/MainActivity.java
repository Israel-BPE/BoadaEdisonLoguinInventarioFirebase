package com.example.loginfirebaseyt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    EditText email, password;
    Spinner spinner;
    TextView tvSeleccionSpinner; // Nuevo TextView para mostrar la selección
    Button login, btnRegistrar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar vistas
        email = findViewById(R.id.etCorreo);
        password = findViewById(R.id.etPassword);
        spinner = findViewById(R.id.spinner);
        tvSeleccionSpinner = findViewById(R.id.tvSeleccionSpinner); // Enlaza con el XML que te proporcioné
        login = findViewById(R.id.btnLogin);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        mAuth = FirebaseAuth.getInstance();

        // Configurar Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.opciones_login,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Listener para mostrar la selección del Spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String seleccion = parent.getItemAtPosition(position).toString();
                tvSeleccionSpinner.setText("Modo: " + seleccion); // Actualiza el TextView
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tvSeleccionSpinner.setText("Modo: No seleccionado");
            }
        });

        // Listener del botón Login (sin cambios)
        login.setOnClickListener(v -> {
            String correo = email.getText().toString();
            String pass = password.getText().toString();

            if (correo.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Campos vacíos. Rellene sus datos para poder ingresar", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(correo, pass).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    String opcion = spinner.getSelectedItem().toString();
                    switch (opcion) {
                        case "Persona":
                            startActivity(new Intent(this, PersonaActivity.class));
                            break;
                        case "Producto":
                            startActivity(new Intent(this, ProductoActivity.class));
                            break;
                        case "Inventario":
                            startActivity(new Intent(this, InventarioActivity.class));
                            break;
                    }
                } else {
                    Toast.makeText(this, "Login fallido. Verifique sus datos", Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Listener del botón Registrar (sin cambios)
        btnRegistrar.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }
}