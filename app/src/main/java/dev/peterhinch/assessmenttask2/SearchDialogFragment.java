package dev.peterhinch.assessmenttask2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class SearchDialogFragment extends DialogFragment {
    public static String TAG = "SearchDialogFragment";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                .setMessage(getString(R.string.searchDialog_textView_search))
                .setPositiveButton(getString(R.string.searchDialog_button_search), (dialog, which) -> {} )
                .create();
    }
}