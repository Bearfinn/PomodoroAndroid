package com.hundredacrewoods.pomodoroandroid;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

@Database(entities = {Preset.class, UserRecord.class}, version = 1)
@TypeConverters({TimestampLongConverter.class})
public abstract class PomodoroDatabase extends RoomDatabase {

    private static volatile PomodoroDatabase INSTANCE;

    public abstract PresetDao presetDao();

    public abstract UserRecordDao userRecordDao();

    public static PomodoroDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (PomodoroDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PomodoroDatabase.class, "Pomodoro.db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
