package com.pimo.thea.vaccinesschedulemvp.detail.injschedulechild;

import com.pimo.thea.vaccinesschedulemvp.BasePresenter;
import com.pimo.thea.vaccinesschedulemvp.data.Child;

import java.util.List;

/**
 * Created by thea on 8/3/2017.
 */

public interface DetailInjSContract {

    interface View {

        void showFullnameChildInAppbarLayout(String fullname);

        void showInformationChildInAppBarLayout(Child child);

        void showNoInformationChildInAppBarLayout();

        void showListInjScheduleByChildID(List<Object> objects);

        void showNoInjScheduleByChildID();

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void loadChildById();

        void loadListInjSchedulesByChildID(boolean forceLoad);

        void result(int requestCode, int resultCode);
    }
}
