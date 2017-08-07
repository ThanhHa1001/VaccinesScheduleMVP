package com.pimo.thea.vaccinesschedulemvp.listmoreinformation;

import android.util.Log;

import com.pimo.thea.vaccinesschedulemvp.data.Vaccine;
import com.pimo.thea.vaccinesschedulemvp.data.source.VaccinesScheduleDataSource;
import com.pimo.thea.vaccinesschedulemvp.data.source.VaccinesScheduleRepository;

import java.util.List;

/**
 * Created by thea on 8/5/2017.
 */

public class ListMoreInformationPresenter implements ListMoreInformationContract.Presenter {

    private VaccinesScheduleRepository repository;
    private ListMoreInformationContract.View view;
    private int requestCode;

    public ListMoreInformationPresenter(int requestCode, VaccinesScheduleRepository repository, ListMoreInformationContract.View view) {
        this.requestCode = requestCode;
        this.repository = repository;
        this.view = view;
    }

    @Override
    public void bind() {
        loadList(requestCode);
    }

    @Override
    public void loadList(int requestCode) {
        Log.d("ListMIPresenter", "requestCode: " + requestCode);
        if (requestCode == -1) {
            return;
        }

        if (requestCode == ListMoreInformationFragment.DISEASE_REQUEST) {
            Log.d("ListMIPresenter", "disease");
            repository.getDiseases(new VaccinesScheduleDataSource.LoadDiseasesCallback() {
                @Override
                public void onDiseaseLoaded(List<Object> diseases) {
                    Log.d("ListMIPresenter", "disease size:" + diseases.size());
                    view.showList(diseases);
                }

                @Override
                public void onDataDiseaseNotAvailable() {
                    Log.d("ListMIPresenter", "data not available");
                    view.showNoList();
                }
            });
        } else if (requestCode == ListMoreInformationFragment.VACCINE_REQUEST) {
            repository.getVaccines(new VaccinesScheduleDataSource.LoadVaccinesCallback() {
                @Override
                public void onVaccineLoaded(List<Object> vaccines) {
                    view.showList(vaccines);
                }

                @Override
                public void onDataVaccineNotAvailable() {
                    view.showNoList();
                }
            });
        } else {
            repository.getChildcares(new VaccinesScheduleDataSource.LoadChildcaresCallback() {
                @Override
                public void onChildcareLoaded(List<Object> childcares) {
                    view.showList(childcares);
                }

                @Override
                public void onDataChildcareNotAvailable() {
                    view.showNoList();
                }
            });
        }
    }
}
