package com.hundredacrewoods.pomodoroandroid;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4.class)
public class SimpleCreateInsertReadDBTest {
    private PresetDao mPresetDao;
    private PresetDatabase mDb;

    @Before
    public void creatDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        mDb = PresetDatabase.getInstance(context);
        mPresetDao = mDb.presetDao();
    }

    @After
    public void closeDb() throws IOException {
        mDb.close();
    }

    @Test
    public void writePresetAndReadInList() throws Exception {
        Preset preset = new Preset(
                1000, 1000, 1000, 2
        );

        mPresetDao.insertPresets(preset);

        Preset[] presets = mPresetDao.loadAllPresets();
        assertThat(presets[0], equalTo(preset));
    }
}
