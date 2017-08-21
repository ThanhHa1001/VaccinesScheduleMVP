package com.pimo.thea.vaccinesschedulemvp.detail.healthfeed;

import com.pimo.thea.vaccinesschedulemvp.BasePresenter;
import com.pimo.thea.vaccinesschedulemvp.BaseView;

/**
 * Created by thea on 8/13/2017.
 */

public interface DetailHealthFeedContract {

    interface View extends BaseView {

        void showLoadingHealthFeed();

        void hideLoadingHealthFeed();

        void showTitleHealthFeed(String title);

        void showAskHealthFeed(String ask);

        void showAnswerHealthFeed(String answer);

        void showNoDataHealthFeed();

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void getHealthFeedFromUrl();
    }
}
