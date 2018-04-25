package com.hundredacrewoods.pomodoroandroid.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hundredacrewoods.pomodoroandroid.R;
import com.hundredacrewoods.pomodoroandroid.activities.MainActivity;
import com.hundredacrewoods.pomodoroandroid.databases.Preset;

public class AddingPresetFragment extends Fragment
        implements EditPresetNameFragment.EditPresetNameListener{

    public static final String TAG = "addingPresetFragmentTag";

    private TextView mFocusTimeTextView;

    private TextView mShortBreakTimeTextView;

    private TextView mLongBreakTimeTextView;

    private SeekBar mFocusTimeSeekBar;

    private SeekBar mShortBreakTimeSeekBar;

    private SeekBar mLongBreakTimeSeekBar;

    private Preset mPreset;

    public AddingPresetFragment () {super();}


    @SuppressWarnings("unused")
    public static AddingPresetFragment newInstance(Preset preset) {
        AddingPresetFragment fragment = new AddingPresetFragment();
        Bundle args = new Bundle();
        fragment.mPreset = preset;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);

        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState);

        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.adding_preset_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                getFragmentManager().popBackStack();
                break;
            case R.id.adding_preset_save_button :
                EditPresetNameFragment editPresetNameFragment = new EditPresetNameFragment();
                FragmentManager fragmentManager = AddingPresetFragment.super.getFragmentManager();
                editPresetNameFragment.show(fragmentManager, EditPresetNameFragment.TAG);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_adding_preset, container,
                false);
        initInstances(rootView, savedInstanceState);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(mPreset.getPresetName() != null ? mPreset.getPresetName() : "New Preset");


        boolean isEditing = false;
        if(mPreset.getPresetName() != null) isEditing = true;

        if(isEditing) {
            int focusMinute = (int) ((mPreset.getFocusInMillis() / 60) / 1000);
            int longMinute = (int) ((mPreset.getLongInMillis() / 60) / 1000);
            int shortMinute = (int) ((mPreset.getShortInMillis() / 60) / 1000);

            int focusProgress = (focusMinute - 15) / 5;
            int longProgress = (longMinute - 5) / 5;
            int shortProgress = shortMinute - 1;

            mFocusTimeSeekBar.setProgress(focusProgress);
            mShortBreakTimeSeekBar.setProgress(shortProgress);
            mLongBreakTimeSeekBar.setProgress(longProgress);

            mFocusTimeTextView.setText(focusMinute + "minutes");
            mShortBreakTimeTextView.setText(shortMinute + "minutes");
            mLongBreakTimeTextView.setText(longMinute + "minutes");
        }

        mFocusTimeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                i = i * 5 + 15;
                String text = String.valueOf(i) + " minutes";
                mFocusTimeTextView.setText(text);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mShortBreakTimeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                i = i + 1;
                String text = String.valueOf(i) + " minutes";
                mShortBreakTimeTextView.setText(text);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mLongBreakTimeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                i = i * 5 + 5;
                String text = String.valueOf(i) + " minutes";
                mLongBreakTimeTextView.setText(text);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return rootView;
    }

    public void getSavedName(String name) {
        long focusInMillis = (mFocusTimeSeekBar.getProgress() * 5 + 15) * 60 * 1000;
        long shortInMillis = (mShortBreakTimeSeekBar.getProgress() + 1) * 60 * 1000;
        long longInMillis = (mLongBreakTimeSeekBar.getProgress() * 5 + 5) * 60 * 1000;

        MainActivity mainActivity = (MainActivity) getActivity();

        Preset preset = new Preset(name, focusInMillis
                , shortInMillis, longInMillis,3);

        mainActivity.getPomodoroViewModel().insert(preset);
        getFragmentManager().popBackStack();
    }

    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here

    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        // Init 'View' instance(s) with rootView.findViewById here
        mFocusTimeSeekBar = rootView.findViewById(R.id.focusTimeSlider);
        mFocusTimeTextView = rootView.findViewById(R.id.focusTimeText);
        mShortBreakTimeSeekBar = rootView.findViewById(R.id.shortBreakTimeSlider);
        mShortBreakTimeTextView = rootView.findViewById(R.id.shortBreakTimeText);
        mLongBreakTimeSeekBar = rootView.findViewById(R.id.longBreakTimeSlider);
        mLongBreakTimeTextView = rootView.findViewById(R.id.longBreakTimeText);
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
