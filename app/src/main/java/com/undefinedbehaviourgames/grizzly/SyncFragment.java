package com.undefinedbehaviourgames.grizzly;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SyncFragment extends Fragment {

    private static final String ARG_SYNC_TO = "sync_to";
    private static final String TAG = "SyncFragment";

    public static SyncFragment newInstance(String [] syncTo) {

        SyncFragment fragment = new SyncFragment();
        Bundle args = new Bundle();
        args.putStringArray(ARG_SYNC_TO, syncTo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String syncTo[] = getArguments().getStringArray(ARG_SYNC_TO);

        for (String string: syncTo) {
            Log.d(TAG, string);
        }

        Log.d(TAG, PlaylistItemLab.getInstance().getPlaylistName());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_syncs, container, false);
        return v;
    }
}
