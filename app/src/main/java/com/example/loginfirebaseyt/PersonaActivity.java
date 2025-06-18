package com.example.loginfirebaseyt;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class PersonaActivity extends AppCompatActivity {
    EditText cedula, nombre, provincia, correo;
    RadioGroup genero;
    Spinner pais;
    Button actualizar, btnRegresar;
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persona);

        // Enlazamos todos los componentes visuales
        cedula = findViewById(R.id.etCedula);
        nombre = findViewById(R.id.etNombre);
        provincia = findViewById(R.id.etProvincia);
        correo = findViewById(R.id.etCorreo); // Nuevo campo
        genero = findViewById(R.id.rgGenero);
        pais = findViewById(R.id.spinnerPais);
        actualizar = findViewById(R.id.btnActualizar);
        btnRegresar = findViewById(R.id.btnRegresar); // Nuevo botón

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("usuarios");

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.opciones_pais, android.R.layout.simple_spinner_item);
        pais.setAdapter(adapter);

        actualizar.setOnClickListener(v -> {
            String uid = mAuth.getCurrentUser().getUid();
            String ced = cedula.getText().toString();
            String nom = nombre.getText().toString();
            String prov = provincia.getText().toString();
            String email = correo.getText().toString(); // Obtener el correo ingresado
            String generoSeleccionado = ((RadioButton) findViewById(genero.getCheckedRadioButtonId())).getText().toString();
            String paisSeleccionado = pais.getSelectedItem().toString();

            if (ced.isEmpty() || nom.isEmpty() || prov.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            Map<String, Object> datos = new HashMap<>();
            datos.put("cedula", ced);
            datos.put("nombre", nom);
            datos.put("provincia", prov);
            datos.put("correo", email); // Agregamos el correo a Firebase
            datos.put("genero", generoSeleccionado);
            datos.put("pais", paisSeleccionado);

            mDatabase.child(uid).updateChildren(datos).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Datos actualizados", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Nuevo botón para regresar al MainActivity
        btnRegresar.setOnClickListener(v -> {
            startActivity(new Intent(PersonaActivity.this, MainActivity.class));
            finish(); // Cierra esta actividad
        });
    }
}
