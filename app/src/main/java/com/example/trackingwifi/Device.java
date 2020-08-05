package com.example.trackingwifi;

public class Device {
    private String type_device,device,ip,mac,marque;

    public String getType_device() {
        return type_device;
    }

    public String getDevice() {
        return device;
    }

    public String getIp() {
        return ip;
    }

    public String getMax() {
        return mac;
    }

    public String getMarque() {
        return marque;
    }

    public Device(String type_device, String device, String ip, String mac, String marque) {
        this.type_device = type_device;
        this.device = device;
        this.ip = ip;
        this.mac = mac;
        this.marque = marque;
    }
    public Device(String type_device, String device, String ip) {
        this.type_device = type_device;
        this.device = device;
        this.ip = ip;

    }
}
