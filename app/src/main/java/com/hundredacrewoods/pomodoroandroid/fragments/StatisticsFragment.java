package com.hundredacrewoods.pomodoroandroid.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.hundredacrewoods.pomodoroandroid.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nuuneoi on 11/16/2014.
 */
@SuppressWarnings("unused")
public class StatisticsFragment extends Fragment {

    private BarChart mBarChart;

    public StatisticsFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static StatisticsFragment newInstance() {
        StatisticsFragment fragment = new StatisticsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);

        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_statistics, container, false);
        initInstances(rootView, savedInstanceState);
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, 10f));
        entries.add(new BarEntry(1f, 2f));
        entries.add(new BarEntry(2f, 7f));
        entries.add(new BarEntry(3f, 12f));
        entries.add(new BarEntry(5f, 14f));
        entries.add(new BarEntry(6f, 4f));
        entries.add(new BarEntry(4f, 2f));

        BarDataSet set = new BarDataSet(entries, "Success Pomodoro");
        set.setColors(Color.GRAY);

        //Add data, failed pomodoro
        List<BarEntry> entriesGroup2 = new ArrayList<>();
        entriesGroup2.add(new BarEntry(0f,2f));
        entriesGroup2.add(new BarEntry(1f,5f));
        entriesGroup2.add(new BarEntry(2f,1f));
        entriesGroup2.add(new BarEntry(3f,0f));
        entriesGroup2.add(new BarEntry(4f,6f));
        entriesGroup2.add(new BarEntry(5f,3f));
        entriesGroup2.add(new BarEntry(6f,4f));

        BarDataSet set2 = new BarDataSet(entriesGroup2, "Failed Pomodoro");
        set2.setColor(Color.YELLOW);

        List<String> BarEntryLabels = new ArrayList<String>();
        BarEntryLabels.add("Mon");
        BarEntryLabels.add("Tue");
        BarEntryLabels.add("Wed");
        BarEntryLabels.add("Thu");
        BarEntryLabels.add("Fri");
        BarEntryLabels.add("Sat");
        BarEntryLabels.add("Sun");

        BarData data = new BarData(set, set2);
        data.setBarWidth(0.6f); // set custom bar width
        mBarChart.setData(data);
        //chart.groupBars(0.00001f, 0.2f, 0.02f);
        //chart.setFitBars(true); // make the x-axis fit exactly all bars
        mBarChart.invalidate(); // refresh
        mBarChart.animateXY(3000,3000);

        mBarChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(BarEntryLabels));
        mBarChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mBarChart.getAxisRight().setEnabled(false);
        mBarChart.getAxisLeft().setDrawGridLines(false);
        mBarChart.getXAxis().setDrawGridLines(false);
        mBarChart.getDescription().setEnabled(false);
        //chart.getXAxis().setCenterAxisLabels(true);
        mBarChart.setDragEnabled(true);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here
    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        // Init 'View' instance(s) with rootView.findViewById here
        mBarChart = (BarChart) rootView.findViewById(R.id.chart);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /*
     * Save Instance State Here
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance State here
    }

    /*
     * Restore Instance State Here
     */
    @SuppressWarnings("UnusedParameters")
    private void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore Instance State here
    }

}
