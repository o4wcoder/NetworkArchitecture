package com.bignerdranch.android.networkarchitecture.views;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.bignerdranch.android.networkarchitecture.controllers.VenueDetailActivity;
import com.bignerdranch.android.networkarchitecture.models.Venue;

/**
 * Created by Chris Hare on 10/19/2015.
 */
public class VenueHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private final static String TAG = VenueHolder.class.getSimpleName();

    private VenueView mVenueView;
    private Venue mVenue;

    public VenueHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        mVenueView = (VenueView) itemView;
    }

    public void bindVenue(Venue venue) {
        mVenue = venue;
        mVenueView.setVenueTitle(mVenue.getName());
        mVenueView.setVenueAddress(mVenue.getLocation().getFormattedAddress());
    }

    @Override
    public void onClick(View v) {
        Log.e(TAG, "In onclick with venue: " +mVenue.getName());
        Context context = itemView.getContext();
        Intent intent = VenueDetailActivity.newIntent(context,mVenue.getId());
        context.startActivity(intent);
    }
}
