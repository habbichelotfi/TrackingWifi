package com.example.trackingwifi;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.trackingwifi.R;
import com.example.trackingwifi.RadarView;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;


import java.util.ArrayList;
import java.util.List;
public class Main2Activity extends AppCompatActivity {
    RadarView mRadarView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radioview);
        mRadarView=findViewById(R.id.radarView);
        mRadarView.setFrameRate(1000);
        mRadarView.setShowCircles(true);
        mRadarView.setShowPoints(true);    }

    public void stopAnimation(View view) {
        if (mRadarView != null) mRadarView.stopAnimation();
    }

    public void startAniamtion(View view) {
        if (mRadarView != null) mRadarView.startAnimation();
    }
}
