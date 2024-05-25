package com.example.voxis;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ViewAgregarContactoActivity extends AppCompatActivity {

    private EditText mNombre, mTelefono, mCorreo;
    private Spinner mGrupo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.view_agregar_contacto);

        mNombre = findViewById(R.id.etNombre);
        mTelefono = findViewById(R.id.etNumeroCelular);
        mCorreo = findViewById(R.id.etCorreo);
        mGrupo = findViewById(R.id.spinnerGrupo);

        cargarCategorias();
    }

    private void cargarCategorias() {
        AdminBD adminBD = new AdminBD(this);
        SQLiteDatabase db = adminBD.getReadableDatabase();
        Cursor cursor = null;
        ArrayList<String> categorias = new ArrayList<>();

        try {
            cursor = db.rawQuery("SELECT * FROM " + AdminBD.NOMBRE_TABLA_CATEGORIAS, null);
            if (cursor.moveToFirst()) {
                do {
                    String nombreCategoria = cursor.getString(cursor.getColumnIndexOrThrow(AdminBD.CATEGORIAS_CAMPO2));
                    Log.d("ViewAgregarContacto", "Categoria cargada: " + nombreCategoria); // Log cada categoría cargada
                    categorias.add(nombreCategoria);
                } while (cursor.moveToNext());
            } else {
                Log.d("ViewAgregarContacto", "No se encontraron categorías.");
            }
        } catch (Exception e) {
            Log.e("ViewAgregarContacto", "Error al cargar categorías", e);
            Toast.makeText(this, "Error al cargar categorías", Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        if (categorias.isEmpty()) {
            Log.d("ViewAgregarContacto", "La lista de categorías está vacía.");
        } else {
            Log.d("ViewAgregarContacto", "Categorías cargadas: " + categorias.size());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categorias);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGrupo.setAdapter(adapter);
    }

    public void Cancelar(View view) {
        Intent intent = new Intent(ViewAgregarContactoActivity.this, ViewContactosActivity.class);
        startActivity(intent);
    }

    public void fn_agregar(View view) {
        AdminBD adminBD = new AdminBD(this);
        SQLiteDatabase db = adminBD.getWritableDatabase();

        String nombre = mNombre.getText().toString();
        String telefono = mTelefono.getText().toString();
        String correo = mCorreo.getText().toString();
        String grupo = mGrupo.getSelectedItem().toString();

        if (!nombre.isEmpty() && !telefono.isEmpty() && !correo.isEmpty() && !grupo.isEmpty()) {
            // Obtener el ID de la categoría seleccionada
            Cursor cursor = null;
            int categoriaId = -1;
            try {
                cursor = db.rawQuery("SELECT " + AdminBD.CATEGORIAS_CAMPO1 + " FROM " + AdminBD.NOMBRE_TABLA_CATEGORIAS + " WHERE " + AdminBD.CATEGORIAS_CAMPO2 + " = ?", new String[]{grupo});
                if (cursor.moveToFirst()) {
                    categoriaId = cursor.getInt(cursor.getColumnIndexOrThrow(AdminBD.CATEGORIAS_CAMPO1));
                }
            } catch (Exception e) {
                Log.e("ViewAgregarContacto", "Error al obtener ID de categoría", e);
                Toast.makeText(this, "Error al obtener categoría", Toast.LENGTH_SHORT).show();
                return;
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

            ContentValues valores = new ContentValues();
            valores.put(AdminBD.CONTACTOS_CAMPO2, nombre);
            valores.put(AdminBD.CONTACTOS_CAMPO3, telefono);
            valores.put(AdminBD.CONTACTOS_CAMPO4, correo);
            valores.put(AdminBD.CONTACTOS_CAMPO5, categoriaId);

            long resultado = db.insert(AdminBD.NOMBRE_TABLA_CONTACTOS, null, valores);

            if (resultado == -1) {
                Toast.makeText(this, "Error al agregar contacto", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Contacto agregado correctamente", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ViewAgregarContactoActivity.this, ViewContactosActivity.class);
                startActivity(intent);
            }
        } else {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }
}
