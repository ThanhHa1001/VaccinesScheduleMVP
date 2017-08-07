package com.pimo.thea.vaccinesschedulemvp.detail.moreinformation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.FrameLayout;

import com.pimo.thea.vaccinesschedulemvp.R;
import com.pimo.thea.vaccinesschedulemvp.listmoreinformation.ListMoreInformationFragment;
import com.pimo.thea.vaccinesschedulemvp.utils.ActivityUtils;

/**
 * Created by thea on 8/6/2017.
 */

public class DetailMoreInformationActivity extends AppCompatActivity {
    public static final String INFORMATION_REQUEST = "informationRequest";
    public static final String INFORMATION_ID_REQUEST = "informationIdRequest";

    private ActionBar actionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_information);

        Intent intent = getIntent();
        int request = intent.getIntExtra(INFORMATION_REQUEST, -1);
        long objectId = intent.getLongExtra(INFORMATION_ID_REQUEST, -1L);

        Log.d("DetailMIActivity", "request: " + request + " objectId: " + objectId);

        Toolbar toolbar = (Toolbar) findViewById(R.id.more_information_toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        setupTitleToolbar(request);

        Bundle bundle = new Bundle();
        bundle.putInt(INFORMATION_REQUEST, request);
        bundle.putLong(INFORMATION_ID_REQUEST, objectId);

        DetailMoreInformationFragment informationFragment = DetailMoreInformationFragment.newInstance();
        informationFragment.setArguments(bundle);

        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                informationFragment,
                R.id.more_information_frame_layout,
                DetailMoreInformationFragment.TAG);
    }

    private void setupTitleToolbar(int request) {
        if (request == ListMoreInformationFragment.DISEASE_REQUEST) {
            actionBar.setTitle(getString(R.string.detail_m_i_title_toolbar_disease));
        }else if (request == ListMoreInformationFragment.VACCINE_REQUEST) {
            actionBar.setTitle(getString(R.string.detail_m_i_title_toolbar_vaccine));
        } else {
            actionBar.setTitle(getString(R.string.detail_m_i_title_toolbar_childcare));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
