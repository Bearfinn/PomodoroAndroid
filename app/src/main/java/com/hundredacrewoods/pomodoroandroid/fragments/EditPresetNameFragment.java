package com.hundredacrewoods.pomodoroandroid.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

public class EditPresetNameFragment extends DialogFragment {

    public static final String TAG = "editPresetNameFragment";

    private String mPresetName;

    //Interface for fragment communication between this fragment and AddingPresetFragment
    public interface EditPresetNameListener {
        void getSavedName(String name);
    }

    public EditPresetNameFragment() {
        super();
    }


    public static EditPresetNameFragment newInstance(String presetName) {
        EditPresetNameFragment fragment = new EditPresetNameFragment();
        Bundle args = new Bundle();
        fragment.mPresetName = presetName;
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
        //Create alertDialog builder to pre-build alert dialog
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Preset Name");

        //Create input text view ans set it into the alert dialog
        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(mPresetName);
        input.setSelection(mPresetName.length());
        alertDialogBuilder.setView(input);

        //This is for Save button. After user press this button, it will send the input to Adding preset fragment via the interface
        alertDialogBuilder.setPositiveButton("Save", (dialogInterface, i) -> {
            EditPresetNameListener activity = (EditPresetNameListener) getFragmentManager().
                    findFragmentByTag(AddingPresetFragment.TAG);
            activity.getSavedName(input.getText().toString());
        });

        //This is for cancel button.
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
