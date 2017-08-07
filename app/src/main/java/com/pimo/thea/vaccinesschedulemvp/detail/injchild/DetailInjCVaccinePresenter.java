package com.pimo.thea.vaccinesschedulemvp.detail.injchild;

import android.app.DatePickerDialog;
import android.widget.DatePicker;

import com.pimo.thea.vaccinesschedulemvp.data.InjSchedule;
import com.pimo.thea.vaccinesschedulemvp.data.InjVaccine;
import com.pimo.thea.vaccinesschedulemvp.data.source.VaccinesScheduleDataSource;
import com.pimo.thea.vaccinesschedulemvp.data.source.VaccinesScheduleRepository;

import java.util.Calendar;

/**
 * Created by thea on 8/4/2017.
 */

public class DetailInjCVaccinePresenter implements DetailInjCContract.PresenterCVaccine {

    private long injectionId;
    private VaccinesScheduleRepository repository;
    private DetailInjCContract.ViewCVaccine view;

    private boolean isNewInjVaccine = false;
    private long injVaccineId;


    public DetailInjCVaccinePresenter(long injectionId, VaccinesScheduleRepository repository, DetailInjCContract.ViewCVaccine view) {
        this.injectionId = injectionId;
        this.repository = repository;
        this.view = view;
    }

    @Override
    public void bind() {
        loadInjectionVaccineById();
    }

    @Override
    public void loadInjectionVaccineById() {
        repository.getInjVaccine(injectionId, new VaccinesScheduleDataSource.GetInjVaccineCallback() {
            @Override
            public void onInjVaccineLoaded(InjVaccine injVaccine) {
                injVaccineId = injVaccine.getId();

                view.setTitle(injVaccine.getTitle());
                view.setLotNumber(injVaccine.getLotNumber());
                view.setExpDate(injVaccine.getExpDate());
                view.setDoctorInjected(injVaccine.getDoctorInjected());
                view.setNurseInjected(injVaccine.getNurseInjected());
                view.setPlaceOfVaccination(injVaccine.getPlaceOfVaccination());
            }

            @Override
            public void onDataInjVaccineNotAvailable() {
                isNewInjVaccine = true;
                loadInjectionScheduleById();
            }
        });
    }

    private void loadInjectionScheduleById() {
        repository.getInjSchedule(injectionId, new VaccinesScheduleDataSource.GetInjScheduleCallback() {
            @Override
            public void onInjScheduleLoaded(InjSchedule injSchedule) {
                view.setTitle(injSchedule.getTitle());
            }

            @Override
            public void onDataInjScheduleNotAvailable() {
            }
        });
    }

    @Override
    public void saveInjectionVaccine(InjVaccine injVaccine) {
        if (isNewInjVaccine) {
            createInjVaccine(injVaccine);
        } else {
            updateInjVaccine(injVaccine);
        }
        view.showUpdateSuccessfully();
    }

    private void createInjVaccine(InjVaccine injVaccine) {
        repository.insertInjVaccine(injVaccine);
    }

    private void updateInjVaccine(InjVaccine injVaccine) {
        injVaccine.setId(injVaccineId);
        repository.updateInjVaccine(injVaccine);
    }

    @Override
    public void setupDatePickerDialogExpDate() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                view.setExpDate(calendar.getTimeInMillis());
            }
        };
        view.showDatePickerDialogExpDate(callback, calendar);
    }
}
