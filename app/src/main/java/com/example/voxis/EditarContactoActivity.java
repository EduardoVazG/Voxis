package com.example.voxis;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class    EditarContactoActivity extends AppCompatActivity {

    private EditText nombreEdit;
    private EditText numeroEdit;
    private EditText correoEdit;
    private Spinner categoriaSpinner;
    private Button guardarButton;

    private Map<String, Integer> categoriasMap;
    private int contactoId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_contacto);

        nombreEdit = findViewById(R.id.edit_nombre);
        numeroEdit = findViewById(R.id.edit_numero);
        correoEdit = findViewById(R.id.edit_correo);
        categoriaSpinner = findViewById(R.id.spinner_categoria);
        guardarButton = findViewById(R.id.btn_guardar_contacto);

        contactoId = getIntent().getIntExtra("id", -1);
        String nombre = getIntent().getStringExtra("nombre");
        String numero = getIntent().getStringExtra("numero");
        String correo = getIntent().getStringExtra("correo");

        nombreEdit.setText(nombre);
        numeroEdit.setText(numero);
        correoEdit.setText(correo);

        cargarCategorias();

        // Asigna el listener del botón al método ActualizarContacto
        guardarButton.setOnClickListener(this::ActualizarContacto);
    }

    public void ActualizarContacto(View view) {
        String nuevoNombre = nombreEdit.getText().toString();
        String nuevoNumero = numeroEdit.getText().toString();
        String nuevoCorreo = correoEdit.getText().toString();
        String nuevaCategoria = categoriaSpinner.getSelectedItem().toString();

        Integer categoriaId = categoriasMap.get(nuevaCategoria);

        if (nuevoNombre.isEmpty() || nuevoNumero.isEmpty()) {
            Toast.makeText(this, "Nombre y número son campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        AdminBD adminBD = new AdminBD(this);
        SQLiteDatabase db = adminBD.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(AdminBD.CONTACTOS_CAMPO2, nuevoNombre);
        values.put(AdminBD.CONTACTOS_CAMPO3, nuevoNumero);
        values.put(AdminBD.CONTACTOS_CAMPO4, nuevoCorreo);
        values.put(AdminBD.CONTACTOS_CAMPO5, categoriaId);

        int filasAfectadas = db.update(AdminBD.NOMBRE_TABLA_CONTACTOS, values,
                AdminBD.CONTACTOS_CAMPO1 + "=?", new String[]{String.valueOf(contactoId)});

        if (filasAfectadas > 0) {
            Toast.makeText(this, "Contacto actualizado", Toast.LENGTH_SHORT).show();

            Intent resultIntent = new Intent();
            resultIntent.putExtra("id", contactoId);
            resultIntent.putExtra("nombre", nuevoNombre);
            resultIntent.putExtra("numero", nuevoNumero);
            resultIntent.putExtra("correo", nuevoCorreo);
            resultIntent.putExtra("categoria", nuevaCategoria);
            setResult(RESULT_OK, resultIntent);

            finish();
        } else {
            Toast.makeText(this, "Error al actualizar contacto", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    private void cargarCategorias() {
        categoriasMap = new HashMap<>();

        AdminBD adminBD = new AdminBD(this);
        SQLiteDatabase db = adminBD.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " + AdminBD.CATEGORIAS_CAMPO1 + ", " + AdminBD.CATEGORIAS_CAMPO2 + " FROM " + AdminBD.NOMBRE_TABLA_CATEGORIAS, null);
        List<String> categorias = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(AdminBD.CATEGORIAS_CAMPO1));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow(AdminBD.CATEGORIAS_CAMPO2));
                categorias.add(nombre);
                categoriasMap.put(nombre, id);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categorias);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoriaSpinner.setAdapter(adapter);
    }
}
