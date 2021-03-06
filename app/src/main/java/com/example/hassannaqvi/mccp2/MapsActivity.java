package com.example.hassannaqvi.mccp2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private boolean Todays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean Todays = getIntent().getStringExtra("today").equals("true");
        
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        FormsDbHelper db = new FormsDbHelper(this);
        List<FormsContract> gpsList = new ArrayList<FormsContract>();
        if (Todays) {
            gpsList = db.getTodaysGPS();
        } else {
            gpsList = db.getAllGPS();
        }
        SharedPreferences sharedPref = getSharedPreferences("MC_" + FillFormActivity.FORM_ID, Context.MODE_PRIVATE);
        Double mLat = Double.valueOf(sharedPref.getString("spGPSLat", "0"));
        Double mLong = Double.valueOf(sharedPref.getString("spGPSLng", "0"));
        // Add a marker in Karachi and move the camera
        for (FormsContract gps : gpsList) {

            LatLng karachi = new LatLng(Double.valueOf(gps.getGPSLat()), Double.valueOf(gps.getGPSLng()));
            mMap.addMarker(new MarkerOptions().position(karachi).title("Form Submission for HH:" + gps.get106() + " at (LAT:" + gps.getGPSLat() + " LNG: " + gps.getGPSLng()));
            Log.d("MAP", "Form Submission here! LAT:" + gps.getGPSLat() + " LNG: " + gps.getGPSLng());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(karachi, 15));
        }
    }
}
