package com.bignerdranch.android.networkarchitecture.controllers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.bignerdranch.android.networkarchitecture.R;

public class VenueDetailActivity extends AppCompatActivity {

    private static final String ARG_VENUE_ID = "VenueDetailActivity.VenueId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String venueId = getIntent().getStringExtra(ARG_VENUE_ID);
        setContentView(R.layout.activity_venue_detail);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_contrainer, VenueDetailFragment.newInstance(venueId))
                .commit();

    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_venue_detail, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    public static Intent newIntent(Context context, String venueId) {
        Intent intent = new Intent(context, VenueDetailActivity.class);
        intent.putExtra(ARG_VENUE_ID,venueId);
        return intent;
    }
}
