package com.hundredacrewoods.pomodoroandroid.databases;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

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

    @Query("DELETE FROM Presets WHERE mPresetID = :presetID")
    public void deletePreset(int presetID);

    @Update
    public void updatePreset(Preset... preset);

    @Query("SELECT * FROM Presets")
    public LiveData<List<Preset>> selectAllPresets();
}
