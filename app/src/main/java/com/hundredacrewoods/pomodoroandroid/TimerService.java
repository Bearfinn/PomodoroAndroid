package com.hundredacrewoods.pomodoroandroid;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class TimerService extends Service {

    final String LOG_TAG = "TimerService";

    public enum Status {
        FOCUS, SHORT_BREAK, LONG_BREAK
    }

    int shortBreakTime;
    int longBreakTime;
    int focusTime;
    int shortBreakPerLongBreak;

    CountDownTimer countDownTimer;
    TimerService.Status currentStatus;
    Thread updateTimerThread;
    long currentTimeLeftInMillis;
    int shortBreakCount;

    boolean isTimerRunning;

    public IBinder mBinder;
    Messenger data;

    Queue<CurrentTime> currentTimeQueue;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        data = intent.getParcelableExtra("messenger");
        Log.d(LOG_TAG, "onStartCommand ran.");
        updateTimerThread = new UpdateTimerThread();
        updateTimerThread.start();
        //startForegroundService();
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
        currentStatus = TimerService.Status.FOCUS;

        isTimerRunning = false;
        currentTimeQueue = new LinkedList<>();
    }

    private void startForegroundService()
    {
        Log.d(LOG_TAG, "Start foreground service.");

        // Create notification default intent.
        Intent intent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // Create notification builder.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        // Make notification show big text.
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle("Music player implemented by foreground service.");
        bigTextStyle.bigText("Android foreground service is a android service which can run in foreground always, it can be controlled by user via notification.");
        // Set big text style.
        builder.setStyle(bigTextStyle);

        builder.setWhen(System.currentTimeMillis());
        builder.setSmallIcon(R.mipmap.ic_launcher);
        Bitmap largeIconBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground);
        builder.setLargeIcon(largeIconBitmap);
        // Make the notification max priority.
        builder.setPriority(Notification.PRIORITY_MAX);
        // Make head-up notification.
        builder.setFullScreenIntent(pendingIntent, true);

        // Add Play button intent in notification.
        Intent playIntent = new Intent(this, TimerService.class);
        playIntent.setAction("ACTION_PLAY");
        PendingIntent pendingPlayIntent = PendingIntent.getService(this, 0, playIntent, 0);
        NotificationCompat.Action playAction = new NotificationCompat.Action(android.R.drawable.ic_media_play, "Play", pendingPlayIntent);
        builder.addAction(playAction);

        // Add Pause button intent in notification.
        Intent pauseIntent = new Intent(this, TimerService.class);
        pauseIntent.setAction("ACTION_PAUSE");
        PendingIntent pendingPrevIntent = PendingIntent.getService(this, 0, pauseIntent, 0);
        NotificationCompat.Action prevAction = new NotificationCompat.Action(android.R.drawable.ic_media_pause, "Pause", pendingPrevIntent);
        builder.addAction(prevAction);

        // Build the notification.
        Notification notification = builder.build();

        // Start foreground service.
        startForeground(1, notification);
    }

    public void startButtonPressed() {
        if (isTimerRunning) {
            Log.d("startButtonPressed", "Pausing timer...");
            pauseTimer();
        } else {
            Log.d("startButtonPressed", "Starting timer...");
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
                updateTimerText();
                changeTimerStatus();
                startTimer();
            }
        }.start();
    }

    public void resetTimer() {
        countDownTimer.cancel();
        setTimerStatusParams(TimerService.Status.FOCUS);
        shortBreakCount = 0;
        isTimerRunning = false;
    }

    public void skipPhase() {
        countDownTimer.cancel();
        changeTimerStatus();
        startTimer();
    }

    void updateTimerText() {
        currentTimeQueue.add(new CurrentTime(currentStatus, currentTimeLeftInMillis));
    }

    void changeTimerStatus() {
        switch (currentStatus) {
            case FOCUS:
                if (shortBreakCount > shortBreakPerLongBreak) {
                    setTimerStatusParams(TimerService.Status.LONG_BREAK);
                    shortBreakCount = 0;
                } else {
                    setTimerStatusParams(TimerService.Status.SHORT_BREAK);
                    shortBreakCount++;
                }
                break;
            default:
                setTimerStatusParams(TimerService.Status.FOCUS);
        }
    }

    void setTimerStatusParams(TimerService.Status timerStatus) {
        switch (timerStatus) {
            case FOCUS:
                setTimerStatusParams(timerStatus, focusTime);
                break;
            case SHORT_BREAK:
                setTimerStatusParams(timerStatus, shortBreakTime);
                break;
            case LONG_BREAK:
                setTimerStatusParams(timerStatus, longBreakTime);
                break;
        }
    }

    void setTimerStatusParams(TimerService.Status timerStatus, long timeInMillis) {
        currentStatus = timerStatus;
        currentTimeLeftInMillis = timeInMillis;
        updateTimerText();
    }

    class UpdateTimerThread extends Thread {
        @Override
        public void run() {
            Log.d(LOG_TAG, "UpdateTimerThread called.");
            if (!isInterrupted()) {
                while (true) {
                    Message msg = Message.obtain();
                    Bundle bundle = new Bundle();
                    bundle.putLong("currentTimeLeftInMillis", currentTimeLeftInMillis);
                    bundle.putSerializable("currentStatus", currentStatus);
                    msg.setData(bundle);
                    try {
                        data.send(msg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    SystemClock.sleep(1000);
                }
            }
        }
    }
}
