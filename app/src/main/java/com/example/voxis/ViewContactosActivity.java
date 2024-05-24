package com.example.voxis;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ViewContactosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdaptadorContactos contactsAdapter;
    private List<Contactos> contactosList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_contactos);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        contactosList = new ArrayList<>();
        // Agrega contactos de ejemplo
        contactosList.add(new Contactos("Juan Perez", "12:34 PM", R.drawable.perfil, "9614738212"));
        contactosList.add(new Contactos("Maria Lopez", "11:22 AM", R.drawable.perfil, "9614738264"));
        contactosList.add(new Contactos("Carlos Garcia", "10:45 AM", R.drawable.perfil, "9611622985"));

        contactsAdapter = new AdaptadorContactos(contactosList);
        recyclerView.setAdapter(contactsAdapter);
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
}
