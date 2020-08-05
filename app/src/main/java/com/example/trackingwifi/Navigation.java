package com.example.trackingwifi;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.*;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

public class Navigation extends AppCompatActivity implements SensorEventListener{

NavigationView navigationView=null;
Toolbar toolbar;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    private static final int PERMISSION_FINE_LOCATION =100 ;
TextView textView;
    FusedLocationProviderClient fusedLocationProviderClient;
    private SensorManager sensorManager;
    private final float[] accelerometerReading = new float[3];
    private final float[] magnetometerReading = new float[3];

    private final float[] rotationMatrix = new float[9];
    private final float[] orientationAngles = new float[3];
    private ImageView image;

    // record the compass picture angle turned
    private float currentDegree = 0f;

    // device sensor manager
    private SensorManager mSensorManager;

    TextView tvHeading;
    @Override
    protected void onResume() {
        super.onResume();

        // Get updates from the accelerometer and magnetometer at a constant rate.
        // To make batch operations more efficient and reduce power consumption,
        // provide support for delaying updates to the application.
        //
        // In this example, the sensor reporting delay is small enough such that
        // the application receives an update before the system checks the sensor
        // readings again.
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Don't receive any more updates from either sensor.
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        navigationView=findViewById(R.id.navigation);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        image = (ImageView) findViewById(R.id.imageViewCompass);
        toolbar=findViewById(R.id.toolbar);
        tvHeading=findViewById(R.id.longi);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        navigationView.setFrameRate(1000);
        locationRequest=new LocationRequest();
        locationRequest.setInterval(3000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationCallback=new LocationCallback(){

            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location location=locationResult.getLastLocation();
                updateUIvalues(location);
            }
        };


       // updateGps();
        //startLocationUpdate();
//navigationView.startAnimation();
    }

    private void updateUIvalues(Location location) {
//navigationView.setLongitude(location.getLatitude());
        tvHeading.setText(""+location.getLatitude());
        Geocoder geocoder=new Geocoder(Navigation.this);
        try{
            List<Address> list= geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
           // adresse.setText(""+list.get(0).getAddressLine(0));

        }
        catch (Exception e){
            Toast.makeText(Navigation.this,"DOESNT WORK!",Toast.LENGTH_SHORT).show();
        }
    }



    private void startLocationUpdate() {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback,null);
        updateGps();
    }

    private void stopLocationUpadate() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    private void updateGps(){
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(Navigation.this);
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this,new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    updateUIvalues(location);
                }
            });
        }
        else {
            if(Build.VERSION.SDK_INT>Build.VERSION_CODES.M){
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_FINE_LOCATION);
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float degree = Math.round(event.values[0]);
        navigationView.setLongitude(degree);

        tvHeading.setText("Heading: " + Float.toString(degree) + " degrees");

        // create a rotation animation (reverse turn degree degrees)
        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        // how long the animation will take place
        ra.setDuration(210);

        // set the animation after the end of the reservation status
        ra.setFillAfter(true);

        // Start the animation
        image.startAnimation(ra);
        currentDegree = -degree;
    }

    // Compute the three orientation angles based on the most recent readings from
    // the device's accelerometer and magnetometer.
    public void updateOrientationAngles() {
        // Update rotation matrix, which is needed to update orientation angles.
        SensorManager.getRotationMatrix(rotationMatrix, null,
                accelerometerReading, magnetometerReading);

        // "mRotationMatrix" now has up-to-date information.

        SensorManager.getOrientation(rotationMatrix, orientationAngles);
Toast.makeText(this,""+orientationAngles[1],Toast.LENGTH_LONG).show();
        // "mOrientationAngles" now has up-to-date information..
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
