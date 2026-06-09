package com.undefinedbehaviourgames.grizzly;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import spotify.SpotifyMusicPlatform;

public class SyncToDialog extends DialogFragment {

    private static final String TAG = "SyncToDialog";
    public static final String EXTRA_SYNC_TO = "com.undefinedbehaviourgames.grizzly.synctodialog.sync_to";
    private List<SyncTo> mSyncToList;
    private RecyclerView mRecyclerView;
    public static SyncToDialog newInstance() {
        SyncToDialog dialog = new SyncToDialog();

        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        mSyncToList = new SyncToLab().getSyncToList();

        View v = LayoutInflater.from(getContext()).inflate(R.layout.dialog_sync_to, null, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.sync_to_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new AccountAdapter(mSyncToList));
        return new MaterialAlertDialogBuilder(getContext())
                .setTitle("Sync To")
                .setView(v)
                .setPositiveButton("Sync", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        List<String> syncToList = new ArrayList<>();
                        for (SyncTo syncTo : mSyncToList) {

                            if (syncTo.shouldSync()){
                                syncToList.add(syncTo.getPlatformName());
                            }
                        }

                        String[] result = syncToList.toArray(new String[0]);

                        sendResult(Activity.RESULT_OK, result);

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
    }

    public void sendResult(int resultCode, String[] result) {

        Intent intent = new Intent();
        intent.putExtra(EXTRA_SYNC_TO, result);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    private class AccountHolder extends RecyclerView.ViewHolder  implements CompoundButton.OnCheckedChangeListener {

        private TextView mPlatformName;
        private MaterialCheckBox mCheckBox;
        private ImageView mIconView;
        private SyncTo mSyncTo;
        public AccountHolder(@NonNull View itemView) {
            super(itemView);

            mPlatformName = (TextView) itemView.findViewById(R.id.music_platform_text);
            mCheckBox = (MaterialCheckBox) itemView.findViewById(R.id.sync_to_checkbox);
            mIconView = (ImageView) itemView.findViewById(R.id.music_platform_icon);
        }

        public void bind(SyncTo syncTo) {
            mSyncTo = syncTo;
            mIconView.setImageResource(syncTo.getIconResourceId());
            mPlatformName.setText(syncTo.getPlatformName());
            mCheckBox.setChecked(syncTo.shouldSync());
            mCheckBox.setOnCheckedChangeListener(this);
        }

        @Override
        public void onCheckedChanged(@NonNull CompoundButton buttonView, boolean isChecked) {
            mSyncTo.setSync(isChecked);
        }
    }


    private class AccountAdapter extends RecyclerView.Adapter<AccountHolder> {

        List<SyncTo> mSyncToList;
        public AccountAdapter(List<SyncTo> syncToList) {
            mSyncToList  = syncToList;
        }
        @NonNull
        @Override
        public AccountHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getContext()).inflate(R.layout.component_account_layout_checkbox, parent, false);

            return new AccountHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull AccountHolder holder, int position) {
            holder.bind(mSyncToList.get(position));
        }

        @Override
        public int getItemCount() {
            return mSyncToList.size();
        }


    }
}
