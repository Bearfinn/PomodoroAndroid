package com.hundredacrewoods.pomodoroandroid;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

/**
 * Created by Chatchawan Kotarasu on 4/5/2018.
 */

@Dao
public interface PresetDao {

    @Insert
    public void insertPresets(Preset... presets);

    
}
