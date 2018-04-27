package com.hundredacrewoods.pomodoroandroid.fragments;

<<<<<<< HEAD
import android.app.ActionBar;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
=======
import android.content.Context;
>>>>>>> a113f2f1d4d17748591425cd743c0f5a56b83ff0
import android.os.Bundle;

import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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

    //region Variables

    final String LOG_TAG = "TimerFragment";

    TimerService.Status currentStatus;
    long currentTimeLeftInMillis;
    boolean isTimerRunning;

    TextView presetNameTextView;
    TextView timerStatusTextView;
    TextView timerTextView;
    Button startButton;
    Button resetButton;
    Button skipButton;

    TimerService timerService;
    boolean isBound;

    ServiceConnection mServiceConnection = new ServiceConnection() {
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

    //endregion

    class IncomingHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg) {

            Bundle bundle = msg.getData();
            Log.d(LOG_TAG, "Message received.");

            currentTimeLeftInMillis = (long) bundle.get("currentTimeLeftInMillis");
            updateTimerText();

            currentStatus = (TimerService.Status) bundle.get("currentStatus");
            switch (currentStatus) {
                case FOCUS: timerStatusTextView.setText(R.string.focus_text); break;
                case SHORT_BREAK: timerStatusTextView.setText(R.string.short_break_text); break;
                case LONG_BREAK: timerStatusTextView.setText(R.string.long_break_text); break;
            }
        }
    }

    Button mVibe;

    public TimerFragment() {
        super();
    }

    public static TimerFragment newInstance() {
        TimerFragment fragment = new TimerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    //region Overridden Functions

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

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = new Intent(getActivity(), TimerService.class);
        getActivity().bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (isBound) {
            getActivity().unbindService(mServiceConnection);
            isBound = false;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance State here
        outState.putLong("currentTimeLeftInMillis", currentTimeLeftInMillis);
        outState.putSerializable("currentStatus", currentStatus);
        outState.putBoolean("isTimerRunning", isTimerRunning);
    }

    private void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore Instance State here
        currentTimeLeftInMillis = (long) savedInstanceState.get("currentTimeLeftInMillis");
        currentStatus = (TimerService.Status) savedInstanceState.get("currentStatus");
        isTimerRunning = (boolean) savedInstanceState.get("isTimerRunning");
    }

    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here
        currentTimeLeftInMillis = 0;
        currentStatus = TimerService.Status.FOCUS;

        isTimerRunning = false;

        Messenger messenger = new Messenger(new IncomingHandler());
        Intent intent = new Intent(getActivity().getApplicationContext(), TimerService.class);
        intent.putExtra("messenger", messenger);
        getActivity().startService(intent);
    }

    private void initInstances(View rootView, Bundle savedInstanceState) {
        // Init 'View' instance(s) with rootView.findViewById here
        presetNameTextView = rootView.findViewById(R.id.presetName_textView);
        timerStatusTextView = rootView.findViewById(R.id.timerStatus_textView);
        timerTextView = rootView.findViewById(R.id.timer_textView);
        startButton = rootView.findViewById(R.id.start_button);
        if (isTimerRunning) {
            startButton.setText(R.string.start_button_pause_text);
        }
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startButtonPressed();
            }
        });
        resetButton = rootView.findViewById(R.id.pause_button);
        resetButton.setEnabled(false);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTimer();
            }
        });
        skipButton = rootView.findViewById(R.id.stop_button);
        skipButton.setEnabled(false);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skipPhase();
            }
        });
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.title_timer);
    }

    //endregion
    //region Self-defined Functions

    public void startButtonPressed() {
        timerService.startButtonPressed();
        resetButton.setEnabled(true);
        skipButton.setEnabled(true);
        if (isTimerRunning) {
            pauseTimer();
        } else {
            startTimer();
        }
        isTimerRunning = !isTimerRunning;
    }

    public void pauseTimer() {
        this.startButton.setText(R.string.start_button_resume_text);
    }

    public void startTimer() {
        this.startButton.setText(R.string.start_button_pause_text);;
    }

    public void resetTimer() {
        timerService.resetTimer();
        timerStatusTextView.setText(R.string.focus_text);
        startButton.setText(R.string.start_button_start_text);
        resetButton.setEnabled(false);
        skipButton.setEnabled(false);
        isTimerRunning = false;
    }

    public void skipPhase() {
        timerService.skipPhase();
    }

    void updateTimerText() {
        long minutesLeft = (currentTimeLeftInMillis / 1000) / 60;
        long secondsLeft = (currentTimeLeftInMillis / 1000) % 60;
        timerTextView.setText(String.format(Locale.getDefault(), "%d:%02d", minutesLeft, secondsLeft));
    }

    //endregion

}
