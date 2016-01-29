package com.bignerdranch.android.networkarchitecture.models;


import com.google.gson.annotations.SerializedName;

/**
 * Created by Chris Hare on 10/19/2015.
 */
public class Category {

    @SerializedName("id") private String mId;
    @SerializedName("name") private String mName;
    @SerializedName("icon") private Icon mIcon;
    @SerializedName("primary") private boolean mPrimary;
}
