package com.pimo.thea.vaccinesschedulemvp.detail.healthfeed;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.pimo.thea.vaccinesschedulemvp.R;
import com.pimo.thea.vaccinesschedulemvp.utils.ActivityUtils;

/**
 * Created by thea on 8/13/2017.
 */

public class DetailHealthFeedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_health_feed);

        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_health_feed_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.detail_health_feed_title_toolbar);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String url = intent.getStringExtra(DetailHealthFeedFragment.URL_REQUEST);

        Bundle b = new Bundle();
        b.putString(DetailHealthFeedFragment.URL_REQUEST, url);

        DetailHealthFeedFragment detailHealthFeedFragment = DetailHealthFeedFragment.getInstance();
        detailHealthFeedFragment.setArguments(b);

        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                detailHealthFeedFragment,
                R.id.detail_health_feed_frame_layout,
                DetailHealthFeedFragment.TAG);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
