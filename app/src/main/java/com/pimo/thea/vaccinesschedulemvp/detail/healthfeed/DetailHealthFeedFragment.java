package com.pimo.thea.vaccinesschedulemvp.detail.healthfeed;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.pimo.thea.vaccinesschedulemvp.R;
import com.pimo.thea.vaccinesschedulemvp.data.source.VaccinesScheduleRepository;
import com.pimo.thea.vaccinesschedulemvp.data.source.local.VaccinesScheduleLocalDataSource;
import com.pimo.thea.vaccinesschedulemvp.data.source.remote.VaccinesScheduleRemoteDataSource;

/**
 * Created by thea on 8/13/2017.
 */

public class DetailHealthFeedFragment extends Fragment implements DetailHealthFeedContract.View {
    public static final String URL_REQUEST = "url_request";
    public static final String TAG = "DetailHFFragment";
    private String url;
    private DetailHealthFeedContract.Presenter presenter;

    private ScrollView svContent;
    private ProgressBar pbLoading;
    private TextView txtTitle;
    private TextView txtAsk;
    private TextView txtAnswer;

    public DetailHealthFeedFragment() {

    }

    public static DetailHealthFeedFragment getInstance() {
        return new DetailHealthFeedFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getArguments();
        url = b.getString(URL_REQUEST);
        Log.d(TAG, "Url: " + url);

        presenter = new DetailHealthFeedPresenter(url,
                VaccinesScheduleRepository.getInstance(
                        VaccinesScheduleRemoteDataSource.getInstance(getContext()),
                        VaccinesScheduleLocalDataSource.getInstance(getContext())),
                this);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.bind();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail_health_feed, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        svContent = (ScrollView) view.findViewById(R.id.detail_health_feed_fragment_scroll_view);
        pbLoading = (ProgressBar) view.findViewById(R.id.detail_health_feed_fragment_progress_bar);
        txtTitle = (TextView) view.findViewById(R.id.detail_health_feed_fragment_txt_title);
        txtAsk = (TextView) view.findViewById(R.id.detail_health_feed_fragment_txt_ask);
        txtAnswer = (TextView) view.findViewById(R.id.detail_health_feed_fragment_txt_answer);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detail_health_feed_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.detail_health_feed_open_web) {
            Log.d(TAG, "Open web");
            //create an intent builder
            CustomTabsIntent.Builder customTabsBuilder = new CustomTabsIntent.Builder();

            // Begin customizing
            // set toolbar colors
            customTabsBuilder.setToolbarColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
            customTabsBuilder.setSecondaryToolbarColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));

            CustomTabsIntent customTabsIntent = customTabsBuilder.build();

            String fullUrl = "http://tudu.com.vn" + url;
            customTabsIntent.launchUrl(getActivity(), Uri.parse(fullUrl));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showLoadingHealthFeed() {
        pbLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingHealthFeed() {
        pbLoading.setVisibility(View.GONE);
    }

    @Override
    public void showTitleHealthFeed(String title) {
        txtTitle.setText(title);
    }

    @Override
    public void showAskHealthFeed(String ask) {
        txtAsk.setText(ask);
    }

    @Override
    public void showAnswerHealthFeed(String answer) {
        txtAnswer.setText(answer);
    }

    @Override
    public void showNoDataHealthFeed() {

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
