package com.pimo.thea.vaccinesschedulemvp.detail.healthfeed;

import com.pimo.thea.vaccinesschedulemvp.data.HealthFeed;
import com.pimo.thea.vaccinesschedulemvp.data.source.VaccinesScheduleDataSource;
import com.pimo.thea.vaccinesschedulemvp.data.source.VaccinesScheduleRepository;

/**
 * Created by thea on 8/13/2017.
 */

public class DetailHealthFeedPresenter implements DetailHealthFeedContract.Presenter {
    private VaccinesScheduleRepository repository;
    private DetailHealthFeedContract.View view;
    private String url;

    public DetailHealthFeedPresenter(String url, VaccinesScheduleRepository repository, DetailHealthFeedContract.View view) {
        this.url = url;
        this.repository = repository;
        this.view = view;
    }

    @Override
    public void bind() {
        getHealthFeedFromUrl();
    }


    @Override
    public void getHealthFeedFromUrl() {
        view.showLoadingHealthFeed();
        
        repository.getHealthFeed(url, new VaccinesScheduleDataSource.GetHealthFeedCallback() {
            @Override
            public void onHealthFeedLoaded(HealthFeed healthFeed) {
                if (!view.isActive()) {
                    return;
                }

                view.hideLoadingHealthFeed();
                view.showTitleHealthFeed(healthFeed.getTitleAsk());
                view.showAskHealthFeed(healthFeed.getContentAsk());
                view.showAnswerHealthFeed(healthFeed.getContentAnswer());
            }

            @Override
            public void onDataHealthFeedNotAvailable() {
                view.showNoDataHealthFeed();
            }
        });
    }
}
