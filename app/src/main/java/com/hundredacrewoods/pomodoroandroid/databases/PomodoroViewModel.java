package com.hundredacrewoods.pomodoroandroid.databases;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

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

    public void insert(Preset... presets) {
        mPomodoroRepository.insert(presets);
    }
}
