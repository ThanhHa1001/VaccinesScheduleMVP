package com.pimo.thea.vaccinesschedulemvp.editorchilds;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.pimo.thea.vaccinesschedulemvp.R;
import com.pimo.thea.vaccinesschedulemvp.utils.ActivityUtils;

/**
 * Created by thea on 7/7/2017.
 */

public class EditorChildActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_child);

        Toolbar toolbar = (Toolbar) findViewById(R.id.editor_child_toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(getString(R.string.editor_child_title_toolbar_add));
        ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        ab.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        long childId = intent.getLongExtra(EditorChildFragment.CHILD_ID_ARG, -1L);
        Log.d("EditorChildActivity", "child id: " + childId);

        EditorChildFragment editorChildFragment = EditorChildFragment.newInstance();
        Bundle b = new Bundle();
        b.putLong(EditorChildFragment.CHILD_ID_ARG, childId);
        editorChildFragment.setArguments(b);


        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                editorChildFragment,
                R.id.editor_child_frame_layout,
                EditorChildFragment.TAG);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
