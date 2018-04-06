package com.hundredacrewoods.pomodoroandroid;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface UserRecordDao {

    @Insert
    public void insertUserRecord(UserRecord... userRecords);

    @Query("DELETE FROM UserRecords")
    public void deleteAllUserRecords();

    @Query("SELECT * FROM UserRecords")
    public List<UserRecord> loadAllUserRecords();
}
