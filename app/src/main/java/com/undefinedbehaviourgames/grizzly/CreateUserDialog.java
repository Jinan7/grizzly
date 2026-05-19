package com.undefinedbehaviourgames.grizzly;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;


public class CreateUserDialog extends DialogFragment {

    public static final String EXTRA_CANCEL_SIGNIN = "com.undefinedbehaviourgames.grizzly.cancel_sign_in";
    public static CreateUserDialog newInstance(){

        return new CreateUserDialog();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View v = getLayoutInflater().inflate(R.layout.creating_user_dialog, null, false);
        return new MaterialAlertDialogBuilder(getContext())
                .setView(v)
                .setTitle("Signing In...")
                .setCancelable(false)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancel(Activity.RESULT_OK);
                    }
                })
                .create();
    }

    public void cancel(int resultCode) {

        Intent intent = new Intent();
        intent.putExtra(EXTRA_CANCEL_SIGNIN, true);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
