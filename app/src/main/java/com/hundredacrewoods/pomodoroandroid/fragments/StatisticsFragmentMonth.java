package com.hundredacrewoods.pomodoroandroid.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hundredacrewoods.pomodoroandroid.R;

public class StatisticsFragmentMonth extends Fragment {

    public StatisticsFragmentMonth(){

    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_statistics_month, container, false);

        
        return rootView;
    }

}
