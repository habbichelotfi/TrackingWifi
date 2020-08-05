package com.example.trackingwifi;

import android.graphics.drawable.Drawable;

public class WifiInfo_ {
    private int dmb,freq,distance,channel,wifi_speed;
    private String name,Bssid,encry,addresse;
Drawable drawable;

    public Drawable getColor() {
        return drawable;
    }



    public int getWifi_speed() {
        return wifi_speed;
    }

    public String getEncry() {
        return encry;
    }

    public String getAddresse() {
        return addresse;
    }

    public WifiInfo_(int dmb, int freq, int distance, int channel, int wifi_speed, String name, String bssid, String encry, String addresse) {
        this.dmb = dmb;
        this.freq = freq;
        this.distance = distance;
        this.channel = channel;
        this.wifi_speed = wifi_speed;
        this.name = name;
        Bssid = bssid;
        this.encry = encry;
        this.addresse = addresse;
    }

    public int getChannel() {
        return channel;
    }

    public WifiInfo_(int dmb, int freq, int distance, String name) {
        this.dmb = dmb;
        this.freq = freq;
        this.distance = distance;
        this.name = name;
    }
    public WifiInfo_(int dmb, int freq, String name,int channel) {
        this.dmb = dmb;
        this.freq = freq;
        this.channel = channel;
        this.name = name;
    }
    public WifiInfo_(int dmb, int freq, String name,int channel,String bssid,Drawable color) {
        this.dmb = dmb;
        this.freq = freq;
        this.channel = channel;
        this.name = name;
        this.Bssid=bssid;
        this.drawable=color;
    }
    public WifiInfo_(int dmb, int freq, String name,int channel,String bssid) {
        this.dmb = dmb;
        this.freq = freq;
        this.channel = channel;
        this.name = name;
        this.Bssid=bssid;
    }

    public String getBssid() {
        return Bssid;
    }

    public int getDmb() {
        return dmb;
    }

    public int getFreq() {
        return freq;
    }

    public int getDistance() {
        return distance;
    }

    public String getName() {
        return name;
    }
}
