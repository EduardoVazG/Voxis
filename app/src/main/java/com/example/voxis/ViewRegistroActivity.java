package com.example.voxis;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ViewRegistroActivity extends AppCompatActivity {

    private EditText mNombre, mApellido, mUsuario, mCorreo, mContraseña;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.view_registro);

        mNombre = findViewById(R.id.nombre);
        mApellido = findViewById(R.id.apellido);
        mUsuario = findViewById(R.id.usuario);
        mCorreo = findViewById(R.id.email);
        mContraseña = findViewById(R.id.password);
    }

    public void Registro(View view) {
        Intent intent = new Intent(ViewRegistroActivity.this, MainActivity.class);
        AdminBD adminBDEventos = new AdminBD(this);
        SQLiteDatabase bDatos = adminBDEventos.getWritableDatabase();

        // Obtener los valores de los EditText
        String nombre = mNombre.getText().toString();
        String apellido = mApellido.getText().toString();
        String usuario = mUsuario.getText().toString();
        String correo = mCorreo.getText().toString();
        String contraseña = mContraseña.getText().toString();

        // Validar que todos los campos estén completos
        if (!nombre.isEmpty() && !apellido.isEmpty() && !usuario.isEmpty() && !correo.isEmpty() && !contraseña.isEmpty()) {
            // Crear un objeto ContentValues y agregar los valores
            ContentValues valores = new ContentValues();
            valores.put(AdminBD.CAMPO2, nombre);
            valores.put(AdminBD.CAMPO3, apellido);
            valores.put(AdminBD.CAMPO4, usuario);
            valores.put(AdminBD.CAMPO5, correo);
            valores.put(AdminBD.CAMPO6, contraseña);

            // Insertar los valores en la tabla "registro"
            long resultado = bDatos.insert(AdminBD.NOMBRE_TABLA, null, valores);

            if (resultado == -1) {
                // Error al insertar los datos
                Toast.makeText(this, "Error al registrar usuario", Toast.LENGTH_SHORT).show();
            } else {
                // Registro exitoso
                Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        } else {
            // Mostrar mensaje si algún campo está vacío
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
        }
        bDatos.close();
    }
}
