package com.example.trackingwifi;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;


import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.trackingwifi.ui.frag2.ListwifiAdapter;

import java.nio.channels.Channel;
import java.util.ArrayList;
import java.util.List;

import static android.os.Looper.getMainLooper;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeviceList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeviceList extends Fragment implements WifiP2pManager.PeerListListener, WifiP2pManager.ChannelListener {
    RecyclerView recyclerView;
    List<Device> list=new ArrayList<>();
    WifiP2pManager manager;
    WifiP2pManager.Channel channel;
    IntentFilter intentFilter= new IntentFilter();
    private List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();

    private WifiP2pDevice device;
    BroadcastReceiver receiver,mWifiScanReceiver;
    DeviceListAdapter deviceListAdapter;
    private List _peers = new ArrayList();
    private BroadcastReceiver _broadcastReceiver = null;

    private WifiP2pManager.PeerListListener _peerListListener;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DeviceList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DeviceList.
     */
    // TODO: Rename and change types and number of parameters
    public static DeviceList newInstance(String param1, String param2) {
        DeviceList fragment = new DeviceList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);



    }
    @Override
    public void onResume() {
        super.onResume();
     //   Toast.makeText(getContext(), "No  found",Toast.LENGTH_LONG).show();

        _broadcastReceiver = new WiFiDirectBroadcastReceiver(manager, channel, this, _peerListListener);
        getContext().registerReceiver(mWifiScanReceiver, intentFilter);
    }
    @Override
    public void onStart() {
        super.onStart();
        Toast.makeText(getContext(), "On start",Toast.LENGTH_LONG).show();

        _broadcastReceiver = new WiFiDirectBroadcastReceiver(manager, channel, this, _peerListListener);
        getContext().registerReceiver(mWifiScanReceiver, intentFilter);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_device_list, container, false);
        TextView name,number_devices;
        name=root.findViewById(R.id.name_wifi);
        number_devices=root.findViewById(R.id.number_devices);
        recyclerView=root.findViewById(R.id.liste_device);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        manager = (WifiP2pManager) getContext().getSystemService(Context.WIFI_P2P_SERVICE);
        channel =  manager.initialize(getContext(), getMainLooper(), this);
        receiver = new WiFiDirectBroadcastReceiver(manager, channel, this,_peerListListener);
        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        manager = (WifiP2pManager) getContext().getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(getContext(), getMainLooper(), this);
        if (channel==null){
            Toast.makeText(getContext(), "channel nul",Toast.LENGTH_LONG).show();

        }

        manager.requestPeers(channel, new WifiP2pManager.PeerListListener() {
            @Override
            public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {

                for (WifiP2pDevice device : wifiP2pDeviceList.getDeviceList())
                {
                    Toast.makeText(getContext(),wifiP2pDeviceList.getDeviceList().size()+"jajajajaj",Toast.LENGTH_LONG).show();
peers.add(device);
                    list.add(new Device(device.primaryDeviceType,device.deviceName,device.deviceAddress));

                    if (device.deviceName.equals("ABC"))
                        Toast.makeText(getContext(), "widi"+wifiP2pDeviceList.getDeviceList().size(),Toast.LENGTH_LONG).show();
                    // device.deviceName
                }
            }
        });
      //  Toast.makeText(getContext(), "jaja"+list.get(0).getDevice(),Toast.LENGTH_LONG).show();


        deviceListAdapter=new DeviceListAdapter(getContext(),list);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(deviceListAdapter);
         List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();

        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
//        getContext().unregisterReceiver(_broadcastReceiver);
    }

    @Override
    public void onPeersAvailable(WifiP2pDeviceList peerList) {

        peers.clear();
        peers.addAll(peerList.getDeviceList());
        Toast.makeText(getContext(),peerList.getDeviceList().size()+"jajajajaj",Toast.LENGTH_LONG).show();

        ((DeviceListAdapter) deviceListAdapter).notifyDataSetChanged();
        if (peers.size() == 0) {
            return;
        }
    }

    @Override
    public void onChannelDisconnected() {

    }
}
