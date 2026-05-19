package com.undefinedbehaviourgames.grizzly;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

public class UserLibraryActivity extends SingleFragmentActivity {

    public static final String TAG = "UserLibraryActivityLogger";
    public static final String EXTRA_USER_ID = "com.undefinedbehaviourgames.grizzly.user_id";
    public static final String EXTRA_PLATFORM = "com.undefinedbehaviourgames.grizzly.plalform";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public Fragment createFragment() {
        String id = getIntent().getStringExtra(EXTRA_USER_ID);
        String platform = getIntent().getStringExtra(EXTRA_PLATFORM);
        return UserLibraryFragment.newInstance(id, platform);
    }

    public static Intent newIntent(Context context, String user_id, String platform) {
        Intent intent = new Intent(context, UserLibraryActivity.class);
        intent.putExtra(EXTRA_USER_ID, user_id);
        intent.putExtra(EXTRA_PLATFORM, platform);
        return intent;
    }
}