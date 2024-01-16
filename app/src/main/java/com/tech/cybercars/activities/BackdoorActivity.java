package com.tech.cybercars.activities;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.Style;
import com.tech.cybercars.R;
import com.tech.cybercars.ui.main.MainActivity;
import com.tech.cybercars.ui.splash.SplashActivity;

public class BackdoorActivity extends AppCompatActivity {
    MapView mapView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_backdoor);

//        mapView = findViewById(R.id.mapbox);
//        mapView.onCreate(savedInstanceState);
//        mapView.getMapAsync(mapboxMap -> mapboxMap.setStyle(Style.MAPBOX_STREETS, style -> {
//
//        }));





        findViewById(R.id.btn_go_splash_bd).setOnClickListener(view -> {
            startActivity(new Intent(this, SplashActivity.class));
        });

        findViewById(R.id.btn_go_home_bd).setOnClickListener(view -> {
            startActivity(new Intent(this, MainActivity.class));
        });

    }
}