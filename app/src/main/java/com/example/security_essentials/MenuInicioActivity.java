package com.example.security_essentials;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MenuInicioActivity extends AppCompatActivity {
    FirebaseAuth auth;
    Button buttonRegresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_inicio);
        auth = FirebaseAuth.getInstance();
        buttonRegresar = findViewById(R.id.buttonRegresar);
        buttonRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salir();
            }
        });
    }


    private void salir() {
        auth.signOut();
        mostrarMainActivity();
    }

    private void mostrarMainActivity(){
        startActivity(new Intent(this, MainActivity.class));
    }

}
