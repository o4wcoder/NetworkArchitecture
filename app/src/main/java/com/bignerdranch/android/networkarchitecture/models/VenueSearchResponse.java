package com.bignerdranch.android.networkarchitecture.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Chris Hare on 10/19/2015.
 */
public class VenueSearchResponse {

    @SerializedName("venues") private List<Venue> mVenueList;

    public List<Venue> getVenueList() {
        return mVenueList;
    }
}
