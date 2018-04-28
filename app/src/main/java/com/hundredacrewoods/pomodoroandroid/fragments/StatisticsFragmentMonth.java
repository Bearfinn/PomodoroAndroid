package com.hundredacrewoods.pomodoroandroid.fragments;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
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
import java.util.List;
import java.util.stream.Collectors;

public class StatisticsFragmentMonth extends Fragment {

    private BarChart mBarChart;
    List<BarEntry> successPomo;
    List<BarEntry> failedPomo;
    List<String> monthLabels;

    private PomodoroViewModel mPomodoroViewModel;

    public StatisticsFragmentMonth(){

    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mPomodoroViewModel = ((MainActivity) getActivity()).getPomodoroViewModel();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_statistics_month, container, false);
        mBarChart = rootView.findViewById(R.id.month_barchart);

        mPomodoroViewModel.selectUserRecords().observe(getActivity(), userRecords -> {
            loadData(userRecords);
            addMonthLabel();
            setDataOnBarChart();
            setCustomizationOnBarChart();
        });

        mPomodoroViewModel.setFilterSearch(TimestampRange.getTimestampRange(
                TimestampRange.THIS_MONTH, new Timestamp(System.currentTimeMillis())));

        return rootView;
    }

    public void loadData(List<UserRecord> userRecords) {
        successPomo = new ArrayList<>();
        failedPomo = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();

        userRecords.stream().collect(Collectors.groupingBy(userRecord -> {
            calendar.setTimeInMillis(userRecord.getStartDateTime().getTime());
            return calendar.get(Calendar.WEEK_OF_MONTH);
        }, Collectors.summingInt(userRecord -> userRecord.getNumSuccess())))
                .forEach((week, sumSuccess) -> successPomo.add(new BarEntry((float) week, (float) sumSuccess)));

        userRecords.stream().collect(Collectors.groupingBy(userRecord -> {
            calendar.setTimeInMillis(userRecord.getStartDateTime().getTime());
            return calendar.get(Calendar.WEEK_OF_MONTH);
        }, Collectors.summingInt(userRecord -> userRecord.getNumFailure())))
                .forEach((week, sumFailure) -> failedPomo.add(new BarEntry((float) week, (float) sumFailure)));
    }

    public void loadData(){
//        successPomo = new ArrayList<>();
//        // x-axis = month
//        // x=0 is equal to Jan
//        // x=12 is equal to Dec
//        successPomo.add(new Entry(0f, 10f));
//        successPomo.add(new Entry(1f, 2f));
//        successPomo.add(new Entry(2f, 7f));
//        successPomo.add(new Entry(3f, 12f));
//        successPomo.add(new Entry(5f, 14f));
//        successPomo.add(new Entry(6f, 4f));
//        successPomo.add(new Entry(4f, 2f));
//
//        failedPomo = new ArrayList<>();
//        failedPomo.add(new Entry(0f,2f));
//        failedPomo.add(new Entry(1f,5f));
//        failedPomo.add(new Entry(2f,1f));
//        failedPomo.add(new Entry(3f,0f));
//        failedPomo.add(new Entry(4f,6f));
//        failedPomo.add(new Entry(5f,3f));
//        failedPomo.add(new Entry(6f,4f));
    }

    public void addMonthLabel(){
        monthLabels = new ArrayList<>();
        monthLabels.add("Week 1");
        monthLabels.add("Week 2");
        monthLabels.add("Week 3");
        monthLabels.add("Week 4");
        monthLabels.add("Week 5");
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

        mBarChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(monthLabels));
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
