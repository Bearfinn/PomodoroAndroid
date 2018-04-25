package com.hundredacrewoods.pomodoroandroid;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hundredacrewoods.pomodoroandroid.fragments.TimerFragment;

import java.util.Locale;

public class TimerService extends Service {

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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int result = super.onStartCommand(intent, flags, startId);
        startTimer();
        return result;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
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
        long minutesLeft = (currentTimeLeftInMillis / 1000) / 60;
        long secondsLeft = (currentTimeLeftInMillis / 1000) % 60;
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
}
