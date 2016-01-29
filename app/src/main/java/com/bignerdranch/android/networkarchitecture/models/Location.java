package com.bignerdranch.android.networkarchitecture.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Chris Hare on 10/19/2015.
 */
public class Location {

    @SerializedName("lat") private double mLatitude;
    @SerializedName("lng") private double mLongitude;
    @SerializedName("formattedAddress") private List<String> mForemattedAdress;

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public List<String> getForemattedAdress() {
        return mForemattedAdress;
    }

    public String getFormattedAddress() {
        String formattedAddress = "";

        for(String addressPart : mForemattedAdress) {
            formattedAddress += addressPart;

            if(mForemattedAdress.indexOf(addressPart) !=
                    (mForemattedAdress.size() - 1)) {
                formattedAddress += " ";
            }
        }

        return formattedAddress;
    }
}
