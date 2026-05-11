package com.undefinedbehaviourgames.grizzly;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.undefinedbehaviourgames.grizzly.MusicPlatform.MusicPlatforms;

import java.util.List;

public class AddAccountDialog extends DialogFragment {

    private static final String EXTRA_MUSIC_PLATFORM = "com.undefinedbehaviourgames.grizzly.music_platform";
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

    private class MusicPlatformHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mPlatFormIcon;
        private TextView mPlatFormName;
        private MusicPlatform mMusicPlatform;
        public MusicPlatformHolder(@NonNull View itemView) {

            super(itemView);
            itemView.setOnClickListener(this);
            mPlatFormName = (TextView) itemView.findViewById(R.id.music_platform_text);
            mPlatFormIcon = (ImageView) itemView.findViewById(R.id.music_platform_icon);
        }

        public void bind(MusicPlatform platform) {
            mPlatFormIcon.setImageResource(platform.getIconResourceId());
            mPlatFormName.setText(platform.getPlatformName());
            mMusicPlatform = platform;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_MUSIC_PLATFORM, mMusicPlatform.getTAG());
            getTargetFragment().onActivityResult(getTargetRequestCode(), MainActivity.RESULT_OK, intent);
            dismiss();

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
