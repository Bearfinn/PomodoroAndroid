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

import java.util.Locale;
import java.util.Timer;

/**
 * Created by nuuneoi on 11/16/2014.
 */
@SuppressWarnings("unused")
public class TimerFragment extends Fragment {

    int shortBreakTime;
    int longBreakTime;
    int focusTime;
    int shortBreakPerLongBreak;

    public enum TimerStatus {
        FOCUS, LONG_BREAK, SHORT_BREAK
    }

    CountDownTimer countDownTimer;
    TimerStatus currentStatus;
    long currentTimeLeftInMillis;
    int shortBreakCount;

    boolean isTimerRunning;

    TextView presetNameTextView;
    TextView timerTextView;
    Button startButton;
    Button pauseButton;
    Button stopButton;

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
        focusTime = 20000;
        shortBreakTime = 5000;
        longBreakTime = 15000;
        shortBreakPerLongBreak = 1;

        shortBreakCount = 0;
        currentTimeLeftInMillis = focusTime;
        currentStatus = TimerStatus.FOCUS;

        isTimerRunning = false;
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
                startButtonPressed();
            }
        });
        pauseButton = rootView.findViewById(R.id.pause_button);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTimer();
            }
        });
        stopButton = rootView.findViewById(R.id.stop_button);
    }

    public void startButtonPressed() {
        if (isTimerRunning) {
            Log.d("startButtonPressed", "pausing timer...");
            pauseTimer();
        } else {
            Log.d("startButtonPressed", "starting timer...");
            startTimer();
        }
        isTimerRunning = !isTimerRunning;
    }

    public void pauseTimer() {
        countDownTimer.cancel();
        this.startButton.setText("Continue");
    }

    public void startTimer() {
        int secondsInFuture = 0;
        this.startButton.setText("Pause");

        countDownTimer = new CountDownTimer(currentTimeLeftInMillis, 1000) {

            public void onTick(long millisUntilFinished) {
                currentTimeLeftInMillis = millisUntilFinished;
                updateTimerText();
            }

            public void onFinish() {
                changeTimerStatus();
                timerTextView.setText(R.string.timer_text);
                startTimer();
            }
        }.start();

        updateTimerText();
    }

    public void resetTimer() {
        countDownTimer.cancel();
        setTimerStatusParams(TimerStatus.FOCUS);
        shortBreakCount = 0;
        isTimerRunning = false;
    }

    void updateTimerText() {
        long minutesLeft = (currentTimeLeftInMillis / 1000) / 60;
        long secondsLeft = (currentTimeLeftInMillis / 1000) % 60;
        timerTextView.setText(String.format(Locale.getDefault(), "%d:%02d", minutesLeft, secondsLeft));
    }

    void changeTimerStatus() {
        switch (currentStatus) {
            case FOCUS:
                if (shortBreakCount > shortBreakPerLongBreak) {
                    setTimerStatusParams(TimerStatus.LONG_BREAK);
                    shortBreakCount = 0;
                } else {
                    setTimerStatusParams(TimerStatus.SHORT_BREAK);
                    shortBreakCount++;
                }
                break;
            default:
                setTimerStatusParams(TimerStatus.FOCUS);
        }
    }

    void setTimerStatusParams(TimerStatus timerStatus) {
        switch (timerStatus) {
            case FOCUS:
                setTimerStatusParams(timerStatus, focusTime, R.string.focus_text);
                break;
            case SHORT_BREAK:
                setTimerStatusParams(timerStatus, shortBreakTime, R.string.short_break_text);
                break;
            case LONG_BREAK:
                setTimerStatusParams(timerStatus, longBreakTime, R.string.long_break_text);
                break;
        }
    }

    void setTimerStatusParams(TimerStatus timerStatus, long timeInMillis, int stringResId) {
        currentStatus = timerStatus;
        currentTimeLeftInMillis = timeInMillis;
        presetNameTextView.setText(stringResId);
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
