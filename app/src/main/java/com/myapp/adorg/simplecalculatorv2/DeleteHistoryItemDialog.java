package com.myapp.adorg.simplecalculatorv2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
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
        int resId;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            resId = R.style.MyDialogTheme;
        } else {
            resId = 0;
        }

        return new AlertDialog.Builder(getActivity(), resId)
                .setView(v)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
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

