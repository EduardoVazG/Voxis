package com.example.voxis;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ViewContactosActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSIONS = 100;
    private static final int PICK_FILE_REQUEST = 101;

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!checkPermissions()) {
                requestPermissions();
            } else {
                obtenerContactosBD("Todos");
            }
        } else {
            obtenerContactosBD("Todos");
        }

        handleIncomingIntent(getIntent());
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
                    obtenerContactosBD("Todos");
                } else {
                    Toast.makeText(this, "Se necesitan todos los permisos para que la app funcione correctamente", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIncomingIntent(intent);
    }

    private void handleIncomingIntent(Intent intent) {
        if (intent != null && Intent.ACTION_SEND.equals(intent.getAction())) {
            Uri fileUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
            if (fileUri != null) {
                try {
                    String path = getPathFromUri(fileUri);
                    if (path != null) {
                        processFile(path);
                    } else {
                        Toast.makeText(this, "No se pudo obtener la ruta del archivo", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("ViewContactosActivity", "Error al procesar el archivo", e);
                    Toast.makeText(this, "Error al procesar el archivo: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void openFileChooser(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/json"); // Asegurarse de que se selecciona solo archivos JSON
        startActivityForResult(intent, PICK_FILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                if (uri != null) {
                    String path = getPathFromUri(uri);
                    if (path != null) {
                        processFile(path);
                    } else {
                        Toast.makeText(this, "No se pudo obtener la ruta del archivo", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private String getPathFromUri(Uri uri) {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { MediaStore.Files.FileColumns.DATA };
            Cursor cursor = null;
            try {
                cursor = getContentResolver().query(uri, projection, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA);
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    private void processFile(String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                Toast.makeText(this, "El archivo no existe", Toast.LENGTH_SHORT).show();
                return;
            }
            FileInputStream fis = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            fis.close();

            String contactosJson = stringBuilder.toString();
            Log.d("ViewContactosActivity", "Contactos JSON: " + contactosJson);
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Contactos>>() {}.getType();
            List<Contactos> receivedContacts = gson.fromJson(contactosJson, listType);
            guardarContactosBD(receivedContacts);
            obtenerContactosBD("Todos");
        } catch (Exception e) {
            Log.e("ViewContactosActivity", "Error al procesar el archivo", e);
            Toast.makeText(this, "Error al procesar el archivo: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void guardarContactosBD(List<Contactos> contactos) {
        AdminBD adminBD = new AdminBD(this);
        SQLiteDatabase db = null;
        try {
            db = adminBD.getWritableDatabase();
            db.beginTransaction();
            for (Contactos contacto : contactos) {
                ContentValues values = new ContentValues();
                values.put(AdminBD.CONTACTOS_CAMPO2, contacto.getName());
                values.put(AdminBD.CONTACTOS_CAMPO3, contacto.getNumero_contacto());
                values.put(AdminBD.CONTACTOS_CAMPO4, contacto.getCorreo());
                values.put(AdminBD.CONTACTOS_CAMPO5, obtenerCategoriaId(contacto.getCategoria(), db));

                db.insertOrThrow(AdminBD.NOMBRE_TABLA_CONTACTOS, null, values);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("ViewContactosActivity", "Error al guardar contactos en la base de datos", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }

    private int obtenerCategoriaId(String categoria, SQLiteDatabase db) {
        Cursor cursor = null;
        int categoriaId = -1;

        try {
            cursor = db.rawQuery("SELECT " + AdminBD.CATEGORIAS_CAMPO1 + " FROM " + AdminBD.NOMBRE_TABLA_CATEGORIAS + " WHERE " + AdminBD.CATEGORIAS_CAMPO2 + " = ?", new String[]{categoria});
            if (cursor.moveToFirst()) {
                categoriaId = cursor.getInt(cursor.getColumnIndexOrThrow(AdminBD.CATEGORIAS_CAMPO1));
            }
        } catch (Exception e) {
            Log.e("ViewContactosActivity", "Error al obtener categoria id", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return categoriaId;
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
            Log.e("ViewContactosActivity", "Error al cargar categorías", e);
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
            Log.e("ViewContactosActivity", "Error al obtener contactos desde la base de datos", e);
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
}
