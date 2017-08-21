package com.pimo.thea.vaccinesschedulemvp.heathfeed;

import com.pimo.thea.vaccinesschedulemvp.BasePresenter;
import com.pimo.thea.vaccinesschedulemvp.BaseView;
import com.pimo.thea.vaccinesschedulemvp.data.HealthFeed;

import java.util.List;

/**
 * Created by thea on 8/9/2017.
 */

public interface HealthFeedContract {

    interface View extends BaseView {

        void showNoInternetConnection();

        void showLoadingIndicator(boolean active);

        void showHealthFeed(List<HealthFeed> healthFeeds);

        void showNoHealthFeed();

        void showShareData(String url);

        void showDetailHealthFeed(String url);

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void loadHealthFeeds(boolean forceLoad, int numberPage);

        void addToBookmark(HealthFeed healthFeed, int numberPage);

        void shareHealthFeed(String url);

        void showDetailHealthFeed(String url);

        boolean checkStateNetworkConnectivity();
    }
}
