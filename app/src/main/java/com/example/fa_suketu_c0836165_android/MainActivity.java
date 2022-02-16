package com.example.fa_suketu_c0836165_android;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.location.LocationRequest;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback , GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerDragListener {

    private GoogleMap mMap;
    Marker mCurrLocationMarker,updateMarker;
    String mapType;
    DbHelper dbHelper;
    DataModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            mapType=bundle.getString("TYPE");
            model=bundle.getParcelable("MODEL");

            if(mapType.equalsIgnoreCase("")){

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Alert")
                        .setMessage("Drag The Marker TO Update the Location")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setCancelable(true)
                        .show();

            }else{

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Alert")
                        .setMessage("Long Press To Add data and visible marker")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                            }
                        })


                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setCancelable(true)
                        .show();



            }
        }

        dbHelper=new DbHelper(MainActivity.this);
        getLocation();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMarkerDragListener(this);

        if(mapType.equalsIgnoreCase("Normal")){
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        }else if(mapType.equalsIgnoreCase("Satellite")){
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        }else if(mapType.equalsIgnoreCase("Hybrid")){
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        }else if(mapType.equalsIgnoreCase("Terrain")){
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        }else{
            mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
        }

        if(mapType.equalsIgnoreCase("")){

            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            //Place current location marker
            LatLng latLng = new LatLng(Double.parseDouble(model.getLat()), Double.parseDouble(model.getLng()));
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(Utils.getAddress(Double.parseDouble(model.getLng()),Double.parseDouble(model.getLng()), MainActivity.this));
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            markerOptions.draggable(true);
            updateMarker = mMap.addMarker(markerOptions);

            //move map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        }




    }





    private void getLocation() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.getFusedLocationProviderClient(MainActivity.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {

                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(getApplicationContext())
                                .removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int latestlocIndex = locationResult.getLocations().size() - 1;
                            double lati = locationResult.getLocations().get(latestlocIndex).getLatitude();
                            double longi = locationResult.getLocations().get(latestlocIndex).getLongitude();
                            Toast.makeText(MainActivity.this,"CurrentAddress: " + Utils.getAddress(lati , longi , MainActivity.this ).trim(), Toast.LENGTH_SHORT).show();

                            Location location = new Location("providerNA");
                            location.setLongitude(longi);
                            location.setLatitude(lati);
                            if (mCurrLocationMarker != null) {
                                mCurrLocationMarker.remove();
                            }
                            // inorder to place the current position marker on the map
                            LatLng latLng = new LatLng(lati, longi);
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(latLng);
                            markerOptions.title(Utils.getAddress(lati,longi, MainActivity.this));
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                            mCurrLocationMarker = mMap.addMarker(markerOptions);

                            //function to move the map camera view
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));



                        } else {

                        }
                    }
                }, Looper.getMainLooper());

    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        double lat = latLng.latitude;
        double lng = latLng.longitude;
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(Utils.getAddress(lat,lng, MainActivity.this))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));

        dbHelper.insert(Utils.getAddress(lat,lng, MainActivity.this),String.valueOf(lat),String.valueOf(lng),"false");
        Toast.makeText(MainActivity.this, Utils.getAddress(lat , lng , this).trim() + "  Inserted", Toast.LENGTH_SHORT).show();

    }



    @Override
    public void onMarkerDrag(@NonNull Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(@NonNull Marker marker) {
        updateMarker.remove();


        MarkerOptions markerOptions = new MarkerOptions().position(marker.getPosition()).title("New Location").draggable(true);
        markerOptions.position(marker.getPosition());
        markerOptions.title("New Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        markerOptions.draggable(true);
        updateMarker = mMap.addMarker(markerOptions);

        dbHelper.updatePlace(model,Utils.getAddress(marker.getPosition().latitude, marker.getPosition().longitude, MainActivity.this),String.valueOf(marker.getPosition().latitude),String.valueOf(marker.getPosition().longitude));
        Toast.makeText(MainActivity.this, Utils.getAddress(marker.getPosition().latitude, marker.getPosition().longitude, MainActivity.this).trim() + "Updated Successful", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onMarkerDragStart(@NonNull Marker marker) {

    }
}