package com.example.security_essentials;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class RegistroActivity extends AppCompatActivity {
    private static SecretKeySpec secret;
    static String clave="anderson";
    byte[]amparo;
    TextView clave1;
    TextView clave2;
    TextView correo;
    FirebaseAuth mAuth;
    private static final String TAG = "MostrarMensajeRA";
    private FirebaseDatabase baseDatos;
    private DatabaseReference miRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        mAuth = FirebaseAuth.getInstance();
        correo = findViewById(R.id.correo);
        clave1 = findViewById(R.id.contrasenia1);
        clave2 = findViewById(R.id.contrasenia2);

        baseDatos = FirebaseDatabase.getInstance();
        miRef = baseDatos.getReference();

        Button registrar = (Button)findViewById(R.id.registro);
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)  {
                if (clave1.getText().toString().equals(clave2.getText().toString())){
                    createUserWithEmailAndPassword(correo.getText().toString(), clave1.getText().toString());
                    //Toast.makeText(RegistroActivity.this,"c" +amparo,Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(RegistroActivity.this,"clave incorrecta",Toast.LENGTH_LONG).show();
                }
            }
        });

        Button atras = (Button) findViewById(R.id.atras);
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)  {
            Intent cambio = new Intent(RegistroActivity.this, MainActivity.class);
            startActivity(cambio);
            }
        });
    }

    private void guardarUsuarioSharedP(Usuario usuario){
        SharedPreferences prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("email", usuario.email);
        editor.putString("uid", usuario.uid);
        editor.commit();
    }

    public static SecretKey generateKey() throws NoSuchAlgorithmException, InvalidKeyException {
        return secret  = new SecretKeySpec(clave .getBytes(), "AES");
    }

    public static byte[] encryptMsg(String message, SecretKey secret) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        byte[] cipherText = cipher.doFinal(message.getBytes("UTF-8"));
        return cipherText;
    }

    public static String decrryptMsg(byte[] cipherText, SecretKey secret) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidParameterException , IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException  {
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secret);
        String decryptString = new String(cipher.doFinal(cipherText), "UTF-8");
        return decryptString;
    }

    private void createUserWithEmailAndPassword(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    Usuario usuario = new Usuario(user.getUid(), user.getEmail());
                    miRef.child("users").child(user.getUid()).setValue(usuario);
                    guardarUsuarioSharedP(usuario);
                    Toast.makeText(RegistroActivity.this, "Registro exitoso.", Toast.LENGTH_SHORT).show();
                    mostrarMenuInicio();
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(RegistroActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
                }
            });
    }

    private void mostrarMenuInicio(){
        startActivity(new Intent(this, MainActivity.class));
    }
}

