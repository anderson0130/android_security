package com.example.security_essentials;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
static int clave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button boton_registro = (Button) findViewById(R.id.registar);
        boton_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)  {
                Intent cambio = new Intent(MainActivity.this, registro.class);
                startActivity(cambio);
            }
        });
    }
}
