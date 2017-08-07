package com.pimo.thea.vaccinesschedulemvp.detail.moreinformation;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pimo.thea.vaccinesschedulemvp.R;
import com.pimo.thea.vaccinesschedulemvp.data.source.VaccinesScheduleRepository;
import com.pimo.thea.vaccinesschedulemvp.data.source.local.VaccinesScheduleLocalDataSource;

/**
 * Created by thea on 8/6/2017.
 */

public class DetailMoreInformationFragment extends Fragment implements DetailMoreInformationContract.View {
    public static final String TAG = "DetailMIFragment";

    private DetailMoreInformationContract.Presenter presenter;
    private int request;
    private long objectId;

    private TextView txtTitle;
    private TextView txtTitle1;
    private TextView txtTitle2;
    private LinearLayout llContent;
    private TextView txtContent1;
    private TextView txtContent2;

    public DetailMoreInformationFragment() {

    }

    public static DetailMoreInformationFragment newInstance() {
        return new DetailMoreInformationFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DetailMoreInformationActivity) {
            Bundle b = getArguments();
            request = b.getInt(DetailMoreInformationActivity.INFORMATION_REQUEST);
            objectId = b.getLong(DetailMoreInformationActivity.INFORMATION_ID_REQUEST);
            Log.d("DetailMIFragment", "request: " + request + " objectId: " + objectId);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail_more_information, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtTitle = (TextView) view.findViewById(R.id.detail_m_i_fragment_txt_title);
        txtTitle1 = (TextView) view.findViewById(R.id.detail_m_i_fragment_txt_title_1);
        txtTitle2 = (TextView) view.findViewById(R.id.detail_m_i_fragment_txt_title_2);
        llContent = (LinearLayout) view.findViewById(R.id.detail_m_i_ll_content);
        txtContent1 = (TextView) view.findViewById(R.id.detail_m_i_fragment_txt_content_1);
        txtContent2 = (TextView) view.findViewById(R.id.detail_m_i_fragment_txt_content_2);

        presenter = new DetailMoreInformationPresenter(getContext(),
                request,
                objectId,
                VaccinesScheduleRepository.getInstance(VaccinesScheduleLocalDataSource.getInstance(getContext())),
                this);

    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.bind();
    }

    @Override
    public void setTitle(String title) {
        txtTitle.setText(title);
    }

    @Override
    public void setTitle1(String title1) {
        txtTitle1.setText(title1);
    }

    @Override
    public void setTitle2(String title2) {
        txtTitle2.setText(title2);
    }

    @Override
    public void setHideContent(boolean visible) {
        if (visible) {
            llContent.setVisibility(View.GONE);
        } else {
            llContent.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setContent1(String content1) {
        txtContent1.setText(content1);
    }

    @Override
    public void setContent2(String content2) {
        txtContent2.setText(content2);
    }

    @Override
    public void showError() {
        Log.d("DetailMIFragment", "load error");
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
