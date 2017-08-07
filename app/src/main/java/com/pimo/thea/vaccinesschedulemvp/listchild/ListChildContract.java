package com.pimo.thea.vaccinesschedulemvp.listchild;

import com.pimo.thea.vaccinesschedulemvp.BasePresenter;
import com.pimo.thea.vaccinesschedulemvp.BaseView;
import com.pimo.thea.vaccinesschedulemvp.data.Child;

import java.util.List;

/**
 * Created by thea on 7/1/2017.
 */

public interface ListChildContract {

    interface View {

        void showDialogLoading();

        void dismissDialogLoading();

        void showListChild(List<Child> childList);

        void showNoChild();

        void showAddChild();

        void showLoadingListChildError();

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void loadListChild(boolean forceUpdate);

        void result(int requestCode, int resultCode);

        void addNewChild();
    }
}
