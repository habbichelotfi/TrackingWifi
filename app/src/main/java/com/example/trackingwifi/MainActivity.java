package com.example.trackingwifi;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import com.github.anastr.speedviewlib.AwesomeSpeedometer;
import com.google.android.gms.location.*;
import com.google.android.gms.tasks.OnSuccessListener;
import com.ornach.nobobutton.NoboButton;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;
import me.itangqi.waveloadingview.WaveLoadingView;
import mehdi.sakout.fancybuttons.FancyButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private WaveLoadingView mWaveLoadingView;
private FancyButton button,btn_test;
    private  int dmb,freq,tdistance,tchannel,twifi_speed;
   private String tname,Bssid,encry,addresse;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_FINE_LOCATION:
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    updateGps();
                }else{
                    Toast.makeText(this,"jaja",Toast.LENGTH_LONG).show();
                    finish();
                }

        }
    }

    private static final int PERMISSION_FINE_LOCATION =100 ;
    WifiManager wifiManager;
    private TextView distance,text,mac,wifi_name,channel,encryp,adresse,wifi_speed;
    private Button reload;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    Switch gps,update;
    FusedLocationProviderClient fusedLocationProviderClient;
    Toolbar toolbar;
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        button=findViewById(R.id.btn_connect);
        btn_test=findViewById(R.id.btn_test);
toolbar=findViewById(R.id.toolbar);
linearLayout=findViewById(R.id.Background);
        mWaveLoadingView =  findViewById(R.id.waveLoadingView);
        mWaveLoadingView.setShapeType(WaveLoadingView.ShapeType.RECTANGLE);
        mWaveLoadingView.setTopTitle("DBM");
        mWaveLoadingView.setCenterTitleColor(Color.WHITE);
        mWaveLoadingView.setBottomTitleSize(18);
        mWaveLoadingView.setProgressValue(0);
        mWaveLoadingView.setBorderWidth(10);
        mWaveLoadingView.setAmplitudeRatio(100);
        //mWaveLoadingView.setWaveColor(Color.GRAY);
        mWaveLoadingView.setTopTitleStrokeWidth(3);
        mWaveLoadingView.setTopTitleColor(Color.RED);
        mWaveLoadingView.setAnimDuration(3000);
        mWaveLoadingView.pauseAnimation();
        mWaveLoadingView.resumeAnimation();
        mWaveLoadingView.cancelAnimation();
        mWaveLoadingView.startAnimation();
        distance=findViewById(R.id.distance);
        encryp=findViewById(R.id.encry);
        adresse=findViewById(R.id.adresse);
        wifi_speed=findViewById(R.id.wifi_speed);
        wifi_name=findViewById(R.id.wifi_name);
        mac=findViewById(R.id.addresse_mac);
channel=findViewById(R.id.channel);

if(getIntent()!=null){

     dmb=getIntent().getExtras().getInt("dmb");
    int color = Color.WHITE;
    int color1=Color.WHITE;
    String quality="";
     if(dmb>-60 && dmb<-30){
         color=Color.GREEN;
quality="Very good";
         color1=Color.rgb(127,255,212);
     }
     if (dmb>-70 && dmb<-60){
         color=Color.rgb(255,165,0);
         quality="Good";
         color1=Color.rgb(255,160,122);
     }
     if (dmb>-90 && dmb<-70){
         color=Color.RED;
         quality="Not good";
         color1=Color.rgb(255,99,71);
     }
     mWaveLoadingView.setCenterTitle(""+dmb);
     mWaveLoadingView.setTopTitle(quality);
     mWaveLoadingView.setBottomTitle("dbm");
     mWaveLoadingView.setProgressValue(-(dmb));
     mWaveLoadingView.setWaveColor(color);
     mWaveLoadingView.setCenterTitleColor(Color.WHITE);
     mWaveLoadingView.setBottomTitleColor(Color.WHITE);
     mWaveLoadingView.setBottomTitleSize(15f);
    mWaveLoadingView.setTopTitleColor(Color.WHITE);
linearLayout.setBackgroundColor(color1);
    freq=getIntent().getExtras().getInt("freq");
     tdistance=getIntent().getExtras().getInt("distance");
     tchannel=getIntent().getExtras().getInt("channel");
     twifi_speed=getIntent().getExtras().getInt("wifi_speed");
     tname=getIntent().getExtras().getString("wifi_name");
     toolbar.setTitle(tname);
     toolbar.setNavigationIcon(R.drawable.ic_action_back);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //What to do on back clicked
            finish();
        }
    });
     Bssid=getIntent().getExtras().getString("mac");
     encry=getIntent().getExtras().getString("encryption");
     addresse=getIntent().getExtras().getString("Adresse");
    encryp.setText(encry);
    wifi_name.setText(tname);
    channel.setText(" Channel-> "+tchannel);
    distance.setText(""+tdistance+" Metre");
    mac.setText(Bssid);
    wifi_speed.setText("0 mp/s");
if (!checkWifiOnAndConnected(tname)){
    button.setVisibility(View.VISIBLE);
}
button.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
showDialog();
    }
});

}
        final WifiManager mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

         final BroadcastReceiver mWifiScanReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context c, Intent intent) {
                Toast.makeText(MainActivity.this,"kakaka",Toast.LENGTH_LONG).show();

                if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                    List<ScanResult> mScanResults = mWifiManager.getScanResults();
                    Toast.makeText(MainActivity.this,mScanResults.size()+"",Toast.LENGTH_LONG).show();

                }
            }
        };
        registerReceiver(mWifiScanReceiver,
                new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        mWifiManager.startScan();
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


updateGps();

    }

    private void showDialog() {
        AlertDialog.Builder alertDialog ;
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            alertDialog=new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        }else {
            alertDialog=new AlertDialog.Builder(this);

        }
        LayoutInflater layoutInflater=getLayoutInflater();
        View view=layoutInflater.inflate(R.layout.wifi_connection,null);
        TextView encr,name,signal;
        NoboButton cancel,connect;
        EditText pass;
        CheckBox checkBox;
        encr=view.findViewById(R.id.tv_encry);
        encr.setText(encry);
        name=view.findViewById(R.id.tv_nom);
        name.setText(tname);
        signal=view.findViewById(R.id.tv_force);
        signal.setText(""+dmb);
        pass=view.findViewById(R.id.et_password);
        checkBox=view.findViewById(R.id.checkBox);
        cancel=view.findViewById(R.id.cancel);
        connect=view.findViewById(R.id.connect);
        if(checkBox.isActivated()){
            pass.setShowSoftInputOnFocus(true);
        }
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.setCancelable(true);
            }
        });
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        alertDialog.setView(view);
        alertDialog.setCancelable(true);


        AlertDialog dialog=alertDialog.create();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
dialog.show();
    }

    private void startLocationUpdate() {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback,null);
        updateGps();
    }

    private void stopLocationUpadate() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    private void updateGps(){
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(MainActivity.this);
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
    private boolean checkWifiOnAndConnected(String name) {
        WifiManager wifiMgr = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        if (wifiMgr.isWifiEnabled()) { // Wi-Fi adapter is ON

            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
                if (wifiInfo.getSSID().equals(name)){
                    return true;
                }
                else {
                    return false;
                }

        }
        else {
            return false; // Wi-Fi adapter is OFF
        }
    }

    private void updateUIvalues(Location location) {


        Geocoder geocoder=new Geocoder(MainActivity.this);
        try{
           List<Address> list= geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            adresse.setText(""+list.get(0).getAddressLine(0));

        }
        catch (Exception e){
Toast.makeText(MainActivity.this,"DOESNT WORK!",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
       // unregisterReceiver(mWifiScanReceiver);
    }
}
