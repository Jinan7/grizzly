package com.undefinedbehaviourgames.grizzly;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

public class SyncActivity extends SingleFragmentActivity {

    private static final String EXTRA_SYNC_TO = "com.undefinedbehaviourgames.grizzly.sync_to";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Fragment createFragment() {
        String [] syncTo = getIntent().getStringArrayExtra(EXTRA_SYNC_TO);
        return SyncFragment.newInstance(syncTo);
    }

    public static Intent newIntent(Context context, String [] syncTo) {
        Intent intent = new Intent(context, SyncActivity.class);
        intent.putExtra(EXTRA_SYNC_TO, syncTo);
        return intent;
    }
}