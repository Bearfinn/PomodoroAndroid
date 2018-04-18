package com.hundredacrewoods.pomodoroandroid.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hundredacrewoods.pomodoroandroid.R;

public class AddingPresetFragment extends Fragment {

    private TextView mFocusTimeTextView;

    private TextView mShortBreakTimeTextView;

    private TextView mLongBreakTimeTextView;

    private SeekBar mFocusTimeSeekBar;

    private SeekBar mShortBreakTimeSeekBar;

    private SeekBar mLongBreakTimeSeekBar;

    private Button mSavePresetButton;

    public AddingPresetFragment () {
        super();
    }

    @SuppressWarnings("unused")
    public static AddingPresetFragment newInstance() {
        AddingPresetFragment fragment = new AddingPresetFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_adding_preset, container,
                false);
        initInstances(rootView, savedInstanceState);

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
                i = i * 3 + 5;
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

        mSavePresetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditPresetNameFragment editPresetNameFragment = new EditPresetNameFragment();
                FragmentManager fragmentManager = AddingPresetFragment.super.getFragmentManager();
                editPresetNameFragment.show(fragmentManager, null);
            }
        });

        return rootView;
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
        mSavePresetButton = rootView.findViewById(R.id.savePresetButton);
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
