package com.hundredacrewoods.pomodoroandroid.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.hundredacrewoods.pomodoroandroid.R;
import com.hundredacrewoods.pomodoroandroid.activities.MainActivity;
import com.hundredacrewoods.pomodoroandroid.databases.PomodoroViewModel;

public class SettingsFragment extends PreferenceFragmentCompat {

    public static final String PREF_NOTI = "notification_preference";

    public static final String PREF_SOUND = "sound_preference";

    public static final String PREF_VIBE = "vibration_preference";

    public static final String PREF_CLEAR_HIST = "clear_history_preference";

    private int mRingerMode;

    private SharedPreferences mSharedPreferences;

    private SwitchPreferenceCompat mVibratorPreference;

    private SwitchPreferenceCompat mNotificationPreference;

    private SwitchPreferenceCompat mSoundPreference;

    private Preference mHistoryPreference;

    private Context mContext;

    private PomodoroViewModel mPomodoroViewModel;

    public SettingsFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);

        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("Setting");
        actionBar.setDisplayHomeAsUpEnabled(false);

        mPomodoroViewModel = ((MainActivity) getActivity()).getPomodoroViewModel();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        mContext = this.getActivity().getApplicationContext();
        addPreferencesFromResource(R.xml.settings_preference);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);

        mNotificationPreference = (SwitchPreferenceCompat) findPreference(PREF_NOTI);
        mSoundPreference = (SwitchPreferenceCompat) findPreference(PREF_SOUND);
        mVibratorPreference = (SwitchPreferenceCompat) findPreference(PREF_VIBE);
        mHistoryPreference = findPreference(PREF_CLEAR_HIST);

        mHistoryPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Delete history");
                builder.setMessage("Are you sure to delete all history");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mPomodoroViewModel.deleteAllUserRecords();
                        dialogInterface.dismiss();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                builder.show();
                return true;
            }
        });

        mSharedPreferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.
                OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                boolean bool = sharedPreferences.getBoolean(s, false);
                AudioManager audioManager = (AudioManager) mContext.getSystemService(
                        Context.AUDIO_SERVICE);
                String toastText = "";
                switch (s) {
                    case PREF_NOTI :
                        if (bool) {
                            toastText = "turn on notification";
                        } else {
                            toastText = "turn off notification";
                        }
                        break;
                    case PREF_VIBE :
                        if (bool) {
                            audioManager.setRingerMode(mRingerMode);
                            toastText = "non-silent";
                        } else {
                            mRingerMode = audioManager.getRingerMode();
                            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                            toastText = "silent";
                        }
                        break;
                    case PREF_SOUND :
                        if (bool) {
                            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                            toastText = "sound";
                        } else {
                            audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                            toastText = "mute";
                        }
                        break;
                    default : break;
                }
                Toast toast = Toast.makeText(mContext, toastText, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM,
                        0, 180);
                toast.show();
            }
        });
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
//        initInstances(rootView, savedInstanceState);
//
//        return rootView;
//    }

    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here
    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        // Init 'View' instance(s) with rootView.findViewById here
        //textView = rootView.findViewById(R.id.fragment_preset_textview);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /*
     * Save Instance State Here
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance State here

    }

    /*
     * Restore Instance State Here
     */
    @SuppressWarnings("UnusedParameters")
    private void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore Instance State here
    }

}
