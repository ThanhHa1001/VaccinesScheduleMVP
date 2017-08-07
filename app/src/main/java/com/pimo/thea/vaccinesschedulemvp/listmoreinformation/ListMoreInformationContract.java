package com.pimo.thea.vaccinesschedulemvp.listmoreinformation;

import com.pimo.thea.vaccinesschedulemvp.BasePresenter;
import com.pimo.thea.vaccinesschedulemvp.BaseView;

import java.util.List;

/**
 * Created by thea on 8/5/2017.
 */

public class ListMoreInformationContract {

    interface View extends BaseView {

        void showList(List<Object> objects);

        void showNoList();

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void loadList(int requestCode);

    }

}
