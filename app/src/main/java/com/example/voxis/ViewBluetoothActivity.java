package com.example.voxis;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ViewBluetoothActivity extends AppCompatActivity {

    private ListView listViewContactos;
    private RecyclerView recyclerViewSeleccionados;
    private AdaptadorContactos selectedContactsAdapter;
    private List<Contactos> contactosList2;
    private List<Contactos> filteredContactosList;
    private List<Contactos> selectedContactosList;
    private ArrayAdapter<String> listViewAdapter;
    private List<String> contactosNamesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_bluetooth);

        listViewContactos = findViewById(R.id.list_view_contactos);
        recyclerViewSeleccionados = findViewById(R.id.recycler_view_seleccionados);

        contactosList2 = new ArrayList<>();
        filteredContactosList = new ArrayList<>();
        selectedContactosList = new ArrayList<>();
        contactosNamesList = new ArrayList<>();

        recyclerViewSeleccionados.setLayoutManager(new LinearLayoutManager(this));
        selectedContactsAdapter = new AdaptadorContactos(selectedContactosList, this, null);
        recyclerViewSeleccionados.setAdapter(selectedContactsAdapter);

        listViewAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contactosNamesList);
        listViewContactos.setAdapter(listViewAdapter);

        listViewContactos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contactos selectedContact = filteredContactosList.get(position);
                if (!selectedContactosList.contains(selectedContact)) {
                    selectedContactosList.add(selectedContact);
                    selectedContactsAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ViewBluetoothActivity.this, "El contacto ya está seleccionado", Toast.LENGTH_SHORT).show();
                }
            }
        });

        obtenerContactosBD();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_3, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterContactos(newText);
                return false;
            }
        });

        return true;
    }

    private void filterContactos(String query) {
        filteredContactosList.clear();
        contactosNamesList.clear();

        for (Contactos contacto : contactosList2) {
            if (contacto.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredContactosList.add(contacto);
                contactosNamesList.add(contacto.getName());
            }
        }
        listViewAdapter.notifyDataSetChanged();
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
                    contactosNamesList.add(nombre);
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

        filteredContactosList.addAll(contactosList2);
        listViewAdapter.notifyDataSetChanged();
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
