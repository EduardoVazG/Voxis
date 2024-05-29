package com.example.voxis;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ViewBluetoothActivity extends AppCompatActivity {

    private RecyclerView recyclerViewContactos;
    private RecyclerView recyclerViewSeleccionados;
    private AdaptadorContactos contactsAdapter;
    private AdaptadorContactos selectedContactsAdapter;
    private List<Contactos> contactosList2;
    private List<Contactos> selectedContactosList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_bluetooth);

        recyclerViewContactos = findViewById(R.id.recycler_view_contactos);
        recyclerViewSeleccionados = findViewById(R.id.recycler_view_seleccionados);
        contactosList2 = new ArrayList<>();
        selectedContactosList = new ArrayList<>();

        recyclerViewContactos.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSeleccionados.setLayoutManager(new LinearLayoutManager(this));

        contactsAdapter = new AdaptadorContactos(contactosList2, this, this::onContactSelected);
        selectedContactsAdapter = new AdaptadorContactos(selectedContactosList, this, null);

        recyclerViewContactos.setAdapter(contactsAdapter);
        recyclerViewSeleccionados.setAdapter(selectedContactsAdapter);

        obtenerContactosBD();
    }

    private void onContactSelected(Contactos contacto) {
        if (!selectedContactosList.contains(contacto)) {
            selectedContactosList.add(contacto);
            selectedContactsAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "El contacto ya está seleccionado", Toast.LENGTH_SHORT).show();
        }
    }

    private void obtenerContactosBD() {
        AdminBD adminBD = new AdminBD(this);
        SQLiteDatabase db = adminBD.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT contactos.id, contactos.nombre, contactos.telefono, contactos.correo, categorias.nombre AS categoria FROM " + AdminBD.NOMBRE_TABLA_CONTACTOS +
                    " INNER JOIN " + AdminBD.NOMBRE_TABLA_CATEGORIAS + " ON " + AdminBD.NOMBRE_TABLA_CONTACTOS + "." + AdminBD.CONTACTOS_CAMPO5 + " = " + AdminBD.NOMBRE_TABLA_CATEGORIAS + "." + AdminBD.CATEGORIAS_CAMPO1, null);

            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(AdminBD.CONTACTOS_CAMPO1));
                    String nombre = cursor.getString(cursor.getColumnIndexOrThrow(AdminBD.CONTACTOS_CAMPO2));
                    String telefono = cursor.getString(cursor.getColumnIndexOrThrow(AdminBD.CONTACTOS_CAMPO3));
                    String correo = cursor.getString(cursor.getColumnIndexOrThrow(AdminBD.CONTACTOS_CAMPO4));
                    String categoria = cursor.getString(cursor.getColumnIndexOrThrow("categoria"));

                    String hora = "12:00 PM"; // Hora por defecto

                    contactosList2.add(new Contactos(id, nombre, hora, R.drawable.perfil, telefono, correo, categoria));
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

        contactsAdapter.notifyDataSetChanged();
    }

    public void Compartir(View view) {
        // Implementar funcionalidad de compartir
    }

    public void Regresar(View view) {
        Intent intent = new Intent(ViewBluetoothActivity.this, ViewContactosActivity.class);
        startActivity(intent);
    }

    public void SeleccionarContacto(View view) {
        // Implementar la función de seleccionar un contacto
    }
}
