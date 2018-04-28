package com.hundredacrewoods.pomodoroandroid.databases;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.hundredacrewoods.pomodoroandroid.TimestampRange;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PomodoroRepository {

    private PresetDao mPresetDao;

    private UserRecordDao mUserRecordDao;

    private LiveData<List<Preset>> mAllPresets;

    public PomodoroRepository(Application application) {
        PomodoroDatabase db = PomodoroDatabase.getInstance(application);
        mPresetDao = db.presetDao();
        mUserRecordDao = db.userRecordDao();
        mAllPresets = mPresetDao.selectAllPresets();
    }

    public LiveData<List<Preset>> getAllPresets() {
        return mAllPresets;
    }

    public void insertPreset (Preset... presets) {
        new insertPresetAsyncTask(mPresetDao).execute(presets);
    }

    public void deletePreset (Integer... integers) {
        new deletePresetAsyncTask(mPresetDao).execute(integers);
    }

    public void updatePreset (Preset... presets) {
        new updatePresetAsyncTask(mPresetDao).execute(presets);
    }

    public void insertUserRecord (UserRecord... userRecords) {
        new insertUserRecordAsyncTask(mUserRecordDao).execute(userRecords);
    }

    public LiveData<List<UserRecord>> selectUserRecords (TimestampRange timestampRange) {
        return mUserRecordDao.selectUserRecords(timestampRange.getFrom(), timestampRange.getTo());
    }

    private static class deletePresetAsyncTask extends AsyncTask<Integer, Void, Void> {
        private PresetDao mAsyncPresetDao;

        deletePresetAsyncTask (PresetDao dao) {
            mAsyncPresetDao = dao;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            mAsyncPresetDao.deletePreset(integers[0].intValue());
            return null;
        }
    }

    private static class insertPresetAsyncTask extends AsyncTask<Preset, Void, Void> {

        private PresetDao mAsyncPresetDao;

        insertPresetAsyncTask (PresetDao dao) {
            mAsyncPresetDao = dao;
        }

        @Override
        protected Void doInBackground(final Preset... params) {
            mAsyncPresetDao.insertPresets(params);
            return null;
        }
    }

    private static class updatePresetAsyncTask extends AsyncTask<Preset, Void, Void> {
        private PresetDao mAsyncPresetDao;

        updatePresetAsyncTask (PresetDao dao) {
            mAsyncPresetDao = dao;
        }

        @Override
        protected Void doInBackground(final Preset... params) {
            mAsyncPresetDao.updatePreset(params);
            return null;
        }
    }

    private static class insertUserRecordAsyncTask extends AsyncTask<UserRecord, Void, Void> {
        private UserRecordDao mAsyncUserRecordDao;

        public insertUserRecordAsyncTask(UserRecordDao dao) {
            mAsyncUserRecordDao = dao;
        }

        @Override
        protected Void doInBackground(final UserRecord... userRecords) {
            mAsyncUserRecordDao.insertUserRecord(userRecords);
            return null;
        }
    }
}
