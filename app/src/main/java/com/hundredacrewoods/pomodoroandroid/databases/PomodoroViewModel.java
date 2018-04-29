package com.hundredacrewoods.pomodoroandroid.databases;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;

import com.hundredacrewoods.pomodoroandroid.TimestampRange;

import java.util.List;


/*
    This class is the ViewModel class
    ViewModel is like a controller in the concept of MVC (Model View Controller)
    It will handle the input from user, and then update the model or data (in this case database)
    and update UI accordingly.
 */
public class PomodoroViewModel extends AndroidViewModel {

    private PomodoroRepository mPomodoroRepository; //Repository instance for background job

    private LiveData<List<Preset>> mAllPresets;

    private LiveData<List<UserRecord>> mSelectedUserRecords;

    private LiveData<List<UserRecord>> mAllUserRecords;

    private MutableLiveData<TimestampRange> filterSearch = new MutableLiveData<TimestampRange>(); //This is the filter search for query in the WHERE clauses

    public PomodoroViewModel (Application application) {
        super(application);
        mPomodoroRepository = new PomodoroRepository(application);
        mAllPresets = mPomodoroRepository.getAllPresets();
        mAllUserRecords = mPomodoroRepository.getAllUserRecords();

        //This will set the filter as the trigger. When the filter has been changed by the user's input, it will notify SelectedUserRecords
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

    public LiveData<List<UserRecord>> getAllUserRecords() {
        return mAllUserRecords;
    }

    public void setFilterSearch (TimestampRange timestampRange) {
        filterSearch.postValue(timestampRange);
    }
}
