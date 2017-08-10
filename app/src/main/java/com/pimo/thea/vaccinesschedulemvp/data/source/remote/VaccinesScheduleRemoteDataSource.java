package com.pimo.thea.vaccinesschedulemvp.data.source.remote;

import android.content.Context;

import com.pimo.thea.vaccinesschedulemvp.data.Child;
import com.pimo.thea.vaccinesschedulemvp.data.Childcare;
import com.pimo.thea.vaccinesschedulemvp.data.Disease;
import com.pimo.thea.vaccinesschedulemvp.data.InjSchedule;
import com.pimo.thea.vaccinesschedulemvp.data.InjVaccine;
import com.pimo.thea.vaccinesschedulemvp.data.Vaccine;
import com.pimo.thea.vaccinesschedulemvp.data.source.VaccinesScheduleDataSource;

/**
 * Created by thea on 8/10/2017.
 */

public class VaccinesScheduleRemoteDataSource implements VaccinesScheduleDataSource{
    private static VaccinesScheduleRemoteDataSource INSTANCE;

    public static VaccinesScheduleRemoteDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new VaccinesScheduleRemoteDataSource(context);
        }
        return INSTANCE;
    }

    private VaccinesScheduleRemoteDataSource(Context context) {

    }

    @Override
    public void getChildList(LoadChildListCallback loadChildListCallback) {

    }

    @Override
    public void getChildListWithNextInject(LoadChildListCallback loadChildListCallback) {

    }

    @Override
    public void getChild(long childId, GetChildCallback getChildCallback) {

    }

    @Override
    public void getChildWithNextInject(long childId, GetChildCallback getChildCallback) {

    }

    @Override
    public long insertChild(Child child) {
        return 0;
    }

    @Override
    public void updateChild(Child child) {

    }

    @Override
    public void deleteChild(long childId) {

    }

    @Override
    public void deleteAllChildList() {

    }

    @Override
    public void refreshChildList() {

    }

    @Override
    public void getChildcares(LoadChildcaresCallback loadChildcaresCallback) {

    }

    @Override
    public void getChildcare(long childcareId, GetChildcareCallback getChildcareCallback) {

    }

    @Override
    public long insertChildcare(Childcare childcare) {
        return 0;
    }

    @Override
    public void updateChildcare(Childcare childcare) {

    }

    @Override
    public void refreshChildcares() {

    }

    @Override
    public void getDiseases(LoadDiseasesCallback loadDiseasesCallback) {

    }

    @Override
    public void getDisease(long diseaseId, GetDiseaseCallback getDiseaseCallback) {

    }

    @Override
    public long insertDisease(Disease disease) {
        return 0;
    }

    @Override
    public void updateDisease(Disease disease) {

    }

    @Override
    public void refreshDiseases() {

    }

    @Override
    public void getInjSchedulesByChildIdAndChildDob(long childId, long childDob, LoadInjSchedulesByChildIdCallback loadInjSchedulesCallback) {

    }

    @Override
    public void getInjSchedulesByChildId(long childId, LoadInjSchedulesByChildIdCallback loadInjSchedulesByChildIdCallback) {

    }

    @Override
    public void getInjSchedule(long injScheduleID, GetInjScheduleCallback getInjScheduleCallback) {

    }

    @Override
    public long insertInjSchedule(InjSchedule injSchedule) {
        return 0;
    }

    @Override
    public void updateInjSchedule(InjSchedule injSchedule) {

    }

    @Override
    public void updateInjScheduleNotified(long injScheduleId) {

    }

    @Override
    public void deleteInjScheduleByChildId(long childId) {

    }

    @Override
    public void refreshInjSchedules() {

    }

    @Override
    public void getInjVaccines(LoadInjVaccinesCallback loadInjVaccinesCallback) {

    }

    @Override
    public void getInjVaccine(long injScheduleId, GetInjVaccineCallback getInjVaccineCallback) {

    }

    @Override
    public long insertInjVaccine(InjVaccine injVaccine) {
        return 0;
    }

    @Override
    public void updateInjVaccine(InjVaccine injVaccine) {

    }

    @Override
    public void deleteInjVaccineByInjScheduleId(long injScheduleId) {

    }

    @Override
    public void refreshInjVaccines() {

    }

    @Override
    public void getVaccines(LoadVaccinesCallback loadVaccinesCallback) {

    }

    @Override
    public void getVaccine(long vaccineId, GetVaccineCallback getVaccineCallback) {

    }

    @Override
    public long insertVaccine(Vaccine vaccine) {
        return 0;
    }

    @Override
    public void updateVaccine(Vaccine vaccine) {

    }

    @Override
    public void refreshVaccines() {

    }

    @Override
    public void getChildInjScheduleList(LoadChildInjSchedulesCallback loadChildInjSchedulesCallback) {

    }

}
