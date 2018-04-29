package com.hundredacrewoods.pomodoroandroid.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
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

public class StatisticsFragmentOverall extends Fragment{

    private PieChart mPieChart;
    private ArrayList<PieEntry> input;
    private int[] MY_COLORS = {Color.rgb(255,193,7), Color.rgb(189,189,189)};

    private PomodoroViewModel mPomodoroViewModel;

    public  StatisticsFragmentOverall(){

    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mPomodoroViewModel = ((MainActivity) getActivity()).getPomodoroViewModel();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_statistics_overall, container, false);
        mPieChart = rootView.findViewById(R.id.overall_piechart);

        mPomodoroViewModel.getAllUserRecords().observe(getActivity(), userRecords -> {
            loadData(userRecords);
            addDataToPieChart();
            customizePieChart();
        });

        return rootView;
    }

    public void loadData(List<UserRecord> userRecords) {
        input = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        int sumSuccess = 0, sumFailure = 0;

        for(UserRecord userRecord: userRecords) {
            sumSuccess += userRecord.getNumSuccess();
            sumFailure += userRecord.getNumFailure();
        }

        input.add(new PieEntry((float) sumSuccess, "Successful Pomodoro"));
        input.add(new PieEntry((float) sumFailure, "Failed Pomodoro"));
    }

    public void loadData(){
        input = new ArrayList<>();
        // for value 29, it comes from count the number of success Pomodoro
        // for value 3, it comes from count the number of failed Pomodoro
        input.add(new PieEntry(29f,"Success Pomodoro"));
        input.add(new PieEntry(3f, "Failed Pomodoro"));
    }

    public void addDataToPieChart(){
        PieDataSet dataset = new PieDataSet(input, "");
        ArrayList<Integer> colors = new ArrayList<>();
        for(int c: MY_COLORS) colors.add(c);
        dataset.setColors(colors);
        PieData data = new PieData(dataset);
        data.setValueTextSize(20f);
        data.setValueFormatter(new PercentFormatter());
        mPieChart.setData(data);
        mPieChart.invalidate();
    }

    public void customizePieChart(){
        mPieChart.setHoleRadius(30);
        mPieChart.setTransparentCircleRadius(0);
        mPieChart.setUsePercentValues(true);
        mPieChart.animateXY(1500,1000);
        mPieChart.setDescription(null);
        mPieChart.setDrawEntryLabels(false);
    }
}
