package com.pimo.thea.vaccinesschedulemvp.detail.injchild;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.pimo.thea.vaccinesschedulemvp.R;

public class DetailInjCActivity extends AppCompatActivity {

    public static final String INJECTION_ID_ARGS = "injectionIdArgs";

    private long injId;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_inj_child);

        Intent intent = getIntent();
        injId = intent.getLongExtra(INJECTION_ID_ARGS, -1L);
        Log.d("DetailInjCActivity", "Inj schedule id: " + injId);

        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_inj_child_activity_toolbar);
        toolbar.setTitle(getString(R.string.detail_inj_child_toolbar_title));
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        ab.setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.detail_inj_child_activity_viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        tabLayout = (TabLayout) findViewById(R.id.detail_inj_child_activity_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        Bundle bundle = new Bundle();
        bundle.putLong(INJECTION_ID_ARGS, injId);

        DetailInjCScheduleFragment scheduleFragment = DetailInjCScheduleFragment.newInstance();
        scheduleFragment.setArguments(bundle);

        DetailInjCVaccineFragment vaccineFragment = DetailInjCVaccineFragment.newInstance();
        vaccineFragment.setArguments(bundle);

        DetailInjCPagerAdapter detailInjCPagerAdapter = new DetailInjCPagerAdapter(getSupportFragmentManager());
        detailInjCPagerAdapter.addFragments(scheduleFragment, getString(R.string.detail_inj_child_schedule));
        detailInjCPagerAdapter.addFragments(vaccineFragment, getString(R.string.detail_inj_child_vaccine));
        viewPager.setAdapter(detailInjCPagerAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
