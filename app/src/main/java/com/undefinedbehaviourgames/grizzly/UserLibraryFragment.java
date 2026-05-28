package com.undefinedbehaviourgames.grizzly;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import amazon.AmazonMusicPlatform;
import spotify.SpotifyMusicPlatform;
import tidal.TidalMusicPlatform;

public class UserLibraryFragment extends Fragment implements MusicPlatform.FetchPlaylistCallbacks {

    private static final String ARGS_USER_ID =  "user id";
    private static final String ARGS_PLATFORM = "platform";
    private RecyclerView mRecyclerView;
    public  static  UserLibraryFragment newInstance(String userId, String platform) {
        UserLibraryFragment fragment = new UserLibraryFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_USER_ID, userId);
        args.putString(ARGS_PLATFORM, platform);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        String userId = getArguments().getString(ARGS_USER_ID);
        String platform = getArguments().getString(ARGS_PLATFORM);

        switch (platform) {

            case MusicPlatform.Platforms.spotify:
                SpotifyMusicPlatform.getInstance(getContext()).fetchPlaylists(userId, this);
                break;
            case MusicPlatform.Platforms.tidal:
                TidalMusicPlatform.getInstance(getContext()).fetchPlaylists(userId, this);
                break;
            case MusicPlatform.Platforms.amazon:
                AmazonMusicPlatform.getInstance(getContext()).fetchPlaylists(userId, this);
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_library, container, false);
        mRecyclerView = v.findViewById(R.id.playlists_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new PlaylistAdapter(PlaylistLab.getInstance().getPlaylist()));
        return v;
    }

    @Override
    public void onFetchPlaylist() {
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }


    private class PlaylistHolder extends RecyclerView.ViewHolder {

        private Playlist mPlaylist;
        private TextView mPlaylistNameView;
        private TextView mPlaylistOwnerView;

        public PlaylistHolder(@NonNull View itemView) {
            super(itemView);
            mPlaylistNameView = (TextView) itemView.findViewById(R.id.playlist_name);
            mPlaylistOwnerView = (TextView) itemView.findViewById(R.id.owner_name);
        }

        public void bind(Playlist playlist) {
            mPlaylist = playlist;
            mPlaylistNameView.setText(playlist.getName());
            mPlaylistOwnerView.setText(playlist.getOwner());
        }
    }


    private class PlaylistAdapter extends RecyclerView.Adapter<PlaylistHolder> {

        List<Playlist> mPlayLists;
        public PlaylistAdapter(List<Playlist> playlists) {
            mPlayLists = playlists;
        }
        @NonNull
        @Override
        public PlaylistHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getContext()).inflate(R.layout.component_playlist_layout, parent, false);

            return new PlaylistHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull PlaylistHolder holder, int position) {
            holder.bind(mPlayLists.get(position));
        }

        @Override
        public int getItemCount() {
            return mPlayLists.size();
        }
    }
}
