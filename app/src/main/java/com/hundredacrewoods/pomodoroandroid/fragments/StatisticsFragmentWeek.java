package com.hundredacrewoods.pomodoroandroid.fragments;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.hundredacrewoods.pomodoroandroid.R;

import java.util.ArrayList;
import java.util.List;

public class StatisticsFragmentWeek extends Fragment {

    private BarChart mBarChart;
    List<BarEntry> successPomo;
    List<BarEntry> failedPomo;
    List<String> weekLabels;

    public StatisticsFragmentWeek(){

    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View rootView = inflater.inflate(R.layout.fragment_statistics_week, container, false);
        mBarChart = rootView.findViewById(R.id.week_barchart);
        loadData();
        addWeekLabel();
        setDataOnBarChart();
        setCustomizationOnBarChart();
        return rootView;
    }

    public void loadData(){
        successPomo = new ArrayList<>();
        // x-axis == day
        // x=0 is equal to Mon
        successPomo.add(new BarEntry(0f, 10f));
        successPomo.add(new BarEntry(1f, 2f));
        successPomo.add(new BarEntry(2f, 7f));
        successPomo.add(new BarEntry(3f, 12f));
        successPomo.add(new BarEntry(5f, 14f));
        successPomo.add(new BarEntry(6f, 4f));

        failedPomo = new ArrayList<>();
        failedPomo.add(new BarEntry(0f,2f));
        failedPomo.add(new BarEntry(1f,5f));
        failedPomo.add(new BarEntry(2f,1f));
        failedPomo.add(new BarEntry(3f,0f));
        failedPomo.add(new BarEntry(4f,6f));
        failedPomo.add(new BarEntry(5f,3f));
        failedPomo.add(new BarEntry(6f,4f));
    }

    public void addWeekLabel(){
        weekLabels = new ArrayList<>();
        weekLabels.add("Mon");
        weekLabels.add("Tue");
        weekLabels.add("Wed");
        weekLabels.add("Thu");
        weekLabels.add("Fri");
        weekLabels.add("Sat");
        weekLabels.add("Sun");
    }

    public void setDataOnBarChart(){
        BarDataSet set = new BarDataSet(successPomo, "Success Pomodoro");
        set.setColors(Color.rgb(255,193,7));

        BarDataSet set2 = new BarDataSet(failedPomo, "Failed Pomodoro");
        set2.setColor(Color.rgb(189,189,189));

        BarData data = new BarData(set, set2);
        data.setBarWidth(0.6f); // set custom bar width
        mBarChart.setData(data);
    }

    public void setCustomizationOnBarChart(){
        //chart.groupBars(0.00001f, 0.2f, 0.02f);
        //chart.setFitBars(true); // make the x-axis fit exactly all bars
        mBarChart.animateXY(1000,1000);

        mBarChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(weekLabels));
        mBarChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mBarChart.getAxisRight().setEnabled(false);
        mBarChart.getAxisLeft().setDrawGridLines(false);
        mBarChart.getXAxis().setDrawGridLines(false);
        mBarChart.getDescription().setEnabled(false);
        //chart.getXAxis().setCenterAxisLabels(true);
        mBarChart.setDragEnabled(true);
        mBarChart.setScaleEnabled(false);
        mBarChart.invalidate(); // refresh
    }
}
