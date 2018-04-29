package com.hundredacrewoods.pomodoroandroid.fragments;

import android.arch.lifecycle.Observer;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Handler;
import android.os.Parcel;
import android.os.SystemClock;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import android.widget.TextView;

import com.hundredacrewoods.pomodoroandroid.R;
import com.hundredacrewoods.pomodoroandroid.TimerService;
import com.hundredacrewoods.pomodoroandroid.activities.MainActivity;
import com.hundredacrewoods.pomodoroandroid.databases.PomodoroViewModel;
import com.hundredacrewoods.pomodoroandroid.databases.Preset;
import com.hundredacrewoods.pomodoroandroid.databases.UserRecord;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
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

    boolean isJustStarted;
    boolean isPausedBeforeBreak;
    boolean isAfterStatusChangeToBreak;
    Timestamp startDateTime, endDateTime;
    int cycleCount, shortBreakCount;
    int successCount, failureCount;

    Preset preset;
    TimerService timerService;
    boolean isBound;

    private PomodoroViewModel mPomodoroViewModel;

    /**
     * Create connection to TimerService
     */
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

    /**
     * Handle the data sent from TimerService
     */
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
                case FOCUS: {
                    isAfterStatusChangeToBreak = true;
                    timerStatusTextView.setText(R.string.focus_text);
                    break;
                }
                case SHORT_BREAK: {
                    if (isAfterStatusChangeToBreak) {
                        Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator.vibrate(1000);

                        try {
                            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                            Ringtone r = RingtoneManager.getRingtone(getActivity().getApplicationContext(), notification);
                            r.play();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (isPausedBeforeBreak) {
                            failureCount++;
                            isPausedBeforeBreak = false;
                        } else {
                            successCount++;
                        }
                        isAfterStatusChangeToBreak = false;
                    }
                    timerStatusTextView.setText(R.string.short_break_text);
                    break;
                }
                case LONG_BREAK: timerStatusTextView.setText(R.string.long_break_text); break;
            }
        }
    }

    public static TimerFragment newInstance() {
        TimerFragment fragment = new TimerFragment();
        Bundle args = new Bundle();
        args.putLong("currentTimeLeftInMillis", 20000);
        args.putSerializable("currentStatus", TimerService.Status.FOCUS);
        args.putBoolean("isTimerRunning", false);
        fragment.setArguments(args);
        return fragment;
    }

    public static TimerFragment newInstance(long currentTimeLeftInMillis, TimerService.Status currentStatus, boolean isTimerRunning) {
        TimerFragment fragment = new TimerFragment();
        Bundle args = new Bundle();
        args.putLong("currentTimeLeftInMillis", currentTimeLeftInMillis);
        args.putSerializable("currentStatus", currentStatus);
        args.putBoolean("isTimerRunning", isTimerRunning);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }
    }

    //region Overridden Functions

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
            currentTimeLeftInMillis = getArguments().getLong("currentTimeLeftInMillis");
            currentStatus = (TimerService.Status) getArguments().getSerializable("currentStatus");
            isTimerRunning = getArguments().getBoolean("isTimerRunning");
        }
        isTimerRunning = ((MainActivity) getActivity()).getTimerRunning();
        mPomodoroViewModel = ((MainActivity) getActivity()).getPomodoroViewModel();
        setHasOptionsMenu(true);
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
        ((MainActivity) getActivity()).setTimerRunning(isTimerRunning);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.change_preset_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.change_preset_button:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Select Preset");
                List<Preset> presets = new ArrayList<Preset>();

                ArrayAdapter adapter = new ArrayAdapter<Preset>(getContext(), android.R.layout.simple_list_item_single_choice, presets);
//                PresetTimerAdapter adapter = new PresetTimerAdapter(getContext());
                mPomodoroViewModel.getAllPresets().observe(getActivity(), new Observer<List<Preset>>() {
                    @Override
                    public void onChanged(@Nullable List<Preset> presets) {
                        adapter.clear();
                        adapter.addAll(presets);
                    }
                });
                builder.setSingleChoiceItems( adapter, 1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        preset = presets.get(i);
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here
        currentTimeLeftInMillis = 0;
        currentStatus = TimerService.Status.FOCUS;

        //isTimerRunning = false;
        isPausedBeforeBreak = false;
        isJustStarted = true;
        successCount = 0;
        failureCount = 0;

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
        if (isTimerRunning) {
            startButton.setText(R.string.start_button_pause_text);
            resetButton.setEnabled(true);
            skipButton.setEnabled(true);
        }
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.title_timer);
    }

    //endregion
    //region Self-defined Functions

    public void startButtonPressed() {
        if (isJustStarted) {
            startDateTime = new Timestamp(System.currentTimeMillis());
            isJustStarted = false;
        }
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
        if (currentStatus == TimerService.Status.FOCUS) {
            isPausedBeforeBreak = true;
        }
        this.startButton.setText(R.string.start_button_resume_text);
    }

    public void startTimer() {
        this.startButton.setText(R.string.start_button_pause_text);;
    }

    public void resetTimer() {
        endDateTime = new Timestamp(System.currentTimeMillis());
        UserRecord userRecord = new UserRecord(
                startDateTime,
                endDateTime,
                0,
                0,
                0,
                successCount,
                failureCount
        );
        mPomodoroViewModel.insertUserRecord(userRecord);

        isJustStarted = true;

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
