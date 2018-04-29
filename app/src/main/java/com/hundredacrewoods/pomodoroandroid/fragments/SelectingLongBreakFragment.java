package com.hundredacrewoods.pomodoroandroid.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;

import com.hundredacrewoods.pomodoroandroid.R;

public class SelectingLongBreakFragment extends DialogFragment {
    public static final String TAG = "selectingLongBreakFragment";

    private int mIntervals;

    private SelectingLongBreakListener mCallback;

    public interface SelectingLongBreakListener {
        void getSelectingInterval(int interval);
    }

    public SelectingLongBreakFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static SelectingLongBreakFragment newInstance(int intervals) {
        SelectingLongBreakFragment fragment = new SelectingLongBreakFragment();
        Bundle args = new Bundle();
        fragment.mIntervals = intervals;
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);

        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState);

    }

    @Override
    public Dialog onCreateDialog (Bundle savedInstanceState) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Long break after");

        //Set alert dialog to show list of intervals using array
        alertDialogBuilder.setSingleChoiceItems(R.array.long_break_after, mIntervals - 2
                , (dialogInterface, i) -> {
                    SelectingLongBreakListener activity =
                            (SelectingLongBreakListener) getFragmentManager().findFragmentByTag(
                                    AddingPresetFragment.TAG
                            );
                    // i + 2 because i is a position in the array and it starts at 2 intervals
                    activity.getSelectingInterval(i + 2);
                    dialogInterface.dismiss();
                });

        alertDialogBuilder.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel());

        return alertDialogBuilder.show();
    }


    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here
    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        // Init 'View' instance(s) with rootView.findViewById here

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
