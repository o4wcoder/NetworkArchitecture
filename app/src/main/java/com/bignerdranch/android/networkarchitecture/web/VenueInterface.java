package com.bignerdranch.android.networkarchitecture.web;

import com.bignerdranch.android.networkarchitecture.models.VenueSearchResponse;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by Chris Hare on 10/19/2015.
 */
public interface VenueInterface {

    @GET("/venues/search")
    void venueSearch(@Query("ll") String latLngString, Callback<VenueSearchResponse> callback);

    @FormUrlEncoded
    @POST("/checkins/add")
    void venueCheckIn
            (@Field("venueId") String venueId, Callback<Object> callback);
}
