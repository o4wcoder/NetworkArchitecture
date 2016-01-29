package com.bignerdranch.android.networkarchitecture.models;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.bignerdranch.android.networkarchitecture.R;
import com.bignerdranch.android.networkarchitecture.exceptions.UnauthorizedException;
import com.bignerdranch.android.networkarchitecture.listeners.VenueCheckInListener;
import com.bignerdranch.android.networkarchitecture.listeners.VenueSearchListener;
import com.bignerdranch.android.networkarchitecture.web.RetrofitErrorHandler;
import com.bignerdranch.android.networkarchitecture.web.VenueInterface;
import com.bignerdranch.android.networkarchitecture.web.VenueListDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

/**
 * Created by Chris Hare on 10/19/2015.
 */
public class DataManager {

    private static final String TAG = DataManager.class.getSimpleName();
    private static final String FOURSQUARE_ENDPOINT = "https://api.foursquare.com/v2";
    private static final String OAUTH_ENDPOINT = "https://foursquare.com/oauth2/authenticate";
    public static final String OAUTH_REDIRECT_URI = "http://www.bignerdranch.com";
    private static final String CLIENT_ID = "DAY00FEC53YTQXUJ51HIGUZ1BJA15VKSWW1WMLKPID11EPHQ";
    private static final String CLIENT_SECRET = "NIIIWM2FRPX1D0AQCU03Z5E2LYCQY2NHNLYYLBFWOIBR4KNA";
    private static final String FOURSQUARE_VERSION = "20151019";
    private static final String FOURSQUARE_MODE = "foursquare";

    private static final String TEST_LAT_LNG = "33.759,-84.332";

    private static final String PARAM_CLIENT_ID = "client_id";
    private static final String PARAM_CLIENT_SECRET = "client_secret";
    private static final String PARAM_V = "v";
    private static final String PARAM_M = "m";
    private static final String PARAM_RESPONSE_TYPE = "response_type";
    private static final String PARAM_REDIRECT_URI = "redirect_uri";
    private static final String TOKEN = "token";
    private static final String SWARM_MODE = "swarm";

    private static final String PARAM_OAUTH_TOKEN = "oauth_token";

    private List<Venue> mVenueList;
    private List<VenueSearchListener> mSearchListenerList;
    private List<VenueCheckInListener> mCheckInListenerList;

    private static DataManager sDataManager;
    private RestAdapter mBasicRestAdapter;
    private RestAdapter mAuthenticatedRestAdapter;

    private Context mContext;
    private static TokenStore mTokenStore;


    /*
     * Protected constructor and static get method insure that there is only
     * one instance gets created for the application
     */
    protected DataManager(Context context, RestAdapter basicRestAdapter,
                          RestAdapter authenticatedRestAdapter) {
        mContext = context;
        mTokenStore = new TokenStore(mContext);
        mBasicRestAdapter = basicRestAdapter;
        mAuthenticatedRestAdapter = authenticatedRestAdapter;
        mSearchListenerList = new ArrayList<>();
        mCheckInListenerList = new ArrayList<>();
    }

    public static DataManager get(Context context) {
        if (sDataManager == null) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(VenueSearchResponse.class, new VenueListDeserializer())
                    .create();

            RestAdapter basicRestAdapter = new RestAdapter.Builder()
                    .setEndpoint(FOURSQUARE_ENDPOINT)
                    .setConverter(new GsonConverter(gson))
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setRequestInterceptor(sRequestInterceptor)
                    .build();

            RestAdapter authenticationRestAdapter = new RestAdapter.Builder()
                    .setEndpoint(FOURSQUARE_ENDPOINT)
                    .setConverter(new GsonConverter(gson))
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setErrorHandler(new RetrofitErrorHandler())
                    .setRequestInterceptor(sAuthenticatedRequestInterceptor)
                    .build();

            sDataManager = new DataManager(context.getApplicationContext(),basicRestAdapter,
                    authenticationRestAdapter);
        }

        return sDataManager;
    }

    public static RequestInterceptor sRequestInterceptor = new RequestInterceptor() {
        @Override
        public void intercept(RequestFacade request) {
            request.addQueryParam(PARAM_CLIENT_ID,CLIENT_ID);
            request.addQueryParam(PARAM_CLIENT_SECRET,CLIENT_SECRET);
            request.addQueryParam(PARAM_V,FOURSQUARE_VERSION);
            request.addQueryParam(PARAM_M,FOURSQUARE_MODE);
        }
    };

    private static RequestInterceptor sAuthenticatedRequestInterceptor =
            new RequestInterceptor() {
                @Override
                public void intercept(RequestFacade request) {
                    request.addQueryParam(PARAM_OAUTH_TOKEN,mTokenStore.getAccessToken());
                    request.addQueryParam(PARAM_V,FOURSQUARE_VERSION);
                    request.addQueryParam(PARAM_M,SWARM_MODE);
                }
            };
    public void fetchVenueSearch() {
        VenueInterface venueInterface = mBasicRestAdapter.create(VenueInterface.class);

        venueInterface.venueSearch(TEST_LAT_LNG, new Callback<VenueSearchResponse>() {
            @Override
            public void success(VenueSearchResponse venueSearchResponse, Response response) {
                mVenueList = venueSearchResponse.getVenueList();
                notifySearchListeners();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG,"Failed to fetch venue search, error" );
            }
        });
    }

    private void notifySearchListeners() {
        for(VenueSearchListener listener : mSearchListenerList) {
            listener.onVenueSearchFinished();
        }
    }
    public List<Venue> getVenueList() {
        return mVenueList;
    }

    public void addVenueSearchListener(VenueSearchListener listener) {
        mSearchListenerList.add(listener);
    }

    public void removeVenueSearchListener(VenueSearchListener listener) {
        mSearchListenerList.remove(listener);
    }

    public void addVenueCheckInListener(VenueCheckInListener listener) {
        mCheckInListenerList.add(listener);
    }

    public void removeVenueCheckInListener(VenueCheckInListener listener) {
        mCheckInListenerList.remove(listener);
    }

    //Notify the check in listeners when a successful check in request was made.
    public void notifyCheckInListeners() {
        for(VenueCheckInListener listener : mCheckInListenerList) {
            listener.onVenueCheckInFinished();
        }
    }

    public void notifyCheckInListenersTokenExpired() {
        for(VenueCheckInListener listener : mCheckInListenerList) {
            listener.onTokenExpired();
        }
    }
    public String getAuthenticationUrl() {

        return Uri.parse(OAUTH_ENDPOINT).buildUpon()
                .appendQueryParameter(PARAM_CLIENT_ID,CLIENT_ID) //Which app our user is authorizing
                .appendQueryParameter(PARAM_RESPONSE_TYPE,TOKEN) //Send an OAuth token back
                .appendQueryParameter(PARAM_REDIRECT_URI,OAUTH_REDIRECT_URI) //Where to send the token to
                .build()
                .toString();
    }

    public Venue getVenue(String venueId) {

        for (Venue venue: mVenueList) {
            if(venue.getId().equals(venueId)) {
                return venue;
            }
        }

        return null;
    }

    public void checkInToVenue(String venueId) {
        VenueInterface venueInterface =
                mAuthenticatedRestAdapter.create(VenueInterface.class);
        venueInterface.venueCheckIn(venueId, new Callback<Object>() {
            @Override
            public void success(Object o, Response response) {
                // notify Fragment about successful check in
                Log.e(TAG,"Success check into venue");
                notifyCheckInListeners();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG,"Failed to check in to venue", error);
                //See if we got an UnauthorizedException and clear out the token
                if(error.getCause() instanceof UnauthorizedException) {
                    //Clear token & prompt user to reauthorize
                    mTokenStore.setAccessToken(null);
                    notifyCheckInListenersTokenExpired();
                }
            }
        });
    }
}
