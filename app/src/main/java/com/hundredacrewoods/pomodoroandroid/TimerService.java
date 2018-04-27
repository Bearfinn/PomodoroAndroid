package com.hundredacrewoods.pomodoroandroid;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hundredacrewoods.pomodoroandroid.fragments.TimerFragment;

import java.util.Locale;

public class TimerService extends Service {

    final String LOG_TAG = "TimerService";

    int shortBreakTime;
    int longBreakTime;
    int focusTime;
    int shortBreakPerLongBreak;

    public enum TimerStatus {
        FOCUS, LONG_BREAK, SHORT_BREAK
    }

    CountDownTimer countDownTimer;
    TimerStatus currentStatus;
    Thread updateTimerThread;
    long currentTimeLeftInMillis;
    int shortBreakCount;

    boolean isTimerRunning;

    public IBinder mBinder;
    Messenger data;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        data = intent.getParcelableExtra("messenger");
        Log.d(LOG_TAG, "onStartCommand ran.");
        updateTimerThread = new UpdateTimerThread();
        startTimer();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBinder = new TimerServiceBinder();
        init();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class TimerServiceBinder extends Binder {
        public TimerService getService() {
            return TimerService.this;
        }
    }

    @SuppressWarnings("unused")
    private void init() {
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
    }

    public void startTimer() {
        countDownTimer = new CountDownTimer(currentTimeLeftInMillis, 1000) {

            public void onTick(long millisUntilFinished) {
                currentTimeLeftInMillis = millisUntilFinished;
                Log.i("TimerService",currentTimeLeftInMillis / 1000 + " seconds left");
                updateTimerText();
            }

            public void onFinish() {
                changeTimerStatus();
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

    public void skipPhase() {
        countDownTimer.cancel();
        changeTimerStatus();
        startTimer();
    }

    void updateTimerText() {
        updateTimerThread.start();
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
        updateTimerText();
    }

    String[] items = {"Lorem", "Ipsum", "Dolor", "Sit", "Amet"};

    class UpdateTimerThread extends Thread {
        @Override
        public void run() {
            Log.d(LOG_TAG, "UpdateTimerThread called.");
            if (!isInterrupted()) {

                Message msg = Message.obtain();
                Bundle bundle = new Bundle();
                bundle.putLong("currentTimeLeftInMillis", currentTimeLeftInMillis);
                msg.setData(bundle);
                try {
                    data.send(msg);
                    Log.d(LOG_TAG, "Message sent back to TimerFragment.");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                SystemClock.sleep(1000);
            }
        }
    }
}
