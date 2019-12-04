package com.example.security_essentials;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;

public class MenuInicioActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,
        LocationListener {

    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private static final int PETITION_PERMIT_LOCATION = 101;
    private static final int UPDATE_INTERVAL = 500;
    private static final int FASTEST_INTERVAL = 500;

    FirebaseAuth auth;
    Button buttonRegresar;
    TextView textViewLocacion;

    private static final String LOG_TAG = "MostrarMensajeMIA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_inicio);
        auth = FirebaseAuth.getInstance();
        textViewLocacion = findViewById(R.id.textViewLocacion);
        locationRequest = new LocationRequest();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();


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

    private void actualizarLocation(){
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    private void actualizarUI(Location loc) {
        if (loc != null) {
            textViewLocacion.setText("Latitude: " + loc.getLatitude() + "\n" +
                    "Longitude: " + loc.getLongitude() + "\n");
        } else {
            textViewLocacion.setText("Latitud: (desconocida)\nLongitud: (desconocida)");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PETITION_PERMIT_LOCATION) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) { //Permiso concedido
                Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                actualizarUI(location);
            } else { //Permiso denegado:
                //Deberíamos deshabilitar toda la funcionalidad relativa a la localización.
                Log.e(LOG_TAG, "Permiso denegado");
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //Conectado correctamente a Google Play Services
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PETITION_PERMIT_LOCATION);
        } else {
            actualizarLocation();
            Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            actualizarUI(location);
        }
        //startLocationUpdate()
    }

    @Override
    public void onConnectionSuspended(int i) {
        //Se ha interrumpido la conexión con Google Play Services
        Log.e(LOG_TAG, "Se ha interrumpido la conexión con Google Play Services");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "Error grave al conectar con Google Play Services");
    }

    @Override
    public void onLocationChanged(Location location) {
        actualizarLocation();
        actualizarUI(location);
    }
}
