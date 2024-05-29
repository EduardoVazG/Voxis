package com.example.voxis;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ViewContactosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdaptadorContactos contactsAdapter;
    private List<Contactos> contactosList;

    private static final int REQUEST_CODE_EDIT_CONTACT = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_contactos);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactosList = new ArrayList<>();

        obtenerContactosBD();
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
        } else if (id == R.id.bluetooth) {
            Intent intent = new Intent(ViewContactosActivity.this, ViewBluetoothActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
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

                    contactosList.add(new Contactos(id, nombre, hora, R.drawable.perfil, telefono, correo, categoria));
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
            contactsAdapter = new AdaptadorContactos(contactosList, this, null);
            recyclerView.setAdapter(contactsAdapter);
        } else {
            contactsAdapter.notifyDataSetChanged();
        }
    }
}
