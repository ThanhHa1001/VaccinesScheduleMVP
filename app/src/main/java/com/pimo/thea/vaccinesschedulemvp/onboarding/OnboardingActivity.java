package com.pimo.thea.vaccinesschedulemvp.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.pimo.thea.vaccinesschedulemvp.R;
import com.pimo.thea.vaccinesschedulemvp.data.source.VaccinesScheduleRepository;
import com.pimo.thea.vaccinesschedulemvp.data.source.local.VaccinesScheduleLocalDataSource;
import com.pimo.thea.vaccinesschedulemvp.home.HomeActivity;
import com.pimo.thea.vaccinesschedulemvp.view.DialogLoading;

/**
 * Created by thea on 8/5/2017.
 */

public class OnboardingActivity extends AppCompatActivity implements OnboardingContract.View {

    private OnboardingContract.Presenter presenter;
    private Button btnAcceptTerms;
    private DialogLoading dialogLoading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        presenter = new OnboardingPresenter(this,
                VaccinesScheduleRepository.getInstance(VaccinesScheduleLocalDataSource.getInstance(this)),
                this);

        btnAcceptTerms = (Button) findViewById(R.id.onboarding_btn_use_of_the_terms);
        btnAcceptTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.acceptTerms();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.bind();
    }

    @Override
    public void showHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
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
