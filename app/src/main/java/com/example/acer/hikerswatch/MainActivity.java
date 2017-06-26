package com.example.acer.hikerswatch;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.jar.*;

public class MainActivity extends AppCompatActivity {
    LocationManager locationManager;
    LocationListener locationListener;
    TextView latTextView ;
    TextView logTextView ;
    TextView altTextView ;
    TextView accTextView ;
    TextView addTextView;
    public void startListening(){

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            startListening();
        }
    }


    public void updateLocationInfo(Location location){
        Log.i("Location",location.toString());

          latTextView.setText("Lattitude: "+location.getLatitude());
          logTextView.setText("Longitude: "+location.getLongitude());
          altTextView.setText("Altitude: "+location.getAltitude());
          accTextView.setText("Accuracy: "+location.getAccuracy());


        Geocoder geocoder =new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> listAddresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            if(listAddresses!=null && listAddresses.size()>0)  {
                Log.i("PlaceInfo",listAddresses.get(0).toString());
                String address ="";
                if(listAddresses.get(0).getSubThoroughfare()!=null){
                    address+=listAddresses.get(0).getSubThoroughfare()+"  ";
                }
                if(listAddresses.get(0).getThoroughfare()!=null){
                    address+=listAddresses.get(0).getThoroughfare()+" , ";
                }
                if(listAddresses.get(0).getLocality()!=null){
                    address+=listAddresses.get(0).getLocality()+",";
                }
                if(listAddresses.get(0).getCountryName()!=null){
                    address+=listAddresses.get(0).getCountryName()+" , ";
                }

                //Toast.makeText(MainActivity.this,address,Toast.LENGTH_SHORT).show();
                addTextView.setText(address);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latTextView = (TextView) findViewById(R.id.latTextView);
        logTextView = (TextView) findViewById(R.id.logTextView);
        altTextView = (TextView) findViewById(R.id.altTextView);
        accTextView = (TextView) findViewById(R.id.accTextView);
        addTextView =(TextView) findViewById(R.id.addTextView);



        // to get Location Service
        locationManager = (LocationManager) (this.getSystemService(Context.LOCATION_SERVICE));
        // listen to changes in location
        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {

                 updateLocationInfo(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (Build.VERSION.SDK_INT < 23) {

            startListening();
        } else {
            // for backward compatibility with previous marshmallo devices

            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // if we don't have permission then ask for it
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);


            } else {

                locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);

                Location location =locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
                 if(location!=null){

                     updateLocationInfo(location);
                 }

            }

        }
    }
}
