package com.pimo.thea.vaccinesschedulemvp.home;

import android.os.Bundle;

import com.pimo.thea.vaccinesschedulemvp.BasePresenter;
import com.pimo.thea.vaccinesschedulemvp.BaseView;
import com.pimo.thea.vaccinesschedulemvp.data.Child;

import java.util.List;

/**
 * Created by thea on 6/29/2017.
 */

public interface HomeContract {

    interface View {

        void showListChild();

        void showTitleToolbarHome();

        void showTitleToolbarVaccines();

        void showListVaccines(Bundle bundle);

        void showTitleToolbarDisease();

        void showListDiseases(Bundle bundle);

        void showTitleToolbarChildcare();

        void showListChildcare(Bundle bundle);

        void showTitleToolbarHealthFeed();

        void showHealthFeed();

        void showAppInfo();
    }

    interface Presenter {

        void openViewListChild();

        void openViewListVaccines();

        void openViewListDiseases();

        void openViewListChildcare();

        void openHealthFeed();

        void openViewAppInfo();
    }
}
