package com.hundredacrewoods.pomodoroandroid.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hundredacrewoods.pomodoroandroid.R;

public class StatisticsFragmentDay extends Fragment {

    public StatisticsFragmentDay(){

    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_statistics_today, container, false);
    }
}
