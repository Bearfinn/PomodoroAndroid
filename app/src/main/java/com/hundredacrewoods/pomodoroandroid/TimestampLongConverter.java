package com.hundredacrewoods.pomodoroandroid;

import android.arch.persistence.room.TypeConverter;

import java.sql.Timestamp;

public class TimestampLongConverter {

    @TypeConverter
    public static Timestamp fromLongtoTimestamp(Long value) {
        return value == null ? null : new Timestamp(value);
    }

    @TypeConverter
    public static Long fromTimestampToLong(Timestamp value) {
        return value == null ? null : value.getTime();
    }
}
