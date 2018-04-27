package com.hundredacrewoods.pomodoroandroid;

import java.sql.Timestamp;

public class TimestampRange {
    private Timestamp from;
    private Timestamp to;

    public TimestampRange(Timestamp from, Timestamp to) {
        this.from = from;
        this.to = to;
    }

    public Timestamp getFrom() {
        return from;
    }

    public void setFrom(Timestamp from) {
        this.from = from;
    }

    public Timestamp getTo() {
        return to;
    }

    public void setTo(Timestamp to) {
        this.to = to;
    }
}
