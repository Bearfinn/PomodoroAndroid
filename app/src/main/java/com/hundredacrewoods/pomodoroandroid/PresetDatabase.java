package com.hundredacrewoods.pomodoroandroid;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by Chatchawan Kotarasu on 4/5/2018.
 */

@Database(entities = {Preset.class}, version = 1)
public abstract class PresetDatabase extends RoomDatabase{

    private static volatile PresetDatabase INSTANCE;

    public abstract PresetDao presetDao();

    public static PresetDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (PresetDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PresetDatabase.class, "Preset.db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
