package com.hundredacrewoods.pomodoroandroid.databases;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.hundredacrewoods.pomodoroandroid.databases.UserRecord;

import java.sql.Timestamp;
import java.util.List;

@Dao
public interface UserRecordDao {

    @Insert
    public void insertUserRecord(UserRecord... userRecords);

    @Query("DELETE FROM UserRecords")
    public void deleteAllUserRecords();

    @Query("SELECT * FROM UserRecords")
    public LiveData<List<UserRecord>> selectAllUserRecords();

    @Query("SELECT * FROM UserRecords WHERE startDateTime >= :from AND endDateTime <= :to")
    public LiveData<List<UserRecord>> selectUserRecords(Timestamp from, Timestamp to);
}
