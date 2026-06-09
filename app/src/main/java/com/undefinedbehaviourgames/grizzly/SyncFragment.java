package com.undefinedbehaviourgames.grizzly;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SyncFragment extends Fragment {

    private static final String ARG_SYNC_TO = "sync_to";
    private static final String TAG = "SyncFragment";

    private RecyclerView mRecyclerView;
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
        mRecyclerView = (RecyclerView) v.findViewById(R.id.syncs_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new SyncAdapter());
        return v;
    }


    private class SyncHolder extends RecyclerView.ViewHolder {
        public SyncHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private class SyncAdapter extends RecyclerView.Adapter<SyncHolder> {

        @NonNull
        @Override
        public SyncHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getContext()).inflate(R.layout.component_sync_progress_layout, parent, false);
            return new SyncHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull SyncHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 10;
        }
    }
}
