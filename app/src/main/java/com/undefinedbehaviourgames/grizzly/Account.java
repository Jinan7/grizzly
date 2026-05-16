package com.undefinedbehaviourgames.grizzly;

import java.util.UUID;

public class Account {

    public UUID mUUID;
    public int mIconResourceId;
    public String mAccountName;
    public String mMusicServiceName;

    public Account() {
        mUUID = UUID.randomUUID();
    }
    public static Account getInstance(int iconResourceId, String accountName, String musicServiceName) {
        Account account = new Account();
        account.setIconResourceId(iconResourceId);
        account.setAccountName(accountName);
        account.setMusicServiceName(musicServiceName);
        return account;
    }



    public UUID getUUID() {
        return mUUID;
    }

    public String getID() {
        return null;
    }
    public int getIconResourceId() {
        return mIconResourceId;
    }

    public void setIconResourceId(int iconResourceId) {
        mIconResourceId = iconResourceId;
    }

    public String getAccountName() {
        return mAccountName;
    }

    public void setAccountName(String accountName) {
        mAccountName = accountName;
    }

    public String getMusicServiceName() {
        return mMusicServiceName;
    }

    public void setMusicServiceName(String musicServiceName) {
        mMusicServiceName = musicServiceName;
    }
}
