package com.example.voxis;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
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

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_contactos);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        contactosList = new ArrayList<>();

        // Pedir permisos
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            // Si ya se tiene el permiso, leer contactos
            obtenerContactos();
        }
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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults); // Añade esta línea
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido
                obtenerContactos();
            } else {
                // Permiso denegado
                Toast.makeText(this, "Permiso denegado para leer contactos", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void obtenerContactos() {
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, null);

        if (cursor != null) {
            int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

            while (cursor.moveToNext()) {
                if (nameIndex != -1 && numberIndex != -1) {
                    String nombre = cursor.getString(nameIndex);
                    String numero = cursor.getString(numberIndex);
                    String hora = "12:00 PM"; // Aquí podrías poner la hora real si la tienes, de lo contrario, usa un valor por defecto

                    contactosList.add(new Contactos(nombre, hora, R.drawable.perfil, numero));
                }
            }
            cursor.close();
        }

        // Configurar el adaptador con los datos de contactos
        contactsAdapter = new AdaptadorContactos(contactosList);
        recyclerView.setAdapter(contactsAdapter);
    }
}
