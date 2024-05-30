package com.example.voxis;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ViewBluetoothActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSIONS = 100;

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

        listViewContactos.setOnItemClickListener((parent, view, position, id) -> {
            Contactos selectedContact = filteredContactosList.get(position);
            if (!selectedContactosList.contains(selectedContact)) {
                selectedContactosList.add(selectedContact);
                selectedContactsAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(ViewBluetoothActivity.this, "El contacto ya está seleccionado", Toast.LENGTH_SHORT).show();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!checkPermissions()) {
                requestPermissions();
            }
        }

        obtenerContactosBD();
    }

    private boolean checkPermissions() {
        int permission1 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED &&
                permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_PERMISSIONS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length > 0) {
                boolean allPermissionsGranted = true;
                for (int result : grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        allPermissionsGranted = false;
                        break;
                    }
                }
                if (allPermissionsGranted) {
                    Toast.makeText(this, "Permisos concedidos", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Se necesitan todos los permisos para que la app funcione correctamente", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_3, menu);

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
        if (selectedContactosList.isEmpty()) {
            Toast.makeText(this, "No hay contactos seleccionados para compartir", Toast.LENGTH_SHORT).show();
            return;
        }

        Gson gson = new Gson();
        String contactosJson = gson.toJson(selectedContactosList);

        try {
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "contactos.json");
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(contactosJson.getBytes());
            fos.close();

            Uri uri = FileProvider.getUriForFile(this, "com.example.voxis.provider", file);  // Cambia esto a tu nombre de paquete

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
            sendIntent.setType("application/json");

            // Restrict to Bluetooth only
            sendIntent.setPackage("com.android.bluetooth");

            startActivity(Intent.createChooser(sendIntent, "Compartir contactos via Bluetooth"));
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al crear el archivo JSON", Toast.LENGTH_SHORT).show();
        }
    }

    public void Regresar(View view) {
        Intent intent = new Intent(ViewBluetoothActivity.this, ViewContactosActivity.class);
        startActivity(intent);
    }
}
