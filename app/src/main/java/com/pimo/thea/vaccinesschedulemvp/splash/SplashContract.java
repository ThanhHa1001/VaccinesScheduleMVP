package com.pimo.thea.vaccinesschedulemvp.splash;

import com.pimo.thea.vaccinesschedulemvp.BasePresenter;
import com.pimo.thea.vaccinesschedulemvp.BaseView;

/**
 * Created by thea on 8/5/2017.
 */

public class SplashContract {

    interface View extends BaseView {

        void showHomeActivity();

        void showUseOfTheTerms();

        void hideImageViewIcon();

        void showDialogLoading();

        void dismissDialogLoading();

    }

    interface Presenter extends BasePresenter {

        void getAcceptTerms();

        void acceptTerms();

    }
}
