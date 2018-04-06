package com.hundredacrewoods.pomodoroandroid;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by Chatchawan Kotarasu on 4/5/2018.
 */

@Dao
public interface PresetDao {

    @Insert
    public void insertPresets(Preset... presets);

    @Query("DELETE FROM Presets")
    public void deleteAllPresets();

    @Query("SELECT * FROM Presets")
    public List<Preset> loadAllPresets();
}
