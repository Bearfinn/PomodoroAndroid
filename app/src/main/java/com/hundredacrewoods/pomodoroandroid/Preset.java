package com.hundredacrewoods.pomodoroandroid;

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
    private int presetID;

    @ColumnInfo(name = "numShortPerLong")
    private int numShortPerLong;

    @ColumnInfo(name = "focusInMillis")
    private long focusInMillis;

    @ColumnInfo(name = "shortInMillis")
    private long shortInMillis;

    @ColumnInfo(name = "longInMillis")
    private long longInMillis;

    @Ignore
    public Preset() {
    }

    public Preset(long focusInMillis, long shortInMillis, long longInMillis, int numShortPerLong) {
        this.focusInMillis = focusInMillis;
        this.shortInMillis = shortInMillis;
        this.longInMillis = longInMillis;
        this.numShortPerLong = numShortPerLong;
    }

    public int getPresetID() {
        return presetID;
    }

    public void setPresetID(int presetID) {
        this.presetID = presetID;
    }

    public int getNumShortPerLong() {
        return numShortPerLong;
    }

    public void setNumShortPerLong(int numShortPerLong) {
        this.numShortPerLong = numShortPerLong;
    }

    public long getFocusInMillis() {
        return focusInMillis;
    }

    public void setFocusInMillis(long focusInMillis) {
        this.focusInMillis = focusInMillis;
    }

    public long getShortInMillis() {
        return shortInMillis;
    }

    public void setShortInMillis(long shortInMillis) {
        this.shortInMillis = shortInMillis;
    }

    public long getLongInMillis() {
        return longInMillis;
    }

    public void setLongInMillis(long longInMillis) {
        this.longInMillis = longInMillis;
    }
}
