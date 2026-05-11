package com.undefinedbehaviourgames.grizzly;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.divider.MaterialDividerItemDecoration;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.shape.MaterialShapeDrawable;

import java.util.List;

public class MainFragment extends Fragment {

    private static final String TAG = "MainFragmentLogger";
    private static final String ADD_ACCOUNT_DIALOG_TAG = "add account dialog";
    private static final int ADD_ACCOUNT_REQUEST_CODE = 0;
    private AppBarLayout mAppBarLayout;
    private RecyclerView mAccountRecyclerView;
    private FloatingActionButton mFloatingActionButton;
    public static MainFragment newInstance() {

        MainFragment fragment = new MainFragment();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_main,container,false);

        mAppBarLayout = (AppBarLayout) v.findViewById(R.id.toolbar_main);
        mAccountRecyclerView = (RecyclerView) v.findViewById(R.id.music_accounts_recycler_view);
        mAccountRecyclerView.setAdapter(new AccountsAdapter(AccountLab.get().getAccounts()));
        mAccountRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mFloatingActionButton = v.findViewById(R.id.fab_add_account);

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddAccountDialog dialog = AddAccountDialog.newInstance();
                dialog.setTargetFragment(MainFragment.this, ADD_ACCOUNT_REQUEST_CODE);
                dialog.show(getParentFragmentManager(), ADD_ACCOUNT_DIALOG_TAG);
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) return;

        switch (requestCode) {

            case ADD_ACCOUNT_REQUEST_CODE:
                Intent intent = new Intent(getActivity(), SignInActivity.class);
                startActivity(intent);
                break;
        }
    }

    private class AccountHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mAccountIcon;
        private TextView mAccountName;
        private  TextView mAccountMusicService;
        public AccountHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            mAccountIcon = (ImageView) itemView.findViewById(R.id.account_icon);
            mAccountName = (TextView) itemView.findViewById(R.id.account_name);
            mAccountMusicService = (TextView) itemView.findViewById(R.id.account_music_service_name);
        }

        public void bind(Account account) {
            mAccountIcon.setImageResource(account.getIconResourceId());
            mAccountName.setText(account.getAccountName());
            mAccountMusicService.setText(account.mMusicServiceName);
        }

        @Override
        public void onClick(View v) {
//            Intent intent = new Intent();
//            intent.putExtra(EXTRA_MUSIC_PLATFORM, "youtube");
        }
    }
    private class AccountsAdapter extends RecyclerView.Adapter<AccountHolder> {

        private List<Account> mAccounts;

        public AccountsAdapter(List<Account> accounts) {
            mAccounts = accounts;
        }
        @NonNull
        @Override
        public AccountHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getContext()).inflate(R.layout.component_account_layout, parent, false);
            return new AccountHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull AccountHolder holder, int position) {
            holder.bind(mAccounts.get(position));
        }

        @Override
        public int getItemCount() {
            return mAccounts.size();
        }
    }
}