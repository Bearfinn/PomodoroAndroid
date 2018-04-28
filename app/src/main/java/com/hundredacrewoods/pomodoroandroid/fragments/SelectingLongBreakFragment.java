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

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//
//        try {
//            mCallback = (OnAlertDialogPositiveButtonClicked) context;
//
//        } catch (ClassCastException e) {
//            throw new ClassCastException(context.toString() +
//                    "must implement OnAlertDialogPositiveButtonClicked");
//        }
//    }

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

        alertDialogBuilder.setSingleChoiceItems(R.array.long_break_after, mIntervals - 2
                , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SelectingLongBreakListener activity =
                        (SelectingLongBreakListener) getFragmentManager().findFragmentByTag(
                                AddingPresetFragment.TAG
                        );
                activity.getSelectingInterval(i + 2);
                dialogInterface.dismiss();
            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        return alertDialogBuilder.show();
    }


//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.dialog_fragment_save_preset_name, container,
//                false);
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
