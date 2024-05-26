package com.example.voxis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MostrarContactoActivity extends AppCompatActivity {
    private TextView nombreView;
    private TextView numeroView;
    private TextView correoView;
    private TextView categoriaView;
    private ImageView perfilView;

    // Variables para almacenar los datos del contacto
    private int id;
    private String nombre;
    private String numero;
    private String correo;
    private String categoria;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_contacto);

        nombreView = findViewById(R.id.nombre_detalle);
        numeroView = findViewById(R.id.numero_detalle);
        correoView = findViewById(R.id.correo_detalle);
        categoriaView = findViewById(R.id.categoria_detalle);
        perfilView = findViewById(R.id.perfil_detalle);

        // Obtener los datos del contacto de la actividad anterior
        id = getIntent().getIntExtra("id", -1);
        nombre = getIntent().getStringExtra("nombre");
        numero = getIntent().getStringExtra("numero");
        correo = getIntent().getStringExtra("correo");
        categoria = getIntent().getStringExtra("categoria");
        int perfilResId = getIntent().getIntExtra("perfilResId", R.drawable.perfil);

        // Mostrar los datos del contacto
        nombreView.setText(nombre);
        numeroView.setText(numero);
        correoView.setText(correo != null ? correo : "Correo no disponible");
        categoriaView.setText(categoria); // Añadido
        perfilView.setImageResource(perfilResId);
    }

    public void EditarContacto(View view) {
        Intent intent = new Intent(MostrarContactoActivity.this, EditarContactoActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("nombre", nombreView.getText().toString());
        intent.putExtra("numero", numeroView.getText().toString());
        intent.putExtra("correo", correoView.getText().toString());
        intent.putExtra("categoria", categoriaView.getText().toString()); // Añadido
        startActivityForResult(intent, 1); // Usar startActivityForResult para iniciar la actividad
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            // Actualizar los datos del contacto
            nombre = data.getStringExtra("nombre");
            numero = data.getStringExtra("numero");
            correo = data.getStringExtra("correo");
            categoria = data.getStringExtra("categoria");
            int perfilResId = data.getIntExtra("perfilResId", R.drawable.perfil);

            // Mostrar los datos del contacto actualizados
            nombreView.setText(nombre);
            numeroView.setText(numero);
            correoView.setText(correo != null ? correo : "Correo no disponible");
            categoriaView.setText(categoria); // Añadido
            perfilView.setImageResource(perfilResId);
        }
    }
}
