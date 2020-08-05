package com.example.trackingwifi;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.trackingwifi.ui.speed.SpeedFragment;

public class Speed extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speed_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, SpeedFragment.newInstance())
                    .commitNow();
        }
    }
}
