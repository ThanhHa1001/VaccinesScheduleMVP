package com.pimo.thea.vaccinesschedulemvp.listmoreinformation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pimo.thea.vaccinesschedulemvp.R;
import com.pimo.thea.vaccinesschedulemvp.data.source.VaccinesScheduleRepository;
import com.pimo.thea.vaccinesschedulemvp.data.source.local.VaccinesScheduleLocalDataSource;
import com.pimo.thea.vaccinesschedulemvp.detail.moreinformation.DetailMoreInformationActivity;
import com.pimo.thea.vaccinesschedulemvp.home.HomeActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thea on 8/5/2017.
 */

public class ListMoreInformationFragment extends Fragment implements ListMoreInformationContract.View {
    public static final String TAG = "ListMoreInformation";
    public static final String REQUEST_LIST = "requestList";
    public static final int DISEASE_REQUEST = 1;
    public static final int VACCINE_REQUEST = 2;
    public static final int CHILDCARE_REQUEST = 3;

    private ListMoreInformationContract.Presenter presenter;
    private ListMoreInformationAdapter adapter;
    private RecyclerView rvContent;
    private int requestCode;

    public ListMoreInformationFragment() {

    }

    public static ListMoreInformationFragment newInstance() {
        return new ListMoreInformationFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HomeActivity) {
            Bundle b = getArguments();
            requestCode = b.getInt(REQUEST_LIST, -1);
            Log.d(TAG, "request code: " + requestCode);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.bind();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_more_information, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new ListMoreInformationPresenter(requestCode,
                VaccinesScheduleRepository.getInstance(VaccinesScheduleLocalDataSource.getInstance(getContext())),
                this);

        adapter = new ListMoreInformationAdapter(getContext(), new ArrayList<Object>(0), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListMoreInformationViewHolder vH = (ListMoreInformationViewHolder) view.getTag();
                showDetailMoreInformation(vH.request, vH.objectId);
            }
        });

        rvContent = (RecyclerView) view.findViewById(R.id.list_more_information_rv_content);
        rvContent.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rvContent.setAdapter(adapter);
    }

    private void showDetailMoreInformation(int request, long objectId) {
        Intent intent = new Intent(getActivity(), DetailMoreInformationActivity.class);
        intent.putExtra(DetailMoreInformationActivity.INFORMATION_REQUEST, request);
        intent.putExtra(DetailMoreInformationActivity.INFORMATION_ID_REQUEST, objectId);
        startActivity(intent);
    }

    @Override
    public void showList(List<Object> objects) {
        Log.d(TAG, "show list");
        adapter.replaceData(objects);
    }

    @Override
    public void showNoList() {

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}