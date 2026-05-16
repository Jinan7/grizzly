package com.undefinedbehaviourgames.grizzly;

public abstract class Playlist {

    public String mName;
    public String mDescription;
    public String mID;
    public String mOwner;
    public Playlist(){

    }
    public void init(){

    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getID() {
        return mID;
    }

    public void setID(String ID) {
        mID = ID;
    }

    public String getOwner() {
        return mOwner;
    }

    public void setOwner(String owner) {
        mOwner = owner;
    }
}
