package com.aayush.journeyjournal;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.aayush.journeyjournal.databinding.ActivityMapsBinding;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    HashMap<String,String> mapData = new HashMap<>();
    FloatingActionButton fabSetLoc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fabSetLoc = findViewById(R.id.setLocationBtn);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fabSetLoc.setOnClickListener(view-> {
            Intent intent = new Intent();
            intent.putExtra("location",mapData);
            setResult(RESULT_OK, intent);
            finish();
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                MarkerOptions options = new MarkerOptions();
                options.position(latLng);
                mMap.clear();
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 6));
                mMap.addMarker(options);
                mapData = LatLongToAddress(latLng.latitude, latLng.longitude);
            }
        });
    }


    public HashMap<String, String> LatLongToAddress(double lat, double lng) {
        HashMap<String, String> pointedAddress = new HashMap<>();
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addrList = geocoder.getFromLocation(lat, lng, 1);
            if (addrList.size() > 0) {
                Address addr = addrList.get(0);
                String adminArea = addr.getAdminArea();
                String subAdmin = addr.getSubAdminArea();
                String locality = addr.getLocality();
                String countryName = addr.getCountryName();
                pointedAddress.put("countryName", countryName);
                pointedAddress.put("adminArea", adminArea);
                pointedAddress.put("subAdmin", subAdmin);
                pointedAddress.put("locality", locality);
                return pointedAddress;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return pointedAddress;
    }


}