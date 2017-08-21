package com.pimo.thea.vaccinesschedulemvp.heathfeed;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.pimo.thea.vaccinesschedulemvp.data.HealthFeed;
import com.pimo.thea.vaccinesschedulemvp.data.source.VaccinesScheduleDataSource;
import com.pimo.thea.vaccinesschedulemvp.data.source.VaccinesScheduleRepository;

import java.util.List;

/**
 * Created by thea on 8/9/2017.
 */

public class HealthFeedPresenter implements HealthFeedContract.Presenter {
    private VaccinesScheduleRepository repository;
    private HealthFeedContract.View view;
    private Context context;

    public HealthFeedPresenter(Context context, VaccinesScheduleRepository repository, HealthFeedContract.View view) {
        this.context = context;
        this.repository = repository;
        this.view = view;
    }

    @Override
    public void bind() {
        loadHealthFeeds(false, 1);
    }

    @Override
    public void loadHealthFeeds(boolean forceLoad, int numberPage) {
//        view.showProgressLoading();
        if (!checkStateNetworkConnectivity()) {
            view.showNoInternetConnection();
            return;
        }

        loadHealthFeeds(forceLoad, true, numberPage);
    }

    private void loadHealthFeeds(boolean forceLoad, final boolean showLoadingUI, int numberPage) {
        if (showLoadingUI) {
            view.showLoadingIndicator(showLoadingUI);
        }

        if (forceLoad) {
            repository.refreshHealthFeeds(false); // false == isBookmark
        }

        boolean isBookmark = false;
        Log.d("HealthFeedPresenter", "number page: " + numberPage);
        repository.getHealthFeeds(numberPage, isBookmark, new VaccinesScheduleDataSource.LoadHealthFeedsCallback() {
            @Override
            public void onHealthFeedsLoaded(List<HealthFeed> healthFeeds) {
                if (!view.isActive()) {
                    return;
                }

                if (showLoadingUI) {
                    view.showLoadingIndicator(false);
                }

                view.showHealthFeed(healthFeeds);
            }

            @Override
            public void onDataHealthFeedsNotAvailable() {
                Log.d("HealthFeedPresenter", "Data not available");
                view.showNoHealthFeed();
                view.showLoadingIndicator(false);
            }
        });

    }

    @Override
    public void addToBookmark(HealthFeed healthFeed, final int numberPage) {
        if (healthFeed.isBookmark()) {
            repository.deleteHealthFeed(healthFeed);
            loadHealthFeeds(false, false, numberPage);
        } else {
            repository.getHealthFeed(healthFeed.getUrl(), new VaccinesScheduleDataSource.GetHealthFeedCallback() {
                @Override
                public void onHealthFeedLoaded(HealthFeed healthFeed) {
                    healthFeed.setBookmark(true);
                    repository.insertHealthFeed(healthFeed);
                    loadHealthFeeds(false, false, numberPage);
                }

                @Override
                public void onDataHealthFeedNotAvailable() {

                }
            });
        }
    }

    @Override
    public void shareHealthFeed(String url) {
        view.showShareData(url);
    }

    @Override
    public void showDetailHealthFeed(String url) {
        view.showDetailHealthFeed(url);
    }

    @Override
    public boolean checkStateNetworkConnectivity() {
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
}
