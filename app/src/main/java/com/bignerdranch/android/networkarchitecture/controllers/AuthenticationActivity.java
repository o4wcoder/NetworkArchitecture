package com.bignerdranch.android.networkarchitecture.controllers;

import android.media.session.MediaSession;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bignerdranch.android.networkarchitecture.R;
import com.bignerdranch.android.networkarchitecture.helpers.FoursquareOauthUriHelper;
import com.bignerdranch.android.networkarchitecture.models.DataManager;
import com.bignerdranch.android.networkarchitecture.models.TokenStore;

public class AuthenticationActivity extends AppCompatActivity {

    private static final String TAG = AuthenticationActivity.class.getSimpleName();

    private WebView mWebView;
    private DataManager mDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mWebView = new WebView(this);
        setContentView(mWebView);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(mWebViewClient);

        mDataManager = DataManager.get(this);

        Log.e(TAG, "Auth Url: " + mDataManager.getAuthenticationUrl());
        //Tell the WebView which URI to load
        mWebView.loadUrl(mDataManager.getAuthenticationUrl());

    }

    /**
     * Used to check Foursquare's response to teh authentication request
     */
    private WebViewClient mWebViewClient = new WebViewClient() {

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {

            if(url.contains(DataManager.OAUTH_REDIRECT_URI)) {
                FoursquareOauthUriHelper uriHelper =
                        new FoursquareOauthUriHelper(url);

                if(uriHelper.isAuthorized()) {
                    //Fetch access token and store it
                    String accessToken = uriHelper.getAccessToken();
                    TokenStore tokenStore = new TokenStore(AuthenticationActivity.this);
                    tokenStore.setAccessToken(accessToken);
                }
                //Return to previous activity whether authentication finished or not
                finish();
            }
            return true;
        }
    };


}
