package com.example.trackingwifi;

import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class Main3Activity extends AppCompatActivity {
    private PieChart mPieChart;
    private List<PieEntry> y=new ArrayList<>();
    private PieDataSet dateset=new PieDataSet(y,"Ice Brick Diagram");
    private PieData mdata=new PieData(dateset);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);


            mPieChart= findViewById(R.id.pc);
            mPieChart.getDescription().setEnabled(false);
            mPieChart.setUsePercentValues(true);
            mPieChart.setExtraOffsets(5, 10, 5, 5);
            mPieChart.setCenterText("I am Guo Shen, I speak for myself");

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
            Legend l = mPieChart.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            l.setOrientation(Legend.LegendOrientation.VERTICAL);
            l.setDrawInside(false);
            l.setTextColor(Color.GREEN);
            l.setTextSize(12);
            l.setXEntrySpace(7f);
            l.setYEntrySpace(0f);
            l.setYOffset(0f);
            y.add(new PieEntry(0.1f,"The First Cake"));
            y.add(new PieEntry(0.5f,"The Second Cake"));
            y.add(new PieEntry(0.2f,"The Third Cake"));
            y.add(new PieEntry(0.1f,"The Fourth Cake"));
            y.add(new PieEntry(0.1f,"The 5th Cake"));
            //Set to display Values ​​outside
            dateset.setValueLinePart1OffsetPercentage(80.f);
            dateset.setValueLinePart1Length(0.2f);
            dateset.setValueLinePart2Length(0.4f);
            dateset.setValueLineColor(Color.RED);
            dateset.setValueTextColor(Color.BLACK);
            dateset.setValueTextSize(20);
            //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
            dateset.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

            dateset.setColors(ColorTemplate.MATERIAL_COLORS);
            dateset.setSelectionShift(5f);
            dateset.setSliceSpace(3f);//Blank gap between each pie chart
            mdata.setValueFormatter(new PercentFormatter());
            mPieChart.setData(mdata);
            mPieChart.invalidate();
    }
}
