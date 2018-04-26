package com.hundredacrewoods.pomodoroandroid.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.hundredacrewoods.pomodoroandroid.R;

import java.util.ArrayList;

public class StatisticsFragmentOverall extends Fragment{

    private PieChart mPieChart;

    public  StatisticsFragmentOverall(){

    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_statistics_overall, container, false);
        mPieChart = rootView.findViewById(R.id.overall_piechart);

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(29f,"Success"));
        entries.add(new PieEntry(3f, "Failed"));

        PieDataSet dataset = new PieDataSet(entries, "Data");
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);

        PieData data = new PieData(dataset);
        mPieChart.setData(data);

        mPieChart.setHoleRadius(0);
        mPieChart.setDrawEntryLabels(true);
        mPieChart.setTransparentCircleRadius(0);
        mPieChart.setUsePercentValues(true);
        mPieChart.setCenterTextSize(20);
        mPieChart.animateXY(1500,1000);
        mPieChart.setDescription(null);
        mPieChart.invalidate();

        return rootView;
    }
}
