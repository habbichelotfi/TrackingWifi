package com.example.trackingwifi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {
    Context context;
    int flags[];
    String[] name;
    LayoutInflater inflter;

    public CustomAdapter(Context applicationContext, int[] flags, String[] names) {
        this.context = applicationContext;
        this.flags = flags;
        this.name = names;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return flags.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.spinner_image, null);
        ImageView icon =  view.findViewById(R.id.imageView);
        icon.setImageResource(flags[i]);
        return view;
    }
}