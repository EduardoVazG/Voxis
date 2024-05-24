package com.example.voxis;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextView textViewRegister;
    EditText ingresarUsuario;
    EditText ingresarContra;
    AdminBD adminBD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_login);

        textViewRegister = findViewById(R.id.registrarse);
        ingresarUsuario = findViewById(R.id.ingresarUsuario);
        ingresarContra = findViewById(R.id.ingresarContra);
        adminBD = new AdminBD(this);

        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewRegistroActivity.class);
                startActivity(intent);
            }
        });
    }

    public void validacion(View view) {
        String usuario = ingresarUsuario.getText().toString().trim();
        String contraseña = ingresarContra.getText().toString().trim();

        if (usuario.isEmpty() || contraseña.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = adminBD.getReadableDatabase();
        String[] columns = {AdminBD.CAMPO4, AdminBD.CAMPO6};
        String selection = AdminBD.CAMPO4 + " = ?";
        String[] selectionArgs = {usuario};
        Cursor cursor = db.query(AdminBD.NOMBRE_TABLA, columns, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            String dbUsuario = cursor.getString(0);
            String dbContraseña = cursor.getString(1);

            if (usuario.equals(dbUsuario) && contraseña.equals(dbContraseña)) {
                Intent intent = new Intent(MainActivity.this, ViewContactosActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
        db.close();
    }
}
