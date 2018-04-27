package com.hundredacrewoods.pomodoroandroid.databases;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.sql.Timestamp;
import java.util.List;

public class PomodoroViewModel extends AndroidViewModel {

    private PomodoroRepository mPomodoroRepository;

    private LiveData<List<Preset>> mAllPresets;

    public PomodoroViewModel (Application application) {
        super(application);
        mPomodoroRepository = new PomodoroRepository(application);
        mAllPresets = mPomodoroRepository.getAllPresets();
    }

    public LiveData<List<Preset>> getAllPresets() {
        return mAllPresets;
    }

    public void insertPreset(Preset... presets) {
        mPomodoroRepository.insertPreset(presets);
    }

    public void updatePreset(Preset... presets) {
        mPomodoroRepository.updatePreset(presets);
    }

    public void deletePreset(Integer... integers) {
        mPomodoroRepository.deletePreset(integers);
    }

    public void insertUserRecord(UserRecord... userRecords) {
        mPomodoroRepository.insertUserRecord(userRecords);
    }

    public List<UserRecord> selectUserRecords(Timestamp from, Timestamp to) {
        return mPomodoroRepository.selectUserRecords(from, to);
    }
}
