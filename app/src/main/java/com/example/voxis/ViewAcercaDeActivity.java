package com.example.voxis;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ViewFlipper;


import androidx.appcompat.app.AppCompatActivity;

public class ViewAcercaDeActivity extends AppCompatActivity {

    private ViewFlipper viewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_acerda_de);

        viewFlipper = findViewById(R.id.viewFlipper);

        // Configura la duración del intervalo para cada desarrollador
        viewFlipper.setFlipInterval(3000); // 3 segundos
        viewFlipper.startFlipping();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.btnRegresarMenu) {
            Intent intent = new Intent(ViewAcercaDeActivity.this, ViewContactosActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
