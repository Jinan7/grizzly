package com.undefinedbehaviourgames.grizzly;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.List;

public class SyncFragment extends Fragment implements SyncLab.Callbacks {

    private static final String ARG_SYNC_TO = "sync_to";
    private static final String ARG_NEW_SYNC = "new_sync";
    private static final String TAG = "SyncFragment";

    private RecyclerView mRecyclerView;
    public static SyncFragment newInstance(String [] syncTo, boolean newSync) {

        SyncFragment fragment = new SyncFragment();
        Bundle args = new Bundle();
        args.putStringArray(ARG_SYNC_TO, syncTo);
        args.putBoolean(ARG_NEW_SYNC, newSync);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String syncTo[] = getArguments().getStringArray(ARG_SYNC_TO);
        boolean newSync = getArguments().getBoolean(ARG_NEW_SYNC);

        if (newSync == true) {

            for (String string: syncTo) {
                SyncTask newSyncTask =  new SyncTask(string, PlaylistLab.getInstance().getPlatform());
                newSyncTask.setPlaylistName(PlaylistItemLab.getInstance().getPlaylistName());

                new SyncHelper(newSyncTask).execute();
            }

        }



        Log.d(TAG, PlaylistItemLab.getInstance().getPlaylistName());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_syncs, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.syncs_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new SyncAdapter(SyncLab.getInstance(this).getSyncTasks()));
        return v;
    }

    @Override
    public void onProgressUpdate() {
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }


    private class SyncHolder extends RecyclerView.ViewHolder {

        private LinearProgressIndicator mLinearProgressIndicator;
        private ImageView mSyncFrom;
        private ImageView mSyncTo;
        private TextView mTextView;
        public SyncHolder(@NonNull View itemView) {
            super(itemView);
            mLinearProgressIndicator = itemView.findViewById(R.id.sync_progress);
            mSyncFrom = itemView.findViewById(R.id.sync_from);
            mSyncTo = itemView.findViewById(R.id.sync_to);
            mTextView = itemView.findViewById(R.id.sync_playlist_name);
        }

        public void bind(SyncTask syncTask) {
            mLinearProgressIndicator.setMax(syncTask.getTotal());
            mLinearProgressIndicator.setProgress(syncTask.getProgress());
            mTextView.setText(syncTask.getPlaylistName());
            switch (syncTask.getSyncTo()) {
                case MusicPlatform.Platforms.spotify:
                    mSyncTo.setImageResource(R.drawable.spotify);
                    break;
                case MusicPlatform.Platforms.tidal:
                    mSyncTo.setImageResource(R.drawable.tidal);
                    break;
            }

            switch (syncTask.getSyncFrom()) {
                case MusicPlatform.Platforms.spotify:
                    mSyncFrom.setImageResource(R.drawable.spotify);
                    break;
                case MusicPlatform.Platforms.tidal:
                    mSyncFrom.setImageResource(R.drawable.tidal);
                    break;
            }
        }
    }

    private class SyncAdapter extends RecyclerView.Adapter<SyncHolder> {

        List<SyncTask> mSyncItemList;

        public SyncAdapter(List<SyncTask> syncItems) {
            mSyncItemList = syncItems;
        }
        @NonNull
        @Override
        public SyncHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getContext()).inflate(R.layout.component_sync_progress_layout, parent, false);
            return new SyncHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull SyncHolder holder, int position) {

            holder.bind(mSyncItemList.get(position));
        }

        @Override
        public int getItemCount() {
            return mSyncItemList.size();
        }
    }

    private class SyncHelper extends AsyncTask<Void, Void, Void> {

        private SyncTask mSyncTask;
        public SyncHelper(SyncTask syncTask) {
            mSyncTask = syncTask;
        }

        @Override
        protected Void doInBackground(Void... tasks) {

            int total = 1;
            for (PlaylistItem item : PlaylistItemLab.getInstance().getPlaylistItems()) {

                if (item.isChecked()) {
                    SyncItem syncItem = new SyncItem(item.getTitle(), item.getArtist());
                    mSyncTask.addItem(syncItem);
                    total +=2;
                }
            }

            mSyncTask.setTotal(total);
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            SyncLab.getInstance(SyncFragment.this).addSyncTask(getContext(), mSyncTask);
        }
    }
}
