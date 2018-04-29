package com.hundredacrewoods.pomodoroandroid.activities;

import android.app.NotificationManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;

import com.hundredacrewoods.pomodoroandroid.R;
import com.hundredacrewoods.pomodoroandroid.databases.PomodoroViewModel;
import com.hundredacrewoods.pomodoroandroid.fragments.PresetFragment;
import com.hundredacrewoods.pomodoroandroid.fragments.SettingsFragment;
import com.hundredacrewoods.pomodoroandroid.fragments.StatisticsFragment;
import com.hundredacrewoods.pomodoroandroid.fragments.TimerFragment;

public class MainActivity extends AppCompatActivity {

    private PomodoroViewModel mPomodoroViewModel;

    public PomodoroViewModel getPomodoroViewModel() {
        return mPomodoroViewModel;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
                switch (item.getItemId()) {
                    case R.id.navigation_timer:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame_fragmentholder, new TimerFragment())
                                .commit();
                        return true;
                    case R.id.navigation_preset:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame_fragmentholder, new PresetFragment())
                                .commit();
                        return true;
                    case R.id.navigation_statistics:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame_fragmentholder, new StatisticsFragment())
                                .commit();
                        return true;
                    case R.id.navigation_settings:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame_fragmentholder, new SettingsFragment())
                                .commit();
                        return true;
                }
                return false;
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(
                NOTIFICATION_SERVICE);

        if (!notificationManager.isNotificationPolicyAccessGranted()) {
            Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            startActivity(intent);
        }

        BottomNavigationView navigation = findViewById(R.id.navigation);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (savedInstanceState == null) {
            Fragment timerFragment = new TimerFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_fragmentholder, timerFragment)
                    .commit();
        }

        mPomodoroViewModel = ViewModelProviders.of(this).get(PomodoroViewModel.class);
        PreferenceManager.setDefaultValues(this, R.xml.settings_preference, false);

    }
}
