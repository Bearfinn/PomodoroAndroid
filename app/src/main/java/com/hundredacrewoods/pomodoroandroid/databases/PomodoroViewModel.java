package com.hundredacrewoods.pomodoroandroid.databases;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;

import com.hundredacrewoods.pomodoroandroid.TimestampRange;

import java.sql.Timestamp;
import java.util.List;

public class PomodoroViewModel extends AndroidViewModel {

    private PomodoroRepository mPomodoroRepository;

    private LiveData<List<Preset>> mAllPresets;

    private LiveData<List<UserRecord>> mSelectedUserRecords;

    private MutableLiveData<TimestampRange> filterSearch = new MutableLiveData<TimestampRange>();

    public PomodoroViewModel (Application application) {
        super(application);
        mPomodoroRepository = new PomodoroRepository(application);
        mAllPresets = mPomodoroRepository.getAllPresets();
        mSelectedUserRecords = Transformations.switchMap(filterSearch, filter -> mPomodoroRepository.selectUserRecords(filter));
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

    public LiveData<List<UserRecord>> selectUserRecords() {
        return mSelectedUserRecords;
    }

    public void setFilterSearch (TimestampRange timestampRange) {
        filterSearch.setValue(timestampRange);
    }
}
