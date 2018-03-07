package com.metaphor.jenson.maptest;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, OnMapReadyCallback {
    private Button button;
    Marker marker;
    LatLng latLng;
    CameraUpdate cameraUpdate;
    private LocationListener locationListener;
    private LocationManager locationManager;
    double latitude, longitude;
    public String loc = new String(  );

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        locationManager = (LocationManager) this.getSystemService( LOCATION_SERVICE );
        button = (Button) findViewById( R.id.locationControllerGPS );
        if (ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION )
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager
                .PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions( new String[] {
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission
                        .ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, 10);
            }
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        } else {
            configureButton();
        }
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                latLng = new LatLng(latitude, longitude);
                cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
                marker = mMap.addMarker(new MarkerOptions().position(latLng).title("You are here"));
                mMap.animateCamera(cameraUpdate);

                loc = "Latitude = "+latitude+"\n Longitude = "+longitude;
//                textView.setText( loc );
//                textView.append( "\n"+location.getLatitude()+ " "+ location.getLongitude() );
//                ourLocation = location;
//                textView.append( "\n"+ourLocation.getLatitude()+ " "+ ourLocation.getLongitude() );
//                Intent intent;
//                intent = new Intent(MapsActivity.this, FormActivity.class);
//                intent.putExtra("latitude", latitude);
//                intent.putExtra("longitude", longitude);
//                startActivity(intent);

//                senddata();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent intent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS );
                startActivity( intent );
            }
        };

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void configureButton() {
        button.setOnClickListener( new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                Criteria criteria = new Criteria();
//                criteria.setAccuracy(Criteria.ACCURACY_FINE);
//                criteria.setAltitudeRequired(false);
//                criteria.setBearingRequired(false);
//                criteria.setCostAllowed(true);
//                criteria.setPowerRequirement(Criteria.POWER_LOW);
                locationManager.requestLocationUpdates( locationManager.NETWORK_PROVIDER, 5000, 1,
                        locationListener );
//                ourLocation = new Location( "gps" );
////                Location location = new Location( LOCATION_SERVICE );
//                textView = (TextView)findViewById( R.id.titleTextGPS );
//                textView.append( "\n"+ourLocation.getLatitude()+ " "+ ourLocation.getLongitude() );
            }
        } );
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

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    public void passCoordinates(View view) {
        if(latitude!=0 && longitude !=0){
                Intent intent;
                intent = new Intent(MapsActivity.this, FormActivity.class);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                startActivity(intent);
        } else {
            Context context = getApplicationContext();
            Toast.makeText(context.getApplicationContext(), "Location Not Set ",
                    Toast.LENGTH_LONG).show();
        }
    }
}
