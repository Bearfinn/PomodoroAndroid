package com.hundredacrewoods.pomodoroandroid.databases;

import android.arch.persistence.room.TypeConverter;

import java.sql.Timestamp;

/*
    This class is used to map the Timestamp type to the long type
    Because the SQLite db does not have the Timestamp type, this class must be implemented.
 */
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
