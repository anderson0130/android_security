package com.example.security_essentials;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    static int clave;
    TextView correo;
    TextView contrasenia;
    FirebaseAuth mAuth;
    private static final String TAG = "MostrarMensajeMA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        correo = findViewById(R.id.correo);
        contrasenia = findViewById(R.id.contrasenia1);

        Button boton_registro = (Button) findViewById(R.id.registar);
        Button boton_inicio = (Button) findViewById(R.id.login);

        boton_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)  {
            Intent cambio = new Intent(MainActivity.this, RegistroActivity.class);
            startActivity(cambio);
            }
        });

        boton_inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)  {
                login(correo.getText().toString(), contrasenia.getText().toString());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            mostrarMenuInicio();
        }
    }

    private void mostrarMenuInicio(){
        startActivity(new Intent(this, MenuInicioActivity.class));
    }

    private void login(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    mostrarMenuInicio();
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                    Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
////metodo autenticacion y geolocalizacion///