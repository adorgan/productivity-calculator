package com.myapp.adorg.simplecalculatorv2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;


public class DeleteHistoryItemDialogActivity extends DialogFragment {

    private DeleteHistoryItemDialogActivity.OnFragmentInteractionListener mListener;

    public DeleteHistoryItemDialogActivity() {
        // Required empty public constructor
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.delete_history_item_dialog_history, null);

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
                        mListener.isDeleted(true);
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
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ClearFragment.OnFragmentInteractionListener) {
            mListener = (DeleteHistoryItemDialogActivity.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        //
        void isDeleted(boolean cleared);
    }
}
