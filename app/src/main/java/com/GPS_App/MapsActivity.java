package com.GPS_App;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, OnSuccessListener<Location> {

    private GoogleMap mMap;
    private Button btn_refresh;
    Task<Location> lastLocation;
    private Double lat, longi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        btn_refresh = findViewById(R.id.btn_refresh);
        btn_refresh.setOnClickListener(this);

        ActivityCompat.requestPermissions(MapsActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 123);
        createLocationRequest();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_refresh :
                //lastLocation = fusedLocationClient.getLastLocation();
                lastLocation.addOnSuccessListener(this,this);
                break;
        }
    }
    @Override
    public void onSuccess(Location location) {
        if (location!=null)
        {
            String lat = String.valueOf(location.getLatitude());
            String longi = String.valueOf(location.getLongitude());
        }
    }

    protected void createLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        getFusedLocationProviderClient(this).requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        lat = locationResult.getLastLocation().getLatitude();
                        longi = locationResult.getLastLocation().getLongitude();
                        LatLng UB = new LatLng(lat,longi);
                        mMap.addMarker(new MarkerOptions().position(UB).title("Marker in UB"));
                        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(UB, 15);
                        mMap.animateCamera(yourLocation);
                    }
                },
                Looper.myLooper());
    }

    @Override
    public void onStop(){
        super.onStop();
        System.exit(0);
    }

}
