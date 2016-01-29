package com.bignerdranch.android.networkarchitecture.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Chris Hare on 10/19/2015.
 */
public class Venue {

    @SerializedName("id") private String mId;
    @SerializedName("name") private String mName;
    @SerializedName("verified") private  boolean mVerified;
    @SerializedName("location") private Location mLocation;

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public boolean isVerified() {
        return mVerified;
    }

    public Location getLocation() {
        return mLocation;
    }

    public List<Category> getCategory() {
        return mCategory;
    }

    @SerializedName("categories") private List<Category> mCategory;
}
