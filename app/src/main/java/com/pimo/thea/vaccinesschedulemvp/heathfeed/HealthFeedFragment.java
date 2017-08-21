package com.pimo.thea.vaccinesschedulemvp.heathfeed;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.pimo.thea.vaccinesschedulemvp.R;
import com.pimo.thea.vaccinesschedulemvp.data.HealthFeed;
import com.pimo.thea.vaccinesschedulemvp.data.source.VaccinesScheduleRepository;
import com.pimo.thea.vaccinesschedulemvp.data.source.local.VaccinesScheduleLocalDataSource;
import com.pimo.thea.vaccinesschedulemvp.data.source.remote.VaccinesScheduleRemoteDataSource;
import com.pimo.thea.vaccinesschedulemvp.detail.healthfeed.DetailHealthFeedActivity;
import com.pimo.thea.vaccinesschedulemvp.detail.healthfeed.DetailHealthFeedFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thea on 8/9/2017.
 */

public class HealthFeedFragment extends Fragment implements HealthFeedContract.View {
    public static final String TAG = "HealthFeedF";
    private static int NUMBER_PAGE = 1;
    private HealthFeedContract.Presenter presenter;
    private HealthFeedAdapter healthFeedAdapter;
    private RecyclerView rvContent;
    private RelativeLayout rlNoInternetConnection;
    private Button btnRetryConnectInternet;

    public HealthFeedFragment() {

    }

    public static HealthFeedFragment newInstance() {
        return new HealthFeedFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new HealthFeedPresenter(getContext(),
                VaccinesScheduleRepository.getInstance(
                        VaccinesScheduleRemoteDataSource.getInstance(getContext()),
                        VaccinesScheduleLocalDataSource.getInstance(getContext())),
                this);

        healthFeedAdapter = new HealthFeedAdapter(getContext(), new ArrayList<HealthFeed>(0));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_health_feed, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rlNoInternetConnection = (RelativeLayout) view.findViewById(R.id.health_feed_rl_no_internet_connection);
        rvContent = (RecyclerView) view.findViewById(R.id.health_feed_rv_content);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvContent.setLayoutManager(linearLayoutManager);
        rvContent.setHasFixedSize(true);
        rvContent.setAdapter(healthFeedAdapter);

        btnRetryConnectInternet = (Button) view.findViewById(R.id.btn_health_feed_retry_connect_internet);
        btnRetryConnectInternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.bind();
            }
        });

        ScrollChildSwipeRefreshLayout swipeRefreshLayout =
                (ScrollChildSwipeRefreshLayout) view.findViewById(R.id.health_feed_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );
        // Set the scrolling view in the custom SwipeRefreshLayout.
        swipeRefreshLayout.setScrollUpChild(rvContent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG, "onRefresh");
                NUMBER_PAGE = 1;
                presenter.loadHealthFeeds(true, NUMBER_PAGE);
            }
        });

        healthFeedAdapter.setHealthyFeedItemClickListener(new HealthFeedAdapter.HealthFeedItemClickListener() {
            @Override
            public void onShareHealthFeed(String url) {
                presenter.shareHealthFeed(url);
            }

            @Override
            public void onAddBookmark(HealthFeed healthFeed) {
                presenter.addToBookmark(healthFeed, NUMBER_PAGE);
            }

            @Override
            public void onHealthFeedClick(String url) {
                presenter.showDetailHealthFeed(url);
            }

            @Override
            public void onLoadMore(int position) {
                presenter.loadHealthFeeds(false, NUMBER_PAGE);
                NUMBER_PAGE++;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.bind();
    }

    @Override
    public void onPause() {
        super.onPause();
        showLoadingIndicator(false);
    }

    @Override
    public void showNoInternetConnection() {
        rlNoInternetConnection.setVisibility(View.VISIBLE);
        rvContent.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showLoadingIndicator(final boolean active) {
        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout srl = (SwipeRefreshLayout) getView().findViewById(R.id.health_feed_refresh_layout);
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(active);
            }
        });
    }

    @Override
    public void showHealthFeed(List<HealthFeed> healthFeeds) {
        rvContent.setVisibility(View.VISIBLE);
        rlNoInternetConnection.setVisibility(View.INVISIBLE);
        Log.d(TAG, "size list: " + healthFeeds.size());
        healthFeedAdapter.replaceData(healthFeeds);
    }

    @Override
    public void showNoHealthFeed() {
        Log.d(TAG, "Not health feed");
    }

    @Override
    public void showShareData(String url) {
        String fullUrl = "Chăm sóc bé" + "\nhttp://tudu.com.vn" + url;
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, fullUrl);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    @Override
    public void showDetailHealthFeed(String url) {
        Intent intent = new Intent(getContext(), DetailHealthFeedActivity.class);
        intent.putExtra(DetailHealthFeedFragment.URL_REQUEST, url);
        startActivity(intent);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
