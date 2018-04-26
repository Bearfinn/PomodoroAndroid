package com.hundredacrewoods.pomodoroandroid.databases;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class PomodoroRepository {

    private PresetDao mPresetDao;

//    private UserRecordDao mUserRecordDao;

    private LiveData<List<Preset>> mAllPresets;

//    private LiveData<List<UserRecord>> mAllUserRecords;

    public PomodoroRepository(Application application) {
        PomodoroDatabase db = PomodoroDatabase.getInstance(application);
        mPresetDao = db.presetDao();
//        mUserRecordDao = db.userRecordDao();
        mAllPresets = mPresetDao.selectAllPresets();
    }

    public LiveData<List<Preset>> getAllPresets() {
        return mAllPresets;
    }

    public void insert (Preset... presets) {
        new insertAsyncTask(mPresetDao).execute(presets);
    }

    public void delete (Integer... integers) {
        new deleteAsyncTask(mPresetDao).execute(integers);
    }

    public void update (Preset... presets) {
        new updateAsyncTask(mPresetDao).execute(presets);
    }

    private static class deleteAsyncTask extends AsyncTask<Integer, Void, Void> {
        private PresetDao mAsyncPresetDao;

        deleteAsyncTask (PresetDao dao) {
            mAsyncPresetDao = dao;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            mAsyncPresetDao.deletePreset(integers[0].intValue());
            return null;
        }
    }

    private static class insertAsyncTask extends AsyncTask<Preset, Void, Void> {

        private PresetDao mAsyncPresetDao;

        insertAsyncTask (PresetDao dao) {
            mAsyncPresetDao = dao;
        }

        @Override
        protected Void doInBackground(final Preset... params) {
            mAsyncPresetDao.insertPresets(params);
            return null;
        }
    }

    private static class updateAsyncTask extends AsyncTask<Preset, Void, Void> {
        private PresetDao mAsyncPresetDao;

        updateAsyncTask (PresetDao dao) {
            mAsyncPresetDao = dao;
        }

        @Override
        protected Void doInBackground(final Preset... params) {
            mAsyncPresetDao.updatePreset(params);
            return null;
        }
    }
}
