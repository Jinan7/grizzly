package com.undefinedbehaviourgames.grizzly;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class AddAccountDialog extends DialogFragment {

    private RecyclerView mRecyclerView;

    public static AddAccountDialog newInstance() {
        AddAccountDialog dialog = new AddAccountDialog();
        return dialog;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View v = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_account, null, false);
        mRecyclerView = v.findViewById(R.id.music_platforms_recycler_view);
        mRecyclerView.setAdapter(new MusicPlatformRecyclerAdapter(MusicPlatformLab.get().getMusicPlatforms()));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return new MaterialAlertDialogBuilder(getContext()).
                setView(v).
                setTitle("Select Music Platform").
                create();
//                setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                }).create();




    }

    private class MusicPlatformHolder extends RecyclerView.ViewHolder {

        private ImageView mPlatFormIcon;
        private TextView mPlatFormName;
        public MusicPlatformHolder(@NonNull View itemView) {

            super(itemView);

            mPlatFormName = (TextView) itemView.findViewById(R.id.music_platform_text);
            mPlatFormIcon = (ImageView) itemView.findViewById(R.id.music_platform_icon);
        }

        public void bind(MusicPlatform platform) {
            mPlatFormIcon.setImageResource(platform.getIconResourceId());
            mPlatFormName.setText(platform.getPlatformName());
        }

    }

    private class MusicPlatformRecyclerAdapter extends RecyclerView.Adapter<MusicPlatformHolder>{

        private List<MusicPlatform> mMusicPlatforms;

        public MusicPlatformRecyclerAdapter(List<MusicPlatform> musicPlatforms) {
            mMusicPlatforms = musicPlatforms;
        }

        @NonNull
        @Override
        public MusicPlatformHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(getContext()).inflate(R.layout.component_music_platform_layout, parent, false);
            return new MusicPlatformHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull MusicPlatformHolder holder, int position) {
            holder.bind(mMusicPlatforms.get(position));
        }

        @Override
        public int getItemCount() {
            return mMusicPlatforms.size();
        }
    }
}
