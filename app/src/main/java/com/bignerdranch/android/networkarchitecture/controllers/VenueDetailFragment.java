package com.bignerdranch.android.networkarchitecture.controllers;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.networkarchitecture.R;
import com.bignerdranch.android.networkarchitecture.listeners.VenueCheckInListener;
import com.bignerdranch.android.networkarchitecture.models.DataManager;
import com.bignerdranch.android.networkarchitecture.models.TokenStore;
import com.bignerdranch.android.networkarchitecture.models.Venue;

/**
 * A placeholder fragment containing a simple view.
 */
public class VenueDetailFragment extends Fragment implements VenueCheckInListener{

    /***************************************************************************/
    /*                              Constants                                  */
    /***************************************************************************/
    private static final String TAG = VenueDetailFragment.class.getSimpleName();
    private static final String ARG_VENUE_ID = "VenueDetailFragment.VenueId";
    private static final String EXPIRED_DIALOG = "expired_dialog";

    /***************************************************************************/
    /*                              Local Data                                 */
    /***************************************************************************/
    private DataManager mDataManager;
    private String mVenueId;
    private Venue mVenue;
    private TokenStore mTokenStore;

    private TextView mVenueNameTextView;
    private TextView mVenueAddressTextView;
    private Button mCheckInButton;

    public static VenueDetailFragment newInstance(String venueId) {
        VenueDetailFragment fragment = new VenueDetailFragment();

        Bundle args = new Bundle();
        args.putString(ARG_VENUE_ID, venueId);
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_venue_detail, container, false);

        mVenueNameTextView = (TextView)view.findViewById(R.id.fragment_venue_detail_VenueNameTextView);
        mVenueAddressTextView = (TextView)view.findViewById(R.id.fragment_venue_detail_VenueAddressTextView);
        mCheckInButton = (Button)view.findViewById(R.id.fragment_venue_detail_CheckInButton);

        mTokenStore = new TokenStore(getActivity());
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mVenueId = getArguments().getString(ARG_VENUE_ID);
        mDataManager = DataManager.get(getActivity());
        mDataManager.addVenueCheckInListener(this);
        mVenue = mDataManager.getVenue(mVenueId);
    }

    @Override
    public void onStop() {
        super.onStop();
        mDataManager.removeVenueCheckInListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mVenueNameTextView.setText(mVenue.getName());
        mVenueAddressTextView.setText(mVenue.getLocation().getFormattedAddress());

        if(mTokenStore.getAccessToken() != null) {
            mCheckInButton.setVisibility(View.VISIBLE);
            mCheckInButton.setOnClickListener(mCheckInClickLIstener);
        }
    }

    @Override
    public void onVenueCheckInFinished() {
        Toast.makeText(getActivity(),R.string.successful_check_in_message, Toast.LENGTH_SHORT)
                .show();
    }

    private View.OnClickListener mCheckInClickLIstener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.e(TAG,"Setting click listner for " + mVenue.getName());
            mDataManager.checkInToVenue(mVenueId);
        }
    };

    @Override
    public void onTokenExpired() {
        Log.e(TAG, "Token invalid, need to log in again");
        mCheckInButton.setVisibility(View.GONE);
        ExpiredTokenDialogFragment dialogFragment = new ExpiredTokenDialogFragment();
        dialogFragment.show(getFragmentManager(), EXPIRED_DIALOG);
    }
}
