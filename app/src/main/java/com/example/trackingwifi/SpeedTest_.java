package com.example.trackingwifi;

import java.util.Date;

public class SpeedTest_ {
    private String download,upload;
    private String date ;

    private String namewifi;

    public String getDownload() {
        return download;
    }

    public String getUpload() {
        return upload;
    }

    public String getDate() {
        return date;
    }

    public String getNamewifi() {
        return namewifi;
    }

    public SpeedTest_(String download, String upload, String date, String namewifi) {
        this.download = download;

        this.upload = upload;
        this.date = date;
        this.namewifi = namewifi;
    }
}
