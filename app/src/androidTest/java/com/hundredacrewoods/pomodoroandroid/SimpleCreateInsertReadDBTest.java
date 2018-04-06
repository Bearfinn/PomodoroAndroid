package com.hundredacrewoods.pomodoroandroid;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

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
        mPresetDao.deleteAllPresets();

        Preset preset = new Preset(
                1000, 1000, 1000, 2
        );

        Preset preset1 = new Preset(
                500, 250, 1000, 3
        );

        mPresetDao.insertPresets(preset);
        mPresetDao.insertPresets(preset1);

        List<Preset> presets = mPresetDao.loadAllPresets();
        assertThat(presets.size(), equalTo(2));

        Preset getPreset = presets.get(0);
        assertThat(getPreset.getFocusInMillis(), equalTo((long) 1000));
        assertThat(getPreset.getLongInMillis(), equalTo((long) 1000));
        assertThat(getPreset.getNumShortPerLong(), equalTo(2));
    }
}
