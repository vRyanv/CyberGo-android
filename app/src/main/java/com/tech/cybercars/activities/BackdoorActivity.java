package com.tech.cybercars.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.google.firebase.messaging.RemoteMessage;
import com.mapbox.mapboxsdk.maps.MapView;
import com.tech.cybercars.R;
import com.tech.cybercars.constant.Tag;
import com.tech.cybercars.services.notification.NotificationService;
import com.tech.cybercars.ui.main.MainActivity;
import com.tech.cybercars.ui.main.fragment.go.SelectTransportActivity;
import com.tech.cybercars.ui.main.fragment.go.add_share_trip_information.AddShareTripInformationActivity;
import com.tech.cybercars.ui.main.fragment.go.share_trip.ShareTripActivity;
import com.tech.cybercars.ui.splash.SplashActivity;
import com.tech.cybercars.utils.PermissionUtil;

import java.util.Date;

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
        findViewById(R.id.btn_push_notify).setOnClickListener(view -> {
            NotificationService.PushNormal(
                    this,
                    "Your registration has been approved",
                    "Now you can share your trip with everyone"
            );
        });

        findViewById(R.id.btn_go_splash_bd).setOnClickListener(view -> {
            startActivity(new Intent(this, SplashActivity.class));
        });

        findViewById(R.id.btn_go_home_bd).setOnClickListener(view -> {
            startActivity(new Intent(this, MainActivity.class));
        });

        findViewById(R.id.btn_select_location_bd).setOnClickListener(view -> {
            startActivity(new Intent(this, ShareTripActivity.class));
        });

        findViewById(R.id.btn_choose_transport_to_share).setOnClickListener(view -> {
            startActivity(new Intent(this, SelectTransportActivity.class));
        });

        findViewById(R.id.btn_search_map_bd).setOnClickListener(view -> {
            startActivity(new Intent(this, SearchMapActivity.class));
        });

        findViewById(R.id.btn_share_info_tab).setOnClickListener(view -> {
            startActivity(new Intent(this, AddShareTripInformationActivity.class));
        });
    }
}