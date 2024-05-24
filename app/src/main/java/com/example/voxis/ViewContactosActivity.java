package com.example.voxis;

import android.os.Bundle;

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
        contactosList.add(new Contactos("Juan Perez", "12:34 PM", R.drawable.perfil));
        contactosList.add(new Contactos("Maria Lopez", "11:22 AM", R.drawable.perfil));
        contactosList.add(new Contactos("Carlos Garcia", "10:45 AM", R.drawable.perfil));

        contactsAdapter = new AdaptadorContactos(contactosList);
        recyclerView.setAdapter(contactsAdapter);
    }
}
