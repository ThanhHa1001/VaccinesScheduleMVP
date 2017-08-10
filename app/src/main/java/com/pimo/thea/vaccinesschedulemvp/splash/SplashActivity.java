package com.pimo.thea.vaccinesschedulemvp.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.pimo.thea.vaccinesschedulemvp.R;
import com.pimo.thea.vaccinesschedulemvp.data.source.VaccinesScheduleRepository;
import com.pimo.thea.vaccinesschedulemvp.data.source.local.VaccinesScheduleLocalDataSource;
import com.pimo.thea.vaccinesschedulemvp.data.source.remote.VaccinesScheduleRemoteDataSource;
import com.pimo.thea.vaccinesschedulemvp.home.HomeActivity;
import com.pimo.thea.vaccinesschedulemvp.view.DialogLoading;

/**
 * Created by thea on 8/5/2017.
 */

public class SplashActivity extends AppCompatActivity implements SplashContract.View {

    private SplashContract.Presenter presenter;
    private ScrollView svUseOfTheTerms;
    private ImageView imageView;
    private Button btnAcceptTerms;
    private DialogLoading dialogLoading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        presenter = new SplashPresenter(this,
                VaccinesScheduleRepository.getInstance(
                        VaccinesScheduleRemoteDataSource.getInstance(this),
                        VaccinesScheduleLocalDataSource.getInstance(this)),
                this);

        svUseOfTheTerms = (ScrollView) findViewById(R.id.splash_activity_sv_use_of_the_terms);
        imageView = (ImageView) findViewById(R.id.splash_activity_image_view);
        btnAcceptTerms = (Button) findViewById(R.id.splash_activity_btn_use_of_the_terms);
        btnAcceptTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.acceptTerms();
            }
        });

        showSplashAfterDelay();

    }

    private void showSplashAfterDelay() {
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                presenter.bind();
            }
        }, 1000);
    }

    @Override
    public void showHomeActivity() {
        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showUseOfTheTerms() {
        svUseOfTheTerms.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideImageViewIcon() {
        imageView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showDialogLoading() {
        dialogLoading = new DialogLoading(this, getString(R.string.dialog_loading_insert_db));
        dialogLoading.show();
    }

    @Override
    public void dismissDialogLoading() {
        dialogLoading.dismiss();
    }
}
