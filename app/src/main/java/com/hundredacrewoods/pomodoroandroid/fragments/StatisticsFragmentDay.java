package com.hundredacrewoods.pomodoroandroid.fragments;

import android.arch.lifecycle.Observer;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.hundredacrewoods.pomodoroandroid.TimestampRange;
import com.hundredacrewoods.pomodoroandroid.activities.MainActivity;
import com.hundredacrewoods.pomodoroandroid.databases.PomodoroViewModel;
import com.hundredacrewoods.pomodoroandroid.databases.UserRecord;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class StatisticsFragmentDay extends Fragment {

    private LineChart mLineChart;
    List<Entry> successPomo;
    List<Entry> failedPomo;
    List<String> timeLabels;

    LineDataSet dataSet1;
    LineDataSet dataSet2;

    private PomodoroViewModel mPomodoroViewModel;

    public StatisticsFragmentDay(){

    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mPomodoroViewModel = ((MainActivity) getActivity()).getPomodoroViewModel();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_statistics_today, container, false);
        mLineChart = rootView.findViewById(R.id.month_linechart);

        mPomodoroViewModel.selectUserRecords().observe(getActivity(), userRecords -> {
            loadData(userRecords);
            //loadData();
            addTimeLabel();
            setDataOnLineChart();
            setCustomizationOnBarChart();
        });
        mPomodoroViewModel.setFilterSearch(TimestampRange.
                getTimestampRange(TimestampRange.TODAY, new Timestamp(System.currentTimeMillis())));
//        loadData();
//        addTimeLabel();
//        setDataOnLineChart();
//        setCustomizationOnBarChart();
        return rootView;
    }

    public void loadData(List<UserRecord> userRecords) {
        successPomo = new ArrayList<>();
        failedPomo = new ArrayList<>();

        for (UserRecord userRecord : userRecords) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(userRecord.getStartDateTime().getTime());
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            successPomo.add(new Entry(((float) hour), ((float) userRecord.getNumSuccess())));
            failedPomo.add(new Entry(((float) hour), ((float) userRecord.getNumFailure())));
        }
    }

//    public void loadData(){
//        successPomo = new ArrayList<>();
//        // x-axis == hour
//        // x=3 is equal to 3.00am
//        successPomo.add(new Entry(3f, 6f));
//        successPomo.add(new Entry(3f, 7f));
//        successPomo.add(new Entry(6f, 8f));
//        successPomo.add(new Entry(5f, 1f));
//        successPomo.add(new Entry(8f, 4f));
//        successPomo.add(new Entry(9f, 7f));
//        successPomo.add(new Entry(10f, 10f));
//        successPomo.add(new Entry(14f, 11f));
//        successPomo.add(new Entry(15f, 5f));
//        successPomo.add(new Entry(16f, 4f));
//        successPomo.add(new Entry(19f, 3f));
//        successPomo.add(new Entry(22f, 4f));
//        successPomo.add(new Entry(23f, 2f));
//
//
//        failedPomo = new ArrayList<>();
//        failedPomo.add(new Entry(0f,2f));
//        failedPomo.add(new Entry(1f,5f));
//        failedPomo.add(new Entry(2f,1f));
//        failedPomo.add(new Entry(3f,0f));
//        failedPomo.add(new Entry(4f,6f));
//        failedPomo.add(new Entry(5f,3f));
//        failedPomo.add(new Entry(6f,4f));
//        failedPomo.add(new Entry(10f,2f));
//        failedPomo.add(new Entry(14f,5f));
//        failedPomo.add(new Entry(15f,1f));
//        failedPomo.add(new Entry(17f,0f));
//        failedPomo.add(new Entry(19f,6f));
//        failedPomo.add(new Entry(21f,3f));
//        failedPomo.add(new Entry(22f,4f));
//    }

    public void addTimeLabel(){
        timeLabels = new ArrayList<>();
        timeLabels.add("1.00am");
        timeLabels.add("2.00am");
        timeLabels.add("3.00am");
        timeLabels.add("4.00am");
        timeLabels.add("5.00am");
        timeLabels.add("6.00am");
        timeLabels.add("7.00am");
        timeLabels.add("8.00am");
        timeLabels.add("9.00am");
        timeLabels.add("10.00am");
        timeLabels.add("11.00am");
        timeLabels.add("12.00pm");
        timeLabels.add("13.00pm");
        timeLabels.add("14.00pm");
        timeLabels.add("15.00pm");
        timeLabels.add("16.00pm");
        timeLabels.add("17.00pm");
        timeLabels.add("18.00pm");
        timeLabels.add("19.00pm");
        timeLabels.add("20.00pm");
        timeLabels.add("21.000pm");
        timeLabels.add("22.00pm");
        timeLabels.add("23.00pm");
        timeLabels.add("12.00pm");
        timeLabels.add("13.000pm");
        timeLabels.add("14.00pm");
        timeLabels.add("00.00pm");

    }

    public void setDataOnLineChart(){
        if(successPomo.size() != 0) {
            dataSet1 = new LineDataSet(successPomo, "Success Pomodoro");
            dataSet1.setColor(Color.rgb( 255,193,7));
            dataSet1.setAxisDependency(YAxis.AxisDependency.LEFT);
            dataSet1.setLineWidth(3f);
        }

        if(failedPomo.size() != 0) {
            dataSet2 = new LineDataSet(failedPomo, "Failed Pomodoro");
            dataSet2.setColor(Color.rgb(117, 117,117));
            dataSet2.setAxisDependency(YAxis.AxisDependency.LEFT);
            dataSet2.setLineWidth(3f);
        }


        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        if(dataSet1 != null) dataSets.add(dataSet1);
        if(dataSet2 != null) dataSets.add(dataSet2);

        if(dataSets.size() == 0) return;

        LineData data = new LineData(dataSets);
        mLineChart.setData(data);
        mLineChart.invalidate(); // refresh
    }

    public void setCustomizationOnBarChart(){
        mLineChart.animateXY(1000, 1000);
        mLineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(timeLabels));
        mLineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mLineChart.getAxisRight().setEnabled(false);
        mLineChart.getAxisLeft().setDrawGridLines(false);
        mLineChart.getXAxis().setDrawGridLines(false);
        mLineChart.getDescription().setEnabled(false);
        mLineChart.setVisibleXRangeMaximum(15);
        mLineChart.moveViewToX(10);
        mLineChart.setScaleEnabled(false);
    }
}
