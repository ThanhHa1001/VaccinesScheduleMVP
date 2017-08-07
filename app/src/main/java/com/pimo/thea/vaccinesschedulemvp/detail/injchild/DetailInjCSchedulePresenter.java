package com.pimo.thea.vaccinesschedulemvp.detail.injchild;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.widget.DatePicker;

import com.pimo.thea.vaccinesschedulemvp.data.InjSchedule;
import com.pimo.thea.vaccinesschedulemvp.data.source.VaccinesScheduleDataSource;
import com.pimo.thea.vaccinesschedulemvp.data.source.VaccinesScheduleRepository;
import com.pimo.thea.vaccinesschedulemvp.data.source.local.VaccinesScheduleLocalContract;
import com.pimo.thea.vaccinesschedulemvp.receiver.InjectionAlarmReceiver;
import com.pimo.thea.vaccinesschedulemvp.utils.VaccineHelper;

import java.util.Calendar;

/**
 * Created by thea on 8/3/2017.
 */

public class DetailInjCSchedulePresenter implements DetailInjCContract.PresenterCSchedule {

    private InjectionAlarmReceiver alarmReceiver;
    private VaccinesScheduleRepository repository;
    private DetailInjCContract.ViewCSchedule view;
    private long injectionId;
    private InjSchedule injScheduleObject = null;
    private boolean firstLoad = true;
    private String strVaccineCode;

    public DetailInjCSchedulePresenter(long injectionId, VaccinesScheduleRepository repository, DetailInjCContract.ViewCSchedule view) {
        this.injectionId = injectionId;
        this.repository = repository;
        this.view = view;
    }

    @Override
    public void bind() {
        loadInjectionById(false);
    }

    @Override
    public void loadInjectionById(boolean forceLoad) {
        loadInformationInjectionById(forceLoad || firstLoad);
        firstLoad = false;
    }

    private void loadInformationInjectionById(boolean forceLoad) {
        if (forceLoad) {
        }

        repository.getInjSchedule(injectionId, new VaccinesScheduleDataSource.GetInjScheduleCallback() {
            @Override
            public void onInjScheduleLoaded(InjSchedule injSchedule) {
                injScheduleObject = injSchedule;
                strVaccineCode = injSchedule.getVaccineCode();
                int isInject = injSchedule.getIsInject();
                setImageIconInjection(isInject, strVaccineCode);
                view.setTitle(injSchedule.getTitle());
                view.setShotNumber(injSchedule.getShotNumber());
                view.setIsInject(isInject);
                view.setDayInjection(injSchedule.getDayInjection());
                view.setNote(injSchedule.getNote());
            }

            @Override
            public void onDataInjScheduleNotAvailable() {
                Log.d("DetailCSPresenter", "on data inj schedule not available");
            }
        });
    }

    @Override
    public void updateInjection(int isInject, long dayInjection, String note) {
        Log.d("DetailInjCSchedule", "is inject: " + isInject + " day injection: " + dayInjection + " note: " + note);
        if (injScheduleObject == null) {
            return;
        }
        injScheduleObject.setIsInject(isInject);
        injScheduleObject.setDayInjection(dayInjection);
        injScheduleObject.setNote(note);

        repository.updateInjSchedule(injScheduleObject);

        repository.refreshInjSchedules();

        setImageIconInjection(isInject, strVaccineCode);

        view.showUpdateSuccessfully();
    }

    @Override
    public void setAlarmOfDayInjectionChanged(Context context, long injScheduleId) {
        if (alarmReceiver == null) {
            alarmReceiver = new InjectionAlarmReceiver();
        }
        alarmReceiver.cancelAlarmByInjScheduleId(context, injScheduleId);
        alarmReceiver.setAlarmByInjScheduleId(context, injScheduleId);
    }

    @Override
    public void setupDatePickerDialogDobChild() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                view.setDayInjection(calendar.getTimeInMillis());
            }
        };
        view.showDatePickerDialogDobChild(callback, calendar);
    }

    private void setImageIconInjection(int isInject, String vaccineCode) {
        if (isInject == VaccinesScheduleLocalContract.InjectionScheduleEntry.IS_INJECT_DONE) {
            view.setImageIcon(VaccineHelper.getIconResourcesInjectDone(vaccineCode));
        } else {
            view.setImageIcon(VaccineHelper.getIconResourcesInjectNotDone(vaccineCode));
        }
    }
}
