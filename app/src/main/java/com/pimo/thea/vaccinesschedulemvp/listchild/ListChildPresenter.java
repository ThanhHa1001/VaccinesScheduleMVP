package com.pimo.thea.vaccinesschedulemvp.listchild;

import android.util.Log;

import com.pimo.thea.vaccinesschedulemvp.data.Child;
import com.pimo.thea.vaccinesschedulemvp.data.InjSchedule;
import com.pimo.thea.vaccinesschedulemvp.data.source.VaccinesScheduleDataSource;
import com.pimo.thea.vaccinesschedulemvp.data.source.VaccinesScheduleRepository;

import java.util.List;

/**
 * Created by thea on 7/1/2017.
 */

public class ListChildPresenter implements ListChildContract.Presenter {

    private VaccinesScheduleRepository repository;

    private ListChildContract.View view;

    private boolean firstLoad = true;

    public ListChildPresenter(VaccinesScheduleRepository repository, ListChildContract.View view) {
        this.repository = repository;
        this.view = view;
    }

    @Override
    public void bind() {
        loadListChild(false);
    }

    @Override
    public void loadListChild(boolean forceUpdate) {
        loadAllListChild(forceUpdate || firstLoad, true);
        firstLoad = false;
    }

    private void loadAllListChild(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            // show loading UI
        }

        if (forceUpdate) {
            repository.refreshChildList();
            Log.d("ListChildPresenter", " refresh child list");
        }

        view.showDialogLoading();

        repository.getChildListWithNextInject(new VaccinesScheduleDataSource.LoadChildListCallback() {
            @Override
            public void onChildLoaded(List<Child> childList) {
                Log.d("ListChildPresenter", " on child loaded");
                if (!view.isActive()) {
                    return;
                }

                if (showLoadingUI) {
                    // show loading UI
                }

                processChild(childList);
            }

            @Override
            public void onDataChildNotAvailable() {
                if (!view.isActive()) {
                    return;
                }

                if (showLoadingUI) {
                    // show loading UI
                }

                view.showLoadingListChildError();
            }
        });
        view.dismissDialogLoading();
    }

    private void processChild(List<Child> childList) {
        if (childList.isEmpty()) {
            view.showNoChild();
        } else {
            view.showListChild(childList);
        }
    }

    @Override
    public void result(int requestCode, int resultCode) {
        // If a child was successfully added, show snackbar
        //TODO: result - ListChildPresenter
    }

    @Override
    public void addNewChild() {
        view.showAddChild();
    }
}
