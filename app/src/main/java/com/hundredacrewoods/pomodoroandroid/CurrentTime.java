package com.hundredacrewoods.pomodoroandroid;

public class CurrentTime {
    public TimerService.Status status;
    public long time;

    public CurrentTime(TimerService.Status status, long time) {
        this.status = status;
        this.time = time;
    }
}
