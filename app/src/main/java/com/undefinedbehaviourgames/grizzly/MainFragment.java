package com.undefinedbehaviourgames.grizzly;

import static android.widget.Toast.LENGTH_SHORT;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.openid.appauth.AuthorizationResponse;

import java.util.List;

import amazon.AmazonMusicPlatform;
import spotify.SpotifyMusicPlatform;
import tidal.TidalMusicPlatform;
import youtube.YoutubeMusicPlatform;

public class MainFragment extends Fragment implements SpotifyMusicPlatform.Callbacks {

    private static final String TAG = "MainFragmentLogger";
    private static final String ADD_ACCOUNT_DIALOG_TAG = "add account dialog";
    private static final String CREATE_USER_DIALOG_TAG = "create user dialog";
    private static final int ADD_ACCOUNT_REQUEST_CODE = 0;
    private static final int CREATE_USER_REQUEST_CODE = 1;
    private AppBarLayout mAppBarLayout;
    private RecyclerView mAccountRecyclerView;
    private FloatingActionButton mFloatingActionButton;
    private CreateUserDialog mCreateUserDialog;
    public static MainFragment newInstance() {

        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy called");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_main,container,false);

        mAppBarLayout = (AppBarLayout) v.findViewById(R.id.toolbar_main);
//        mAppBarLayout.setLiftOnScroll(false);
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

            case CREATE_USER_REQUEST_CODE:
                Log.d(TAG, "cancelling sign in");
                SpotifyMusicPlatform.getInstance(getContext()).cancel();
                break;


        }
    }

    public void dismissDialog() {

        if (mCreateUserDialog != null) {
            mCreateUserDialog.dismiss();
        }

    }

    public void signInNewUser(AuthorizationResponse response, String service) {
        Log.d(TAG, response.authorizationCode);
        mCreateUserDialog = CreateUserDialog.newInstance();
        mCreateUserDialog.setTargetFragment(MainFragment.this, CREATE_USER_REQUEST_CODE);
        mCreateUserDialog.show(getParentFragmentManager(), CREATE_USER_DIALOG_TAG);
        switch (service) {
            case MusicPlatform.Platforms.spotify:
                SpotifyMusicPlatform.getInstance(getContext()).signIn(response, MainFragment.this);
                break;
            case MusicPlatform.Platforms.tidal:
                TidalMusicPlatform.getInstance(getContext()).signIn(response, MainFragment.this);
                break;
            case MusicPlatform.Platforms.amazon:
                AmazonMusicPlatform.getInstance(getContext()).signIn(response, MainFragment.this);
                break;
            case MusicPlatform.Platforms.youtube:
                YoutubeMusicPlatform.getInstance(getContext()).signIn(response, MainFragment.this);
                break;
        }


    }

    public void signInNewUser(String accountName, String service) {

        mCreateUserDialog = CreateUserDialog.newInstance();
        mCreateUserDialog.setTargetFragment(MainFragment.this, CREATE_USER_REQUEST_CODE);
        mCreateUserDialog.show(getParentFragmentManager(), CREATE_USER_DIALOG_TAG);
        YoutubeMusicPlatform.getInstance(getContext()).signIn(accountName, MainFragment.this);
    }

    @Override
    public void cancelSignIn() {
        dismissDialog();
    }

    @Override
    public void onSignInFinished(Account account) {
        AccountLab.get().add(account);
        mAccountRecyclerView.getAdapter().notifyDataSetChanged();
        dismissDialog();
    }

    @Override
    public void onSignInError() {

        dismissDialog();
        Toast.makeText(getContext(), "Could not complete sign in", LENGTH_SHORT).show();
    }

    private class AccountHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mAccountIcon;
        private TextView mAccountName;
        private  TextView mAccountMusicService;
        private Account mAccount;
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
            mAccountMusicService.setText(account.getMusicServiceName());
            mAccount = account;
        }

        @Override
        public void onClick(View v) {

            Intent intent = UserLibraryActivity.newIntent(getContext(), mAccount.getID(), mAccount.getMusicServiceName());
            startActivity(intent);
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