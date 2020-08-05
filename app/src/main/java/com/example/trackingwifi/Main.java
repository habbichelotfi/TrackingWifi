package com.example.trackingwifi;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.trackingwifi.ui.frag2.Frag2Fragment;
import com.example.trackingwifi.ui.speed.SpeedFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.example.trackingwifi.ui.main.SectionsPagerAdapter;

public class Main extends AppCompatActivity {
    private int last_frag = 0;
    private TabLayout tabs;
    private ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.ic_track_changes_black_48dp,
            R.drawable.ic_slow_motion_video_black_48dp,
            R.drawable.ic_view_list_black_48dp,
            R.drawable.ic_devices_other_black_48dp
    };
    @Override
    public void onStart() {
        super.onStart();


    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Main.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Do you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
          viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
         tabs = findViewById(R.id.tabs);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                switch (position){
                    case 0:
                        new Frag1();
                        break;
                    case 1:
                        new SpeedFragment();
                        break;
                    case 2:
                        new Frag2Fragment();
                        break;
                    case 3:
                        new DeviceList();
                        break;

                }
            }

            @Override
            public void onPageSelected(int position) {

                // do this instead, assuming your adapter reference
                // is named mAdapter:
                Fragment fragment=null;
                switch (position){
                    case 0:
                        new Frag1();
                        break;
                    case 1:
                        new SpeedFragment();
                        break;
                    case 2:
                        new Frag2Fragment();
                        break;
                    case 3:
                        new DeviceList();
                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {  }
        });
        tabs.setupWithViewPager(viewPager);


setupTabIcons();
    }
    private void setupTabIcons() {
       tabs .getTabAt(0).setIcon(tabIcons[0]);
        tabs.getTabAt(1).setIcon(tabIcons[1]);
        tabs.getTabAt(2).setIcon(tabIcons[2]);
        tabs.getTabAt(3).setIcon(tabIcons[3]);

    }
}