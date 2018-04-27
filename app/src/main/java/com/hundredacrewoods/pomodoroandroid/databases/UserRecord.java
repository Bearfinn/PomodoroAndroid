package com.hundredacrewoods.pomodoroandroid.databases;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.sql.Timestamp;

@Entity(tableName = "UserRecords")
public class UserRecord {

    @PrimaryKey(autoGenerate = true)
    private int recordID;

    @ColumnInfo(name = "startDateTime")
    private Timestamp startDateTime;

    @ColumnInfo(name = "endDateTime")
    private Timestamp endDateTime;

    @ColumnInfo(name = "numCycle")
    private int numCycle;

    @ColumnInfo(name = "numShort")
    private int numShort;

    @ColumnInfo(name = "numLong")
    private int numLong;

    @ColumnInfo(name = "numSuccess")
    private int numSuccess;

    @ColumnInfo(name = "numFailure")
    private int numFailure;

    public UserRecord(Timestamp startDateTime, Timestamp endDateTime) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.numCycle = 0;
        this.numShort = 0;
        this.numLong = 0;
        this.numSuccess = 0;
        this.numFailure = 0;
    }

    @Ignore
    public UserRecord(Timestamp startDateTime, Timestamp endDateTime, int numCycle, int numShort,
                      int numLong, int numSuccess, int numFailure) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.numCycle = numCycle;
        this.numShort = numShort;
        this.numLong = numLong;
        this.numSuccess = numSuccess;
        this.numFailure = numFailure;
    }

    public int getRecordID() {
        return recordID;
    }

    public void setRecordID(int recordID) {
        this.recordID = recordID;
    }

    public Timestamp getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Timestamp startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Timestamp getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Timestamp endDateTime) {
        this.endDateTime = endDateTime;
    }

    public int getNumCycle() {
        return numCycle;
    }

    public void setNumCycle(int numCycle) {
        this.numCycle = numCycle;
    }

    public int getNumShort() {
        return numShort;
    }

    public void setNumShort(int numShort) {
        this.numShort = numShort;
    }

    public int getNumLong() {
        return numLong;
    }

    public void setNumLong(int numLong) {
        this.numLong = numLong;
    }

    public int getNumSuccess() {
        return numSuccess;
    }

    public void setNumSuccess(int numSuccess) {
        this.numSuccess = numSuccess;
    }

    public int getNumFailure() {
        return numFailure;
    }

    public void setNumFailure(int numFailure) {
        this.numFailure = numFailure;
    }
}
