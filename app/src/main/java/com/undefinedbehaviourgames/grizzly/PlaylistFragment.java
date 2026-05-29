package com.undefinedbehaviourgames.grizzly;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import spotify.SpotifyMusicPlatform;
import tidal.TidalMusicPlatform;

public class PlaylistFragment extends Fragment implements MusicPlatform.FetchPlaylistItemsCallbacks {

    private static final String ARGS_PLAYLIST_ID = "playlist_id";
    private static final String ARGS_PLATFORM = "platform";
    private RecyclerView mRecyclerView;
    public static PlaylistFragment newInstance(String playlistId, String musicServiceName) {
        PlaylistFragment fragment = new PlaylistFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_PLAYLIST_ID, playlistId);
        args.putString(ARGS_PLATFORM, musicServiceName);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String playlistId = getArguments().getString(ARGS_PLAYLIST_ID);
        String platform = getArguments().getString(ARGS_PLATFORM);

        switch (platform) {
            case MusicPlatform.Platforms.spotify:
                SpotifyMusicPlatform.getInstance(getContext()).fetchPlaylistsItems(playlistId, this);
                break;
            case MusicPlatform.Platforms.tidal:
                TidalMusicPlatform.getInstance(getContext()).fetchPlaylistsItems(playlistId, this);
                break;
        }

    }
    @Override
    public void onFetchPlaylistItems() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_playlist, container, false);

        mRecyclerView = v.findViewById(R.id.songs_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new SongAdapter());

        return v;
    }




    private class SongHolder extends RecyclerView.ViewHolder {

        public SongHolder(@NonNull View itemView) {
            super(itemView);
        }

    }


    private class SongAdapter extends RecyclerView.Adapter<SongHolder> {


        @NonNull
        @Override
        public SongHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.component_song_layout, parent, false);
            return new SongHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SongHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 10;
        }
    }
}
