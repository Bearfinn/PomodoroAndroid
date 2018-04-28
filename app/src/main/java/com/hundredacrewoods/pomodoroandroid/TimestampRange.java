package com.hundredacrewoods.pomodoroandroid;

import java.sql.Timestamp;
import java.util.Calendar;

public class TimestampRange {
    private Timestamp from;
    private Timestamp to;

    public final static int TODAY = 0;
    public final static int THIS_WEEK = 1;
    public final static int THIS_MONTH = 2;
    public final static long ONE_DAY_IN_MILLIS = 24 * 60 * 60 * 1000;

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

    public static TimestampRange getTimestampRange(int caseId, Timestamp current) {
        Timestamp startDate = null;
        Timestamp endDate = null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(current.getTime());

        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);

        switch (caseId) {
            case TODAY:
                startDate = new Timestamp(calendar.getTimeInMillis());
                endDate = new Timestamp(calendar.getTimeInMillis() + ONE_DAY_IN_MILLIS);
                break;
            case THIS_WEEK:
                calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
                startDate = new Timestamp(calendar.getTimeInMillis());
                calendar.add(Calendar.WEEK_OF_YEAR, 1);
                endDate = new Timestamp(calendar.getTimeInMillis());
                break;
            case THIS_MONTH:
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                startDate = new Timestamp(calendar.getTimeInMillis());
                calendar.add(Calendar.MONTH, 1);
                endDate = new Timestamp(calendar.getTimeInMillis());
                break;
            default: break;
        }
        return new TimestampRange(startDate, endDate);
    }
}
