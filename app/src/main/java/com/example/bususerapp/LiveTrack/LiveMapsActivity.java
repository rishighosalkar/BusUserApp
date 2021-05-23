package com.example.bususerapp.LiveTrack;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bususerapp.R;
import com.example.bususerapp.databinding.ActivityLiveMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LiveMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityLiveMapsBinding binding;
    DatabaseReference databaseReference;
    SharedPreferences sharedPreferences;
    TextView textView;
    String username,lat;
    Double lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        textView = (TextView) findViewById(R.id.longitude);
        Intent intent = getIntent();
        if(sharedPreferences != null)
        {
            username = sharedPreferences.getString("username", null);
        }
        Log.i("UserNameeee: ", username);
        FirebaseApp secondApp = FirebaseApp.getInstance("busdriverapp-258fb");
        FirebaseDatabase secondDatabase = FirebaseDatabase.getInstance(secondApp);
        databaseReference = secondDatabase.getReference("Location");
        lat = databaseReference.child(username).getKey();
        Log.i("Latitude", lat);
        //getCurrentLocation();
        binding = ActivityLiveMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public void getCurrentLocation(){
        FirebaseApp secondApp = FirebaseApp.getInstance("busdriverapp-258fb");
        FirebaseDatabase secondDatabase = FirebaseDatabase.getInstance(secondApp);


        //textView.setText("Latitude");
        databaseReference = secondDatabase.getReference("Location").child(username);

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                Log.i("Updated data", dataSnapshot.getValue().toString());

                if(dataSnapshot.getKey().equals("latitude"))
                {
                    lat =dataSnapshot.getValue().toString();
                    Log.i("Latitude", lat);
                }
                else{
                    lon = Double.valueOf(dataSnapshot.getValue().toString());
                    Log.i("longitude", lon.toString());
                }
                //lon = dataSnapshot.ge.toString();
                //Log.i("Latitude", lat.toString());

                Log.i("Prev", prevChildKey);
                mMap.clear();
                //LatLng latLng = new LatLng(lat, lon);
                //mMap.addMarker((new MarkerOptions().position(latLng).title("Current Location")));
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });


    }
}