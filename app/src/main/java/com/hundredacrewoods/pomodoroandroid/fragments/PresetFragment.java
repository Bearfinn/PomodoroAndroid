package com.hundredacrewoods.pomodoroandroid.fragments;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hundredacrewoods.pomodoroandroid.R;
import com.hundredacrewoods.pomodoroandroid.activities.MainActivity;
import com.hundredacrewoods.pomodoroandroid.adapter.PresetAdapter;
import com.hundredacrewoods.pomodoroandroid.databases.PomodoroViewModel;
import com.hundredacrewoods.pomodoroandroid.databases.Preset;

import java.util.List;

/**
 * Created by nuuneoi on 11/16/2014.
 */
@SuppressWarnings("unused")
public class PresetFragment extends Fragment {

    private FloatingActionButton mFloatingActionButton;

    private RecyclerView mRecyclerView;

    private RecyclerView.LayoutManager mLayoutManager;

    private RecyclerView.Adapter mAdapter;

    private PomodoroViewModel mPomodoroViewModel;

    public PresetFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static PresetFragment newInstance() {
        PresetFragment fragment = new PresetFragment();
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_preset, container, false);
        initInstances(rootView, savedInstanceState);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Preset preset = new Preset();
                Fragment addingPresetFragment = AddingPresetFragment.newInstance(preset);
                FragmentManager fragmentManager = PresetFragment.super.getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_fragmentholder,
                        addingPresetFragment, AddingPresetFragment.TAG);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        mRecyclerView.setHasFixedSize(true);
        final PresetAdapter adapter = new PresetAdapter(getContext());
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        MainActivity activity = (MainActivity) getActivity();
        mPomodoroViewModel = activity.getPomodoroViewModel();

        mPomodoroViewModel.getAllPresets().observe(getActivity(), new Observer<List<Preset>>() {
            @Override
            public void onChanged(@Nullable List<Preset> presets) {
                adapter.setPresets(presets);
            }
        });

        return rootView;
    }

    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here
    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        // Init 'View' instance(s) with rootView.findViewById here
        mFloatingActionButton = rootView.findViewById(R.id.fragment_preset_add_button);
        mRecyclerView = rootView.findViewById(R.id.recycler_view);
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
