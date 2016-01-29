package com.bignerdranch.android.networkarchitecture.helpers;

import android.net.Uri;

/**
 * Created by Chris Hare on 10/19/2015.
 * Helper class to determine
 */
public class FoursquareOauthUriHelper {

    private static final String ACCESS_TOKEN_PARAM = "access_token=";

    private Uri mOauthUri;

    public FoursquareOauthUriHelper(String oauthUri) {
        mOauthUri = Uri.parse(oauthUri);
    }

    //pull out access token from the URI
    public String getAccessToken() {
        String uriFragment = mOauthUri.getFragment();
        if(uriFragment.contains(ACCESS_TOKEN_PARAM)) {
            return uriFragment.substring(ACCESS_TOKEN_PARAM.length());
        }
        return null;
    }

    //see if authorization was successful
    public boolean isAuthorized() {
        return getAccessToken() != null;
    }
}
