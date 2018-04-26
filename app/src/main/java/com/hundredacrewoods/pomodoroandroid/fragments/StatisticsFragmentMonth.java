package com.hundredacrewoods.pomodoroandroid.fragments;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.hundredacrewoods.pomodoroandroid.R;

import java.util.ArrayList;
import java.util.List;

public class StatisticsFragmentMonth extends Fragment {

    private LineChart monthLineChart;
    List<Entry> successPomo;
    List<Entry> failedPomo;
    List<String> weekLabels;

    public StatisticsFragmentMonth(){

    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_statistics_month, container, false);
        monthLineChart = rootView.findViewById(R.id.month_linechart);
        loadData();
        addWeekLabel();
        setDataOnLineChart();
        setCustomizationOnBarChart();
        return rootView;
    }

    public void loadData(){
        successPomo = new ArrayList<>();
        successPomo.add(new Entry(0f, 10f));
        successPomo.add(new Entry(1f, 2f));
        successPomo.add(new Entry(2f, 7f));
        successPomo.add(new Entry(3f, 12f));
        successPomo.add(new Entry(5f, 14f));
        successPomo.add(new Entry(6f, 4f));
        successPomo.add(new Entry(4f, 2f));

        failedPomo = new ArrayList<>();
        failedPomo.add(new Entry(0f,2f));
        failedPomo.add(new Entry(1f,5f));
        failedPomo.add(new Entry(2f,1f));
        failedPomo.add(new Entry(3f,0f));
        failedPomo.add(new Entry(4f,6f));
        failedPomo.add(new Entry(5f,3f));
        failedPomo.add(new Entry(6f,4f));
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

    public void setDataOnLineChart(){
        LineDataSet dataSet1 = new LineDataSet(successPomo, "Success Pomodoro");
        dataSet1.setColor(Color.rgb(117, 117,117));
        dataSet1.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSet1.setLineWidth(3f);
        dataSet1.setCircleRadius(10f);
        dataSet1.setCircleColor(Color.rgb(255, 193, 7));

        LineDataSet dataSet2 = new LineDataSet(failedPomo, "Failed Pomodoro");
        dataSet2.setColor(Color.rgb(117,117,117));
        dataSet2.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSet2.setLineWidth(3f);
        dataSet2.setCircleRadius(10f);
        dataSet2.setCircleColor(Color.rgb(255, 87, 34));

        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(dataSet1);
        dataSets.add(dataSet2);

        LineData data = new LineData(dataSets);
        monthLineChart.setData(data);
        monthLineChart.invalidate(); // refresh
    }

    public void setCustomizationOnBarChart(){
        monthLineChart.animateXY(3000, 3000);
        monthLineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(weekLabels));
        monthLineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        monthLineChart.getAxisRight().setEnabled(false);
        monthLineChart.getAxisLeft().setDrawGridLines(false);
        monthLineChart.getXAxis().setDrawGridLines(false);
        monthLineChart.getDescription().setEnabled(false);
    }

}
