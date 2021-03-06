package com.hundredacrewoods.pomodoroandroid.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.hundredacrewoods.pomodoroandroid.R;
import com.hundredacrewoods.pomodoroandroid.activities.MainActivity;
import com.hundredacrewoods.pomodoroandroid.databases.Preset;
import com.hundredacrewoods.pomodoroandroid.databases.UserRecord;

import java.sql.Timestamp;


/*
    This fragment implements two listeners for retrieving the input from user
    2 inputs are preset name and interval number
 */

public class AddingPresetFragment extends Fragment
        implements EditPresetNameFragment.EditPresetNameListener,
        SelectingLongBreakFragment.SelectingLongBreakListener{

    public static final String TAG = "addingPresetFragmentTag";

    private TextView mFocusTimeTextView;

    private TextView mShortBreakTimeTextView;

    private TextView mLongBreakTimeTextView;

    private TextView mLongBreakAfterTextView;

    private SeekBar mFocusTimeSeekBar;

    private SeekBar mShortBreakTimeSeekBar;

    private SeekBar mLongBreakTimeSeekBar;

    private ConstraintLayout mLongBreakAfterContainer;

    private int mInterval;

    //It is used to check this fragment is used for adding new preset or editing selected preset
    private boolean mIsEdit;

    private Preset mPreset;

    public AddingPresetFragment () {super();}

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
        //This menu contains the save option
        inflater.inflate(R.menu.adding_preset_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                //If user click back button, it will pop the stack back to the view
                getFragmentManager().popBackStack();
                break;
            case R.id.adding_preset_save_button :
                //If user click save, it will show the alert dialog for getting preset name from user input
                //If this fragment is in editing mode, it will send the current preset name to the dialog
                //otherwise, it will send "Preset 1"
                EditPresetNameFragment editPresetNameFragment =
                        EditPresetNameFragment.newInstance(
                                mIsEdit? mPreset.getPresetName() : "Preset 1");
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

        //Set action bar
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(mIsEdit ? mPreset.getPresetName() : "New Preset");

        //If this fragment is in editing mode, it will set the progress bar and interval text view
        if(this.mIsEdit) {
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
            mLongBreakAfterTextView.setText(mInterval + " intervals");
        }

        //This will set Progress bar on change. If the progress bar has been changed, it will change the text view.
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

        //This will set Progress bar on change. If the progress bar has been changed, it will change the text view.
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

        //This will set Progress bar on change. If the progress bar has been changed, it will change the text view.
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

        //This will show the alert dialog contains list view for interval selection
        mLongBreakAfterContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectingLongBreakFragment dialogFragment = SelectingLongBreakFragment.newInstance(
                        mInterval
                );
                FragmentManager fragmentManager = AddingPresetFragment.super.getFragmentManager();
                dialogFragment.show(fragmentManager, SelectingLongBreakFragment.TAG);
            }
        });

        return rootView;
    }

    //This method is the implemented method for the interface editPresetName listener
    @Override
    public void getSavedName(String name) {
        long focusInMillis = (mFocusTimeSeekBar.getProgress() * 5 + 15) * 60 * 1000;
        long shortInMillis = (mShortBreakTimeSeekBar.getProgress() + 1) * 60 * 1000;
        long longInMillis = (mLongBreakTimeSeekBar.getProgress() * 5 + 5) * 60 * 1000;

        MainActivity mainActivity = (MainActivity) getActivity();

        if(mIsEdit) {
            mPreset.setPresetName(name);
            mPreset.setFocusInMillis(focusInMillis);
            mPreset.setShortInMillis(shortInMillis);
            mPreset.setLongInMillis(longInMillis);
            mPreset.setNumShortPerLong(mInterval);

            //This will update preset in the database
            mainActivity.getPomodoroViewModel().updatePreset(mPreset);
        } else {
            Preset preset = new Preset(name, focusInMillis
                    , shortInMillis, longInMillis, mInterval);

            //This will insert preset into the database
            mainActivity.getPomodoroViewModel().insertPreset(preset);
        }
        
        getFragmentManager().popBackStack();
    }

    @Override
    public void getSelectingInterval(int interval) {
        //Set interval
        mInterval = interval;
        mLongBreakAfterTextView.setText(interval + " intervals");
    }

    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here
        mIsEdit = mPreset.getPresetName() != null ? true : false;
        mInterval = mIsEdit ? mPreset.getNumShortPerLong() : 4;
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
        mLongBreakAfterContainer = rootView.findViewById(R.id.longBreakAfterContainer);
        mLongBreakAfterTextView = rootView.findViewById(R.id.longBreakAfterText);
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
