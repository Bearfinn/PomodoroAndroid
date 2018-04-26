package com.hundredacrewoods.pomodoroandroid.fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hundredacrewoods.pomodoroandroid.R;
import com.hundredacrewoods.pomodoroandroid.TimerService;

import java.util.Locale;

@SuppressWarnings("unused")
public class TimerFragment extends Fragment {

    final String LOG_TAG = "TimerFragment";

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
    Button resetButton;
    Button skipButton;

    TimerService timerService;
    boolean isBound;
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(LOG_TAG, "Service connected.");
            TimerService.TimerServiceBinder binder = (TimerService.TimerServiceBinder) iBinder;
            timerService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(LOG_TAG, "Service disconnected.");
            timerService = null;
            isBound = false;
        }
    };

    public TimerFragment() {
        super();
    }

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

    private void initInstances(View rootView, Bundle savedInstanceState) {
        // Init 'View' instance(s) with rootView.findViewById here
        presetNameTextView = rootView.findViewById(R.id.presetName_textView);
        timerTextView = rootView.findViewById(R.id.timer_textView);
        startButton = rootView.findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timerService.startButtonPressed();
            }
        });
        resetButton = rootView.findViewById(R.id.pause_button);
        resetButton.setEnabled(false);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timerService.resetTimer();
            }
        });
        skipButton = rootView.findViewById(R.id.stop_button);
        skipButton.setEnabled(false);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timerService.skipPhase();
            }
        });
    }

    public void startButtonPressed() {
        resetButton.setEnabled(true);
        skipButton.setEnabled(true);
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
        this.startButton.setText(R.string.start_button_resume_text);
    }

    public void startTimer() {
        this.startButton.setText(R.string.start_button_pause_text);
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
        resetButton.setEnabled(false);
        skipButton.setEnabled(false);
    }

    public void skipPhase() {
        countDownTimer.cancel();
        changeTimerStatus();
        startTimer();
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
        startButton.setText(R.string.start_button_start_text);
        updateTimerText();
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = new Intent(getActivity(), TimerService.class);
        getActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
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
        outState.putInt("shortBreakCount", shortBreakCount);
        outState.putLong("currentTimeLeftInMillis", currentTimeLeftInMillis);
        outState.putSerializable("currentStatus", currentStatus);
        outState.putBoolean("isTimerRunning", isTimerRunning);
    }

    /*
     * Restore Instance State Here
     */
    @SuppressWarnings("UnusedParameters")
    private void onRestoreInstanceState(Bundle savedInstanceState) {
        focusTime = 20000;
        shortBreakTime = 5000;
        longBreakTime = 15000;
        shortBreakPerLongBreak = 1;

        // Restore Instance State here
        shortBreakCount = (int) savedInstanceState.get("shortBreakCount");
        currentTimeLeftInMillis = (long) savedInstanceState.get("currentTimeLeftInMillis");
        currentStatus = (TimerStatus) savedInstanceState.get("currentStatus");
        isTimerRunning = (boolean) savedInstanceState.get("isTimerRunning");
    }

}
