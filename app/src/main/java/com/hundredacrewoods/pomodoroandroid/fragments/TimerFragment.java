package com.hundredacrewoods.pomodoroandroid.fragments;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hundredacrewoods.pomodoroandroid.R;

/**
 * Created by nuuneoi on 11/16/2014.
 */
@SuppressWarnings("unused")
public class TimerFragment extends Fragment {

    int shortBreakTime;
    int longBreakTime;
    int focusTime;
    int currentStatus;
    int shortBreakCount;
    int shortBreakPerLongBreak;
    TextView presetNameTextView;
    TextView timerTextView;
    Button startButton;

    public TimerFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static TimerFragment newInstance() {
        TimerFragment fragment = new TimerFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_timer, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here
        focusTime = 20;
        shortBreakTime = 5;
        longBreakTime = 15;
        shortBreakCount = 0;
        currentStatus = 0;
        shortBreakPerLongBreak = 1;
    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        // Init 'View' instance(s) with rootView.findViewById here
        presetNameTextView = rootView.findViewById(R.id.presetName_textView);
        timerTextView = rootView.findViewById(R.id.timer_textView);
        startButton = rootView.findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTimer();
            }
        });
    }

    public void startTimer() {
        Log.i("Timer", "Button clicked!");
        int secondsInFuture = 0;
        switch (currentStatus) {
            case 0:
                secondsInFuture = focusTime;
                presetNameTextView.setText(R.string.focus_text);
                break;
            case 1:
                secondsInFuture = shortBreakTime;
                presetNameTextView.setText(R.string.short_break_text);
                break;
            case 2:
                secondsInFuture = longBreakTime;
                presetNameTextView.setText(R.string.long_break_text);
                break;
            default:
                break;
        }
        new CountDownTimer(secondsInFuture * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                long minutesLeft = (millisUntilFinished / 1000) / 60;
                long secondsLeft = (millisUntilFinished / 1000) % 60;
                String timeLeft = minutesLeft + ":" + secondsLeft;
                timerTextView.setText(timeLeft);
            }

            public void onFinish() {
                switch (currentStatus) {
                    case 0:
                        if (shortBreakCount > shortBreakPerLongBreak) {
                            currentStatus = 2;
                            shortBreakCount = 0;
                        } else {
                            currentStatus = 1;
                            shortBreakCount++;
                        }
                        startTimer();
                        break;
                    default:
                        currentStatus = 0;
                        startTimer();
                }
                timerTextView.setText(R.string.timer_text);
            }
        }.start();
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
