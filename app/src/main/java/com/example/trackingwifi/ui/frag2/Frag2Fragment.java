package com.example.trackingwifi.ui.frag2;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.example.trackingwifi.R;
import com.anychart.AnyChart;

import com.anychart.AnyChartView;
import com.anychart.chart.*;
import com.anychart.charts.*;
import com.example.trackingwifi.WifiInfo_;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.Math.log10;
import static java.lang.Math.round;


public class Frag2Fragment extends Fragment {
    private PieChart mPieChart;
    private List<PieEntry> y=new ArrayList<>();
    private PieDataSet dateset=new PieDataSet(y,"DBM DIAGRAM");
    private PieData mdata=new PieData(dateset);
    private Frag2ViewModel mViewModel;

    List<WifiInfo_> wifiInfoList=new ArrayList<WifiInfo_>();
    List<WifiInfo_> wifiInfoList1=new ArrayList<WifiInfo_>();

    ListwifiAdapter adapter;
    public static Frag2Fragment newInstance() {
        return new Frag2Fragment();
    }
    public int distance(int dbm,int freq){
        dbm=dbm*-1;
        double FSPL =  27.55;

        double m = Math.pow(10,( FSPL - (20 * log10(freq)) + dbm ) / 20 );
        m=round(m);
        return (int) m;
    }
    @SuppressWarnings("boxing")
    private final static ArrayList<Integer> channelsFrequency = new ArrayList<Integer>(
            Arrays.asList(0, 2412, 2417, 2422, 2427, 2432, 2437, 2442, 2447,
                    2452, 2457, 2462, 2467, 2472, 2484));
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.frag2_fragment, container, false);
        wifiInfoList=new ArrayList<>();
        wifiInfoList1=new ArrayList<>();
        dateset.setValueLinePart1OffsetPercentage(80.f);
        dateset.setValueLinePart1Length(0.2f);
        dateset.setValueLinePart2Length(0.4f);
        dateset.setValueLineColor(Color.RED);
        dateset.setValueTextColor(Color.BLACK);
        dateset.setValueTextSize(13);
        //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dateset.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);


        int[] colors={Color.MAGENTA,Color.RED,Color.DKGRAY,Color.GREEN,Color.CYAN,Color.BLUE,Color.BLACK,Color.GRAY,Color.LTGRAY,Color.rgb(233,150,122)};
        dateset.setColors(colors);

        WifiManager wmgr = (WifiManager) getContext().getSystemService(Context.WIFI_SERVICE);

        List<ScanResult> availNetworks = wmgr.getScanResults();
int i=0;
        if (availNetworks.size() > 0) {
            // Get Each network detail
            for (ScanResult availNetwork : availNetworks) {

                int dbm = availNetwork.level;
                int freq = availNetwork.frequency;
                int channel=channelsFrequency.indexOf(Integer.valueOf(freq));
                wifiInfoList.add(new WifiInfo_(dbm, freq,  availNetwork.SSID,channel));

                Drawable unwrappedDrawable = getResources().getDrawable( R.drawable.circle,null);
                Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
                DrawableCompat.setTint(wrappedDrawable, colors[i]);
                wifiInfoList1.add(new WifiInfo_(dbm, freq,  availNetwork.SSID,channel,availNetwork.BSSID,wrappedDrawable));

                i++;

            }
        }else {
            Toast.makeText(getContext(),"No wifi avaible!!",Toast.LENGTH_LONG).show();
        }
        RecyclerView recyclerView=root.findViewById(R.id.list_wifi);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager((linearLayoutManager));


        mPieChart= root.findViewById(R.id.piechart);
        mPieChart.getDescription().setEnabled(false);
       // mPieChart.setUsePercentValues(true);
        mPieChart.setExtraOffsets(5, 10, 5, 5);

//Animate
        mPieChart.animateXY(2000,2000);
        // mPieChart.setDrawHoleEnabled(false);//Whether it is covered
        mPieChart.setHoleColor(Color.GREEN);//Set the color of the inner circle
        mPieChart.setTransparentCircleColor(Color.WHITE);//Set the color of the inner circle edge
        mPieChart.setTransparentCircleAlpha(110);
        mPieChart.setRotationAngle(0);
        mPieChart.setHoleRadius(58f);
        mPieChart.setTransparentCircleRadius(61f);
        //Legend settings

        List channels = new ArrayList();
        for ( i=0;i<wifiInfoList.size();i++){

            if (wifiInfoList.get(i)!=null){
                int d=-(wifiInfoList.get(i).getDmb());
                y.add(new PieEntry(d, d+" dbm"+"\n"+wifiInfoList.get(i).getChannel()+" Chan",wifiInfoList.get(i).getName()));
                channels.add(wifiInfoList.get(i).getChannel());
            }


        }
        //Set to display Values ​​outside


        dateset.setSelectionShift(5f);
        dateset.setSliceSpace(3f);//Blank gap between each pie chart
        mdata.setValueFormatter(new MyValueFormatter());
        mPieChart.setData(mdata);
        mPieChart.invalidate();


       // createRandomBarGraph(wifiInfoList);
        adapter=new ListwifiAdapter(getContext(),wifiInfoList1);
        wifiInfoList=new ArrayList<>();
        wifiInfoList1=new ArrayList<>();
        y=new ArrayList<>();
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);
    return root ;
    }


    public ArrayList<String> getList(Calendar startDate, Calendar endDate){
        ArrayList<String> list = new ArrayList<String>();
        while(startDate.compareTo(endDate)<=0){
            list.add(getDate(startDate));
            startDate.add(Calendar.DAY_OF_MONTH,1);
        }
        return list;
    }

    public String getDate(Calendar cld){
        String curDate = cld.get(Calendar.YEAR) + "/" + (cld.get(Calendar.MONTH) + 1) + "/"
                +cld.get(Calendar.DAY_OF_MONTH);
        try{
            Date date = new SimpleDateFormat("yyyy/MM/dd").parse(curDate);
            curDate =  new SimpleDateFormat("yyy/MM/dd").format(date);
        }catch(ParseException e){
            e.printStackTrace();
        }
        return curDate;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(Frag2ViewModel.class);

    }

static class MyValueFormatter extends ValueFormatter {
    private String[] days ={"Mo", "Tu", "Wed", "Th", "Fr", "Sa", "Su"};

    @Override
    public String getPieLabel(float value, PieEntry pieEntry) {

        return pieEntry.getData().toString();
    }

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        return days[Math.round(value)].toString();
    }
}
}
