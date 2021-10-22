package dev.peterhinch.assessmenttask2.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import dev.peterhinch.assessmenttask2.R;

// DialogFragment for search query input.
// Reference: https://developer.android.com/guide/fragments/dialogs
// Reference: https://guides.codepath.com/android/using-dialogfragment
public class SearchDialogFragment extends DialogFragment {
    public static String TAG = "SearchDialogFragment";

    private EditText searchQuery;

    // An empty constructor is required when using DialogFragment.
    public SearchDialogFragment() { }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                .setMessage(getString(R.string.searchDialog_textView_search))
                .setPositiveButton(getString(R.string.searchDialog_button_search), (dialog, which) -> {

                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                })
                .create();
    }
}