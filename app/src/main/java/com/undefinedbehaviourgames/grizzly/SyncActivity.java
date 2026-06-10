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
    private static final String EXTRA_NEW_SYNC = "com.undefinedbehaviourgames.grizzly.new_sync";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Fragment createFragment() {
        String [] syncTo = getIntent().getStringArrayExtra(EXTRA_SYNC_TO);
        boolean newSync = getIntent().getBooleanExtra(EXTRA_NEW_SYNC, false);
        return SyncFragment.newInstance(syncTo, newSync);
    }

    public static Intent newIntent(Context context, String [] syncTo, boolean newSync) {
        Intent intent = new Intent(context, SyncActivity.class);
        intent.putExtra(EXTRA_SYNC_TO, syncTo);
        intent.putExtra(EXTRA_NEW_SYNC, newSync);
        return intent;
    }
}