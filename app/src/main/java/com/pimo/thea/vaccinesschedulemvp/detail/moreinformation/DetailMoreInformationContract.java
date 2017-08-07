package com.pimo.thea.vaccinesschedulemvp.detail.moreinformation;

import com.pimo.thea.vaccinesschedulemvp.BasePresenter;
import com.pimo.thea.vaccinesschedulemvp.BaseView;

/**
 * Created by thea on 8/6/2017.
 */

public interface DetailMoreInformationContract {

    interface View extends BaseView {

        void setTitle(String title);

        void setTitle1(String title1);

        void setTitle2(String title2);

        void setHideContent(boolean visible);

        void setContent1(String content1);

        void setContent2(String content2);

        void showError();

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void loadObject(int request, long objectId);

    }
}
