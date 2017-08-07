package com.pimo.thea.vaccinesschedulemvp.listchild;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.pimo.thea.vaccinesschedulemvp.R;
import com.pimo.thea.vaccinesschedulemvp.data.Child;
import com.pimo.thea.vaccinesschedulemvp.data.source.VaccinesScheduleRepository;
import com.pimo.thea.vaccinesschedulemvp.data.source.local.VaccinesScheduleLocalDataSource;
import com.pimo.thea.vaccinesschedulemvp.detail.injschedulechild.DetailInjSActivity;
import com.pimo.thea.vaccinesschedulemvp.detail.injschedulechild.DetailInjSFragment;
import com.pimo.thea.vaccinesschedulemvp.editorchilds.EditorChildActivity;
import com.pimo.thea.vaccinesschedulemvp.view.DialogLoading;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thea on 6/30/2017.
 */

public class ListChildFragment extends Fragment implements ListChildContract.View {
    public static final String TAG = "ListChildFragment";

    private ListChildContract.Presenter presenter;

    private RelativeLayout rlListChild;
    private LinearLayout llListChildEmpty;

    private RecyclerView rvContent;
    private ListChildAdapter listChildAdapter;

    private DialogLoading dialogLoading;

    public ListChildFragment() {

    }

    public static ListChildFragment newInstance() {
        return new ListChildFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_child, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new ListChildPresenter(
                VaccinesScheduleRepository.getInstance(VaccinesScheduleLocalDataSource.getInstance(getContext())),
                this);

        rlListChild = (RelativeLayout) view.findViewById(R.id.rl_list_child);
        llListChildEmpty = (LinearLayout) view.findViewById(R.id.ll_list_child_empty);

        FloatingActionButton fabAddChild = (FloatingActionButton) view.findViewById(R.id.fab_list_child);
        fabAddChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.addNewChild();
            }
        });

        listChildAdapter = new ListChildAdapter(getContext(), new ArrayList<Child>(0), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListChildViewHolder listChildViewHolder = (ListChildViewHolder) view.getTag();
                showInjScheduleChildDetail(listChildViewHolder.childId, listChildViewHolder.dob);
            }
        });

        rvContent = (RecyclerView) view.findViewById(R.id.rv_content_list_child);
        rvContent.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rvContent.setAdapter(listChildAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.bind();
    }

    @Override
    public void showDialogLoading() {
        dialogLoading = new DialogLoading(getContext(), getString(R.string.dialog_loading_loading));
        dialogLoading.show();
    }

    @Override
    public void dismissDialogLoading() {
        dialogLoading.dismiss();
    }

    @Override
    public void showListChild(List<Child> childList) {
        Log.d(TAG, "show list child() method");

        listChildAdapter.replaceData(childList);
        llListChildEmpty.setVisibility(View.GONE);
        rlListChild.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNoChild() {
        rlListChild.setVisibility(View.GONE);
        llListChildEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public void showAddChild() {
        Intent intent = new Intent(getActivity(), EditorChildActivity.class);
        startActivity(intent);
    }

    @Override
    public void showLoadingListChildError() {

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    private void showInjScheduleChildDetail(long childId, long dob) {
        Intent intent = new Intent(getActivity(), DetailInjSActivity.class);
        intent.putExtra(DetailInjSFragment.CHILD_ID_ARG, childId);
        intent.putExtra(DetailInjSFragment.CHILD_DOB_ARG, dob);
        startActivity(intent);
    }
}