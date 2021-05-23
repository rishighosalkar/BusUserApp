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
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bususerapp.R;
import com.example.bususerapp.databinding.ActivityLiveMapsBinding;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
    private static final String TAG = "MapsActivity";
    private ActivityLiveMapsBinding binding;
    DatabaseReference databaseReference, databaseReference1;
    SharedPreferences sharedPreferences;
    TextView textView;
    String username;
    private Double lon,lat;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;

    Marker userLocationMarker;
    Circle userLocationAccuracyCircle;

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


        FirebaseApp secondApp2 = FirebaseApp.getInstance("busdriverapp-258fb");
        FirebaseDatabase secondDatabase2 = FirebaseDatabase.getInstance(secondApp2);
        databaseReference1 = secondDatabase2.getReference("Location");

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String latitude = snapshot.child(username).child("latitude").getValue().toString();
                String longitude = snapshot.child(username).child("longitude").getValue().toString();
                sharedPreferences.edit().putString("Latitude", latitude).apply();
                sharedPreferences.edit().putString("LongitudeS", longitude).apply();
                Log.i("Latitude isss::::", snapshot.child(username).child("longitude").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        databaseReference1.addValueEventListener(postListener);
        //Log.i("Postsnap", postsnapshot.toString());


        if(sharedPreferences!=null)
        {
            lat = Double.valueOf(sharedPreferences.getString("Latitude", null));
        }
        if(sharedPreferences!=null)
        {
            lon = Double.valueOf(sharedPreferences.getString("LongitudeS", null));
        }
        //Log.i("Latitude", lat.toString());


        /*databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            Integer i=0;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Log.i("Name is", postSnapshot.getKey());
                    if(postSnapshot.getKey().equals(username))
                    {
                        lat = postSnapshot.child("latitude").getValue().toString();
                        lon = postSnapshot.child("longitude").getValue().toString();
                        Log.i("Latitude Main", lat);
                        Log.i("Longitude Main", lon);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        //Log.i("LLLLL", lat);
        if(lat != null)
        {
            Log.i("Demoo", lat.toString());
            return;
        }
        Log.i("Demo", "Dont know why null");
        getCurrentLocation();
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
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
        //zoomToUserLocation();

    }

    public void getCurrentLocation(){
        FirebaseApp secondApp = FirebaseApp.getInstance("busdriverapp-258fb");
        FirebaseDatabase secondDatabase = FirebaseDatabase.getInstance(secondApp);
        //Location location = null;
        //location.setLatitude(lat);
        //location.setLongitude(lon);

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
                    lat = Double.valueOf(dataSnapshot.getValue().toString());
                    Log.i("Latitude", lat.toString());
                }
                if(dataSnapshot.getKey().equals("longitude")){
                    lon = Double.valueOf(dataSnapshot.getValue().toString());
                    Log.i("longitude", lon.toString());
                }
                LatLng latLng = new LatLng(lat, lon);
                //lon = dataSnapshot.ge.toString();
                //Log.i("Latitude", lat.toString());
                Log.i("Prev", prevChildKey);
                Location location = null;
                location.setLatitude(lat);
                location.setLongitude(lon);
                setUserLocationMarker(location);
                //setUserLocationMarker(location);
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

    private void setUserLocationMarker(Location location) {

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        if (userLocationMarker == null) {
            //Create a new marker
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.redcar));
            markerOptions.rotation(location.getBearing());
            markerOptions.anchor((float) 0.5, (float) 0.5);
            userLocationMarker = mMap.addMarker(markerOptions);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
        } else  {
            //use the previously created marker
            userLocationMarker.setPosition(latLng);
            userLocationMarker.setRotation(location.getBearing());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
        }

        if (userLocationAccuracyCircle == null) {
            CircleOptions circleOptions = new CircleOptions();
            circleOptions.center(latLng);
            circleOptions.strokeWidth(4);
            circleOptions.strokeColor(Color.argb(255, 255, 0, 0));
            circleOptions.fillColor(Color.argb(32, 255, 0, 0));
            circleOptions.radius(location.getAccuracy());
            userLocationAccuracyCircle = mMap.addCircle(circleOptions);
        } else {
            userLocationAccuracyCircle.setCenter(latLng);
            userLocationAccuracyCircle.setRadius(location.getAccuracy());
        }
    }

    private void zoomToUserLocation(Location location) {
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
//                mMap.addMarker(new MarkerOptions().position(latLng));
            }
        });
    }
}