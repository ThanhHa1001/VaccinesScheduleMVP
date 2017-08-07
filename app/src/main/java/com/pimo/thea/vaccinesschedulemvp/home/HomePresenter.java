package com.pimo.thea.vaccinesschedulemvp.home;


import android.os.Bundle;

import com.pimo.thea.vaccinesschedulemvp.listmoreinformation.ListMoreInformationFragment;

/**
 * Created by thea on 6/30/2017.
 */

public class HomePresenter implements HomeContract.Presenter {

    private HomeContract.View view;

    public HomePresenter(HomeContract.View view) {
        this.view = view;
    }

    @Override
    public void openViewListChild() {
        view.showTitleToolbarHome();
        view.showListChild();
    }

    @Override
    public void openViewListVaccines() {
        view.showTitleToolbarVaccines();
        Bundle bundle = new Bundle();
        bundle.putInt(ListMoreInformationFragment.REQUEST_LIST, ListMoreInformationFragment.VACCINE_REQUEST);
        view.showListVaccines(bundle);
    }

    @Override
    public void openViewListDiseases() {
        view.showTitleToolbarDisease();
        Bundle bundle = new Bundle();
        bundle.putInt(ListMoreInformationFragment.REQUEST_LIST, ListMoreInformationFragment.DISEASE_REQUEST);
        view.showListDiseases(bundle);
    }

    @Override
    public void openViewListChildcare() {
        view.showTitleToolbarChildcare();
        Bundle bundle = new Bundle();
        bundle.putInt(ListMoreInformationFragment.REQUEST_LIST, ListMoreInformationFragment.CHILDCARE_REQUEST);
        view.showListChildcare(bundle);
    }

    @Override
    public void openViewAppInfo() {
        view.showAppInfo();
    }
}
