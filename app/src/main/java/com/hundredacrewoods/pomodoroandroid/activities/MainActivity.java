package com.hundredacrewoods.pomodoroandroid.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.hundredacrewoods.pomodoroandroid.R;
import com.hundredacrewoods.pomodoroandroid.fragments.PresetFragment;
import com.hundredacrewoods.pomodoroandroid.fragments.SettingsFragment;
import com.hundredacrewoods.pomodoroandroid.fragments.StatisticsFragment;
import com.hundredacrewoods.pomodoroandroid.fragments.TimerFragment;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (savedInstanceState == null) {
            Fragment timerFragment = new TimerFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_fragmentholder, timerFragment)
                    .commit();
        }
    }
}
