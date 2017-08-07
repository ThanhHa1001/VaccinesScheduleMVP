package com.pimo.thea.vaccinesschedulemvp.detail.injschedulechild;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.pimo.thea.vaccinesschedulemvp.R;
import com.pimo.thea.vaccinesschedulemvp.utils.ActivityUtils;

/**
 * Created by thea on 8/3/2017.
 */

public class DetailInjSActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detai_inj_schedule_child);

        Intent intent = getIntent();
        long childId = intent.getLongExtra(DetailInjSFragment.CHILD_ID_ARG, -1L);
        long childDob = intent.getLongExtra(DetailInjSFragment.CHILD_DOB_ARG, -1L);

        DetailInjSFragment detailInjSFragment = DetailInjSFragment.newInstance();
        Bundle b = new Bundle();
        b.putLong(DetailInjSFragment.CHILD_ID_ARG, childId);
        b.putLong(DetailInjSFragment.CHILD_DOB_ARG, childDob);
        detailInjSFragment.setArguments(b);

        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                detailInjSFragment,
                R.id.detail_inj_schedule_frame_layout,
                DetailInjSFragment.TAG);
    }
}
