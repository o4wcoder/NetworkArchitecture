package com.bignerdranch.android.networkarchitecture.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Chris Hare on 10/19/2015.
 */
public class TokenStore {

    private static final String TOKEN_KEY = "TokenStore.TokenKey";

    private Context mContext;
    private SharedPreferences mSharedPreferences;

    public TokenStore(Context context) {
        mContext = context.getApplicationContext();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    public String getAccessToken() {
        return mSharedPreferences.getString(TOKEN_KEY,null);
    }

    public void setAccessToken(String accessToken) {
        mSharedPreferences.edit()
                .putString(TOKEN_KEY,accessToken)
                .apply();
    }
}
