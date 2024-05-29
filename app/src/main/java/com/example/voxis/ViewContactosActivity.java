package com.example.voxis;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ViewContactosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdaptadorContactos contactsAdapter;
    private List<Contactos> contactosList;
    private Spinner filtrarCategorias;
    private List<String> categoriasList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_contactos);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactosList = new ArrayList<>();
        categoriasList = new ArrayList<>();
        filtrarCategorias = findViewById(R.id.filtro_categoria);

        cargarCategorias();
        configurarSpinner();

        obtenerContactosBD("Todos");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.agregarContacto) {
            Intent intent = new Intent(ViewContactosActivity.this, ViewAgregarContactoActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.acerdaDe) {
            Intent intent = new Intent(ViewContactosActivity.this, ViewAcercaDeActivity.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.bluetooth) {
            Intent intent = new Intent(ViewContactosActivity.this, ViewBluetoothActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private void cargarCategorias() {
        AdminBD adminBD = new AdminBD(this);
        SQLiteDatabase db = adminBD.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT " + AdminBD.CATEGORIAS_CAMPO2 + " FROM " + AdminBD.NOMBRE_TABLA_CATEGORIAS, null);
            categoriasList.add("Todos");

            if (cursor.moveToFirst()) {
                do {
                    categoriasList.add(cursor.getString(cursor.getColumnIndexOrThrow(AdminBD.CATEGORIAS_CAMPO2)));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error al cargar categorías", Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }

    private void configurarSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoriasList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filtrarCategorias.setAdapter(adapter);

        filtrarCategorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String categoriaSeleccionada = categoriasList.get(position);
                obtenerContactosBD(categoriaSeleccionada);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada
            }
        });
    }

    private void obtenerContactosBD(String categoria) {
        AdminBD adminBD = new AdminBD(this);
        SQLiteDatabase db = adminBD.getReadableDatabase();
        Cursor cursor = null;
        contactosList.clear();

        try {
            String query;
            if (categoria.equals("Todos")) {
                query = "SELECT contactos.id, contactos.nombre, contactos.telefono, contactos.correo, categorias.nombre AS categoria FROM " + AdminBD.NOMBRE_TABLA_CONTACTOS +
                        " INNER JOIN " + AdminBD.NOMBRE_TABLA_CATEGORIAS + " ON " + AdminBD.NOMBRE_TABLA_CONTACTOS + "." + AdminBD.CONTACTOS_CAMPO5 + " = " + AdminBD.NOMBRE_TABLA_CATEGORIAS + "." + AdminBD.CATEGORIAS_CAMPO1;
            } else {
                query = "SELECT contactos.id, contactos.nombre, contactos.telefono, contactos.correo, categorias.nombre AS categoria FROM " + AdminBD.NOMBRE_TABLA_CONTACTOS +
                        " INNER JOIN " + AdminBD.NOMBRE_TABLA_CATEGORIAS + " ON " + AdminBD.NOMBRE_TABLA_CONTACTOS + "." + AdminBD.CONTACTOS_CAMPO5 + " = " + AdminBD.NOMBRE_TABLA_CATEGORIAS + "." + AdminBD.CATEGORIAS_CAMPO1 +
                        " WHERE categorias.nombre = ?";
            }
            cursor = db.rawQuery(query, categoria.equals("Todos") ? null : new String[]{categoria});

            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(AdminBD.CONTACTOS_CAMPO1));
                    String nombre = cursor.getString(cursor.getColumnIndexOrThrow(AdminBD.CONTACTOS_CAMPO2));
                    String telefono = cursor.getString(cursor.getColumnIndexOrThrow(AdminBD.CONTACTOS_CAMPO3));
                    String correo = cursor.getString(cursor.getColumnIndexOrThrow(AdminBD.CONTACTOS_CAMPO4));
                    String categoriaNombre = cursor.getString(cursor.getColumnIndexOrThrow("categoria"));

                    contactosList.add(new Contactos(id, nombre, "12:00 PM", R.drawable.perfil, telefono, correo, categoriaNombre));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error al obtener contactos desde la base de datos", Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        if (contactsAdapter == null) {
            contactsAdapter = new AdaptadorContactos(contactosList, this);
            recyclerView.setAdapter(contactsAdapter);
        } else {
            contactsAdapter.notifyDataSetChanged();
        }
    }
}
