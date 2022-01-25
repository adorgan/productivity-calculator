package com.myapp.adorg.simplecalculatorv2;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

    //Change log alert dialog

public class ChangeLog extends DialogFragment {

    public ChangeLog() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_change_log, null);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton("Got it", (dialog, which) -> Preferences.setChangeLogSeen2(getContext(), true))
                .create();
    }
}
