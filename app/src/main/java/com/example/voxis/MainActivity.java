package com.example.voxis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextView textViewRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.view_login);

        textViewRegister = findViewById(R.id.registrarse);

        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewRegistroActivity.class);
                startActivity(intent);
            }
        });
    }

    public void validacion(View view) {
        Intent intent = new Intent(MainActivity.this, ViewContactosActivity.class);
        startActivity(intent);
    }
}
