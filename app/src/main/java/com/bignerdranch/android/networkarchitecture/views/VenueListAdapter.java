package com.bignerdranch.android.networkarchitecture.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.bignerdranch.android.networkarchitecture.models.Venue;

import java.util.List;

/**
 * Created by Chris Hare on 10/19/2015.
 */
public class VenueListAdapter extends RecyclerView.Adapter<VenueHolder>{

    private Context mContext;
    private List<Venue> mVenueList;

    public VenueListAdapter(Context context, List<Venue> venueList) {
        mContext = context;
        mVenueList = venueList;
    }

    public void setVenueList(List<Venue> venueList) {
        mVenueList = venueList;
        notifyDataSetChanged();
    }

    @Override
    public VenueHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        VenueView venueView = new VenueView(mContext);
        return new VenueHolder(venueView);
    }

    @Override
    public void onBindViewHolder(VenueHolder venueHolder, int position) {
        venueHolder.bindVenue(mVenueList.get(position));
    }

    @Override
    public int getItemCount() {
        return mVenueList.size();
    }
}
