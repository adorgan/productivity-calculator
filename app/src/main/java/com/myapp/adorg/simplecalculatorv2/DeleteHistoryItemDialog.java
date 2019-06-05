package com.myapp.adorg.simplecalculatorv2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

public class DeleteHistoryItemDialog extends DialogFragment {

    public static final String EXTRA_DELETED =
            "com.adorgan.therapyproductivitycalculator.deleted";

    public DeleteHistoryItemDialog() {
        // Required empty public constructor
    }

    public static DeleteHistoryItemDialog newInstance() {
        Bundle args = new Bundle();
        DeleteHistoryItemDialog fragment = new DeleteHistoryItemDialog();
        fragment.setArguments(args);
        return fragment;
    }


    private void sendTimeResult(int resultCode, boolean isDeleted){
        if(getTargetFragment()==null){
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DELETED, isDeleted);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_history_delete_item, null);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setPositiveButton("delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendTimeResult(Activity.RESULT_OK, true);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                })
                .create();
    }



}

