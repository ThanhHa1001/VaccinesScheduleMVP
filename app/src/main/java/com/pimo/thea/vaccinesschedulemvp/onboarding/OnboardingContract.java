package com.pimo.thea.vaccinesschedulemvp.onboarding;

import com.pimo.thea.vaccinesschedulemvp.BasePresenter;
import com.pimo.thea.vaccinesschedulemvp.BaseView;

/**
 * Created by thea on 8/5/2017.
 */

public class OnboardingContract {

    interface View extends BaseView {

        void showHome();

        void showDialogLoading();

        void dismissDialogLoading();

    }

    interface Presenter extends BasePresenter {

        void getAcceptTerms();

        void acceptTerms();

    }
}
