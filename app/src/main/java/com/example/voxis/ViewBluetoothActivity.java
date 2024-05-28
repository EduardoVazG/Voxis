package com.example.voxis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ViewBluetoothActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SELECT_CONTACT = 1;
    private TextView nombreContacto;
    private TextView numeroContacto;
    private TextView horaContacto;
    private TextView correoContacto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_bluetooth);

        nombreContacto = findViewById(R.id.nombre_contacto);
        numeroContacto = findViewById(R.id.numero_contacto);
        horaContacto = findViewById(R.id.hora_contacto);
        correoContacto = findViewById(R.id.correo_contacto);
    }

    public void Compartir(View view) {
        // Implementar funcionalidad de compartir
    }

    public void Regresar(View view) {
        Intent intent = new Intent(ViewBluetoothActivity.this, ViewContactosActivity.class);
        startActivity(intent);
    }

    public void SeleccionarContacto(View view) {
        Intent intent = new Intent(ViewBluetoothActivity.this, ViewContactosActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SELECT_CONTACT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_CONTACT && resultCode == RESULT_OK) {
            if (data != null) {
                String nombre = data.getStringExtra("nombre");
                String numero = data.getStringExtra("numero");
                String hora = data.getStringExtra("hora");
                String correo = data.getStringExtra("correo");

                nombreContacto.setText(nombre);
                numeroContacto.setText(numero);
                horaContacto.setText(hora);
                correoContacto.setText(correo);
            }
        }
    }
}
