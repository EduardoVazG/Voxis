package com.example.voxis;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.View;
import android.widget.TextView;

public class ViewRegistroActivity extends AppCompatActivity {

    private EditText mNombre, mApellido, mUsuario, mCorreo, mContraseña;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.view_registro);

        mNombre = findViewById(R.id.nombre);
        mApellido = findViewById(R.id.apellido);
        mUsuario = findViewById(R.id.usuario);
        mCorreo = findViewById(R.id.email);
        mContraseña = findViewById(R.id.password);


    }

    public void Registro(View view) {



    }
}