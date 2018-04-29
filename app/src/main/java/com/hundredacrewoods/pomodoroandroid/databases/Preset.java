package com.hundredacrewoods.pomodoroandroid.databases;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.sql.Time;

/**
 * Created by Chatchawan Kotarasu on 4/5/2018.
 */

@Entity(tableName = "Presets")
public class Preset {
    //Fields
    @PrimaryKey(autoGenerate = true)
    private int mPresetID;

    @ColumnInfo(name = "presetName")
    private String mPresetName;

    @ColumnInfo(name = "numShortPerLong")
    private int mNumShortPerLong;

    @ColumnInfo(name = "focusInMillis")
    private long mFocusInMillis;

    @ColumnInfo(name = "shortInMillis")
    private long mShortInMillis;

    @ColumnInfo(name = "longInMillis")
    private long mLongInMillis;

    @Ignore
    public Preset() {
    }

    public Preset(String presetName, long focusInMillis, long shortInMillis, long longInMillis,
                  int numShortPerLong) {
        this.mPresetName = presetName;
        this.mFocusInMillis = focusInMillis;
        this.mShortInMillis = shortInMillis;
        this.mLongInMillis = longInMillis;
        this.mNumShortPerLong = numShortPerLong;
    }

    public int getPresetID() {
        return mPresetID;
    }

    public void setPresetID(int mPresetID) {
        this.mPresetID = mPresetID;
    }

    public String getPresetName() {
        return mPresetName;
    }

    public void setPresetName(String mPresetName) {
        this.mPresetName = mPresetName;
    }

    public int getNumShortPerLong() {
        return mNumShortPerLong;
    }

    public void setNumShortPerLong(int mNumShortPerLong) {
        this.mNumShortPerLong = mNumShortPerLong;
    }

    public long getFocusInMillis() {
        return mFocusInMillis;
    }

    public void setFocusInMillis(long mFocusInMillis) {
        this.mFocusInMillis = mFocusInMillis;
    }

    public long getShortInMillis() {
        return mShortInMillis;
    }

    public void setShortInMillis(long mShortInMillis) {
        this.mShortInMillis = mShortInMillis;
    }

    public long getLongInMillis() {
        return mLongInMillis;
    }

    public void setLongInMillis(long mLongInMillis) {
        this.mLongInMillis = mLongInMillis;
    }

    @Override
    public String toString() {
        return this.mPresetName;
    }
}
