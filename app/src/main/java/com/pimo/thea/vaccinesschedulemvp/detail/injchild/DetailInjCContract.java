package com.pimo.thea.vaccinesschedulemvp.detail.injchild;

import android.app.DatePickerDialog;
import android.content.Context;

import com.pimo.thea.vaccinesschedulemvp.BasePresenter;
import com.pimo.thea.vaccinesschedulemvp.data.InjVaccine;

import java.util.Calendar;

/**
 * Created by thea on 8/3/2017.
 */

public interface DetailInjCContract {

    interface ViewCSchedule {

        void setImageIcon(int drawable);

        void setTitle(String title);

        void setShotNumber(int shotNumber);

        void setIsInject(int isInject);

        void showDatePickerDialogDobChild(DatePickerDialog.OnDateSetListener callback, Calendar calendar);

        void setDayInjection(long dayInjection);

        void setNote(String note);

        void showUpdateSuccessfully();

        boolean isActive();
    }

    interface PresenterCSchedule extends BasePresenter {

        void loadInjectionById(boolean forceLoad);

        void updateInjection(int isInject, long dayInjection, String note);

        void setAlarmOfDayInjectionChanged(Context context, long injScheduleId);

        void setupDatePickerDialogDobChild();
    }

    interface ViewCVaccine {

        void setTitle(String title);

        void setLotNumber(String lotNumber);

        void showDatePickerDialogExpDate(DatePickerDialog.OnDateSetListener callback, Calendar calendar);

        void setExpDate(long expDate);

        void setDoctorInjected(String doctorName);

        void setNurseInjected(String nurseName);

        void setPlaceOfVaccination(String placeOfVaccination);

        void showUpdateSuccessfully();

        boolean isActive();
    }

    interface PresenterCVaccine extends BasePresenter {

        void loadInjectionVaccineById();

        void saveInjectionVaccine(InjVaccine injVaccine);

        void setupDatePickerDialogExpDate();
    }
}
