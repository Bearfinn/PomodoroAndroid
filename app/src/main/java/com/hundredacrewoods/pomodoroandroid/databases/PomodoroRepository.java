package com.hundredacrewoods.pomodoroandroid.databases;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.hundredacrewoods.pomodoroandroid.TimestampRange;

import java.util.List;

/*
    This class is the repository class. It is a ORM or Object Relation Model
    It is used to handle the various data sources, such as, local db, remote db, etc.
 */

public class PomodoroRepository {

    private PresetDao mPresetDao; //Dao instance for Preset

    private UserRecordDao mUserRecordDao; //Dao instance for UserRecord

    private LiveData<List<Preset>> mAllPresets; //LiveData for the all presets in the list

    private LiveData<List<UserRecord>> mAllUserRecords; //LiveData for the all UserRecords in the list

    //Constructor for the Repository
    public PomodoroRepository(Application application) {
        PomodoroDatabase db = PomodoroDatabase.getInstance(application);
        mPresetDao = db.presetDao();
        mUserRecordDao = db.userRecordDao();
        mAllPresets = mPresetDao.selectAllPresets();
        mAllUserRecords = mUserRecordDao.selectAllUserRecords();
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

    public LiveData<List<UserRecord>> getAllUserRecords() {
        return mAllUserRecords;
    }

    public void deleteAllUserRecords() {
        new deleteAllUserRecordsAsyncTask(mUserRecordDao).execute();
    }

    /*
        The following static class is for AsyncTask
        Instead of manipulating the database from the main thread which is prohibited by default,
        using background thread to handle the data source is must implemented.
     */

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

    private static class deleteAllUserRecordsAsyncTask extends AsyncTask<Void, Void, Void> {
        private UserRecordDao mAsyncUserRecordDao;

        public deleteAllUserRecordsAsyncTask(UserRecordDao mAsyncUserRecordDao) {
            this.mAsyncUserRecordDao = mAsyncUserRecordDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncUserRecordDao.deleteAllUserRecords();
            return null;
        }
    }
}
