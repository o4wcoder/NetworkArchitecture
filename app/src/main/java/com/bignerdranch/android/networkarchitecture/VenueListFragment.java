package com.bignerdranch.android.networkarchitecture;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;

import com.bignerdranch.android.networkarchitecture.controllers.AuthenticationActivity;
import com.bignerdranch.android.networkarchitecture.listeners.VenueSearchListener;
import com.bignerdranch.android.networkarchitecture.models.DataManager;
import com.bignerdranch.android.networkarchitecture.models.TokenStore;
import com.bignerdranch.android.networkarchitecture.models.Venue;
import com.bignerdranch.android.networkarchitecture.views.VenueListAdapter;

import java.util.Collections;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class VenueListFragment extends Fragment implements VenueSearchListener{

    /*********************************************************************/
    /*                         Constants                                 */
    /*********************************************************************/
    private static final int AUTHENTICATION_ACTIVITY_REQUEST=0;

    /*********************************************************************/
    /*                         Local Data                                */
    /*********************************************************************/

    protected DataManager mDataManager;
    private VenueListAdapter mVenueListAdapter;
    List<Venue> mVenueList;
    RecyclerView mRecyclerView;
    TokenStore mTokenStore;

    public VenueListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        mTokenStore = new TokenStore(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_venue_list, container, false);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.venueRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                getActivity(),LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mVenueListAdapter = new VenueListAdapter(getActivity(), Collections.EMPTY_LIST);
        mRecyclerView.setAdapter(mVenueListAdapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        mDataManager = DataManager.get(getActivity());
        mDataManager.addVenueSearchListener(this);
        mDataManager.fetchVenueSearch();
    }

    @Override
    public void onStop() {
        super.onStop();

        mDataManager.removeVenueSearchListener(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(mTokenStore.getAccessToken() == null) {
            inflater.inflate(R.menu.menu_sign_in,menu);
        }
        else {
            inflater.inflate(R.menu.menu_sign_out,menu);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.sign_in:
                Intent authenticationIntent = new Intent(getActivity(),
                        AuthenticationActivity.class);
                startActivityForResult(authenticationIntent,
                        AUTHENTICATION_ACTIVITY_REQUEST);
                return true;
            case R.id.sign_out:
                //Sign out just remove access token
                mTokenStore.setAccessToken(null);
                //Remove sign out menu
                getActivity().invalidateOptionsMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == AUTHENTICATION_ACTIVITY_REQUEST) {
            getActivity().invalidateOptionsMenu();
        }
        super.onActivityResult(requestCode,resultCode,data);
    }
    @Override
    public void onVenueSearchFinished() {

        mVenueList = mDataManager.getVenueList();
        mVenueListAdapter.setVenueList(mVenueList);
    }
}
