package com.example.trackingwifi;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Log;
import android.view.*;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.trackingwifi.ui.frag2.ListwifiAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ornach.nobobutton.NoboButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static java.lang.Math.*;

public class Frag1 extends Fragment implements wifiListAdapter.ItemClickListener {

public int distance(int dbm,int freq){
    dbm=dbm*-1;
    double FSPL =  27.55;

    double m = Math.pow(10,( FSPL - (20 * log10(freq)) + dbm ) / 20 );
    m=round(m);
    return (int) m;
}
List<WifiInfo_> wifiInfoList=new ArrayList<>();
    private static final int PERMISSION_FINE_LOCATION =100 ;
     BroadcastReceiver mWifiScanReceiver;
    WifiManager  wmgr;
private Frag1ViewModel mViewModel;
RadarView mRadarView = null;
private RecyclerView recyclerView;
wifiListAdapter wifiListAdapter;
private Spinner spinner,style;
private NoboButton start,stop;
public static Frag1 newInstance() {
        return new Frag1();
    }
    private static int[] imageIconDatabase = {
            R.drawable.list,
            R.drawable.ic_track_changes_black_48dp,
            R.drawable.ic_navigation_black_48dp

};
    // stores the image database names
    private String[] imageNameDatabase = { "Liste", "Radar" ,"Navigation"};
    List<WifiInfo_> wifiInfoList1=new ArrayList<WifiInfo_>();
    List<WifiInfo_> wifiInfoList2=new ArrayList<WifiInfo_>();
    private final static ArrayList<Integer> channelsFrequency = new ArrayList<Integer>(
            Arrays.asList(0, 2412, 2417, 2422, 2427, 2432, 2437, 2442, 2447,
                    2452, 2457, 2462, 2467, 2472, 2484));
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.activity_radioview, container, false);

spinner=root.findViewById(R.id.rayon);
recyclerView=root.findViewById(R.id.list_wifis);
LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
recyclerView.setLayoutManager(linearLayoutManager);


final LinearLayout linearLayout=root.findViewById(R.id.buttons);
style=root.findViewById(R.id.style);
        CustomAdapter customAdapter=new CustomAdapter(getContext(),imageIconDatabase,imageNameDatabase);
        style.setAdapter(customAdapter);
style.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
switch (position){
    case 0:{

        mRadarView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        wifiListAdapter=new wifiListAdapter(getContext(),wifiInfoList1);
        wifiListAdapter.setClickListener(Frag1.this::onItemClick);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(wifiListAdapter);
        linearLayout.setVisibility(View.GONE);
break;
    }
    case 1:{
        mRadarView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        linearLayout.setVisibility(View.VISIBLE);
        break;
    }
    case 2:{
Intent intent=new Intent().setClass(getContext(),Navigation.class);
startActivity(intent);
    }
    default:break;
}
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
});
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.rayon, R.layout.spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (spinner.getItemAtPosition(position).toString()){
            case "15m":
                mRadarView.setRayon(15);
                break;
            case "100m":
                mRadarView.setRayon(100);
                break;
            case "150m":
                mRadarView.setRayon(150);
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
});
        wifiInfoList=new ArrayList<>();
        wifiInfoList1=new ArrayList<WifiInfo_>();
        wifiInfoList2=new ArrayList<>();
          wmgr = (WifiManager) getContext().getSystemService(Context.WIFI_SERVICE);
          mWifiScanReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context c, Intent intent) {
                if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
                    List<ScanResult>  availNetworks = wmgr.getScanResults();

                    if (availNetworks.size() > 0) {
                        // Get Each network detail
                        for (ScanResult availNetwork : availNetworks) {
                            int dbm = availNetwork.level;
                            int freq = availNetwork.frequency;
                            wifiInfoList.add(new WifiInfo_(dbm, freq, distance(dbm, freq), availNetwork.SSID));
                            int channel = channelsFrequency.indexOf(Integer.valueOf(freq));

                            wifiInfoList1.add(new WifiInfo_(dbm, freq, availNetwork.SSID, channel, availNetwork.BSSID));

wifiInfoList2.add(new WifiInfo_(dbm, freq, distance(dbm, freq), channel,1,availNetwork.SSID,availNetwork.BSSID,availNetwork.capabilities,""));
                        }

                    } else {
                        Toast.makeText(getContext(), "No wifi avaible!!", Toast.LENGTH_LONG).show();
                    }
                }else {
                    if(Build.VERSION.SDK_INT>Build.VERSION_CODES.M){
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_FINE_LOCATION);
                    }
                }
            }};

Toast.makeText(getContext(),wifiInfoList1.size()+"size wifi",Toast.LENGTH_LONG).show();





        mRadarView=root.findViewById(R.id.radarView);
        mRadarView.setFrameRate(1000);

mRadarView.setShowNetwork(true,wifiInfoList);

        start=root.findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRadarView != null) mRadarView.startAnimation();
            }
        });
        stop=root.findViewById(R.id.stop);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRadarView != null) mRadarView.stopAnimation();

            }
        });
        return root;
    }


    @Override
    public void onStart() {
        super.onStart();
        wifiListAdapter=new wifiListAdapter(getContext(),wifiInfoList1);

        recyclerView.setAdapter(wifiListAdapter);
        getContext().registerReceiver(mWifiScanReceiver,
                new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wmgr.startScan();
    }

    @Override
    public void onResume() {
        super.onResume();
        wifiListAdapter=new wifiListAdapter(getContext(),wifiInfoList1);

        recyclerView.setAdapter(wifiListAdapter);
        getContext().registerReceiver(mWifiScanReceiver,
                new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wmgr.startScan();
    }

    @Override
    public void onPause() {
        super.onPause();
        wifiListAdapter=new wifiListAdapter(getContext(),new ArrayList<WifiInfo_>());

        recyclerView.setAdapter(wifiListAdapter);
        try {
            getContext().unregisterReceiver(mWifiScanReceiver);
        }catch (Exception e){

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        wifiListAdapter=new wifiListAdapter(getContext(),new ArrayList<WifiInfo_>());

        recyclerView.setAdapter(wifiListAdapter);
       try {
           getContext().unregisterReceiver(mWifiScanReceiver);
       }catch (Exception e){

       }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(Frag1ViewModel.class);

        // TODO: Use the ViewModel
    }







    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Toast.makeText(getContext(),""+id,Toast.LENGTH_LONG).show();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
   void toMain(String name){
       Intent intent=new Intent(getActivity(),MainActivity.class);
       intent.putExtra("wifi_name","A");
       startActivity(intent);
   }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onItemClick(View view, int position) {
        WifiInfo_ wifiInfo_=wifiInfoList2.get(position);
        Intent intent=new Intent().setClass(getContext(),MainActivity.class);
        intent.putExtra("wifi_name",wifiInfo_.getName());
        intent.putExtra("mac",wifiInfo_.getBssid());
        intent.putExtra("channel",wifiInfo_.getChannel());
        intent.putExtra("distance",wifiInfo_.getDistance());
        intent.putExtra("dmb",wifiInfo_.getDmb());
        intent.putExtra("wifi_speed",wifiInfo_.getWifi_speed());
        intent.putExtra("encryption",wifiInfo_.getEncry());
        intent.putExtra("Adresse",wifiInfo_.getAddresse());
        startActivity(intent);
    }
}
