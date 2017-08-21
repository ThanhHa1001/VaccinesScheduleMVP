package com.pimo.thea.vaccinesschedulemvp.data.source;

import com.pimo.thea.vaccinesschedulemvp.data.Child;
import com.pimo.thea.vaccinesschedulemvp.data.ChildInjSchedule;
import com.pimo.thea.vaccinesschedulemvp.data.Childcare;
import com.pimo.thea.vaccinesschedulemvp.data.Disease;
import com.pimo.thea.vaccinesschedulemvp.data.HealthFeed;
import com.pimo.thea.vaccinesschedulemvp.data.InjSchedule;
import com.pimo.thea.vaccinesschedulemvp.data.InjVaccine;
import com.pimo.thea.vaccinesschedulemvp.data.Vaccine;

import java.util.List;

/**
 * Created by thea on 6/30/2017.
 */

public interface VaccinesScheduleDataSource {

    void getChildList(LoadChildListCallback loadChildListCallback);

    void getChildListWithNextInject(LoadChildListCallback loadChildListCallback);

    void getChild(long childId, GetChildCallback getChildCallback);

    void getChildWithNextInject(long childId, GetChildCallback getChildCallback);

    long insertChild(Child child);

    void updateChild(Child child);

    void deleteChild(long childId);

    void deleteAllChildList();

    void refreshChildList();

    void getChildcares(LoadChildcaresCallback loadChildcaresCallback);

    void getChildcare(long childcareId, GetChildcareCallback getChildcareCallback);

    long insertChildcare(Childcare childcare);

    void updateChildcare(Childcare childcare);

    void refreshChildcares();

    void getDiseases(LoadDiseasesCallback loadDiseasesCallback);

    void getDisease(long diseaseId, GetDiseaseCallback getDiseaseCallback);

    long insertDisease(Disease disease);

    void updateDisease(Disease disease);

    void refreshDiseases();

    void getInjSchedulesByChildIdAndChildDob(long childId, long childDob, LoadInjSchedulesByChildIdCallback loadInjSchedulesCallback);

    void getInjSchedulesByChildId(long childId, LoadInjSchedulesByChildIdCallback loadInjSchedulesByChildIdCallback);

    void getInjSchedule(long injScheduleID, GetInjScheduleCallback getInjScheduleCallback);

    long insertInjSchedule(InjSchedule injSchedule);

    void updateInjSchedule(InjSchedule injSchedule);

    void updateInjScheduleNotified(long injScheduleId);

    void deleteInjScheduleByChildId(long childId);

    void refreshInjSchedules();

    void getInjVaccines(LoadInjVaccinesCallback loadInjVaccinesCallback);

    void getInjVaccine(long injScheduleId, GetInjVaccineCallback getInjVaccineCallback);

    long insertInjVaccine(InjVaccine injVaccine);

    void updateInjVaccine(InjVaccine injVaccine);

    void deleteInjVaccineByInjScheduleId(long injScheduleId);

    void refreshInjVaccines();

    void getVaccines(LoadVaccinesCallback loadVaccinesCallback);

    void getVaccine(long vaccineId, GetVaccineCallback getVaccineCallback);

    long insertVaccine(Vaccine vaccine);

    void updateVaccine(Vaccine vaccine);

    void refreshVaccines();

    void getChildInjScheduleList(LoadChildInjSchedulesCallback loadChildInjSchedulesCallback);

    void getHealthFeeds(int numberPage, boolean isBookmark, LoadHealthFeedsCallback loadHealthFeedsCallback);

    void getHealthFeed(String url, GetHealthFeedCallback getHealthFeedCallback);

    long insertHealthFeed(HealthFeed healthFeed);

    void deleteHealthFeed(HealthFeed healthFeed);

    void refreshHealthFeeds(boolean isBookmark);

    // Child
    interface LoadChildListCallback {

        void onChildLoaded(List<Child> childList);

        void onDataChildNotAvailable();
    }

    interface GetChildCallback {

        void onChildLoaded(Child child);

        void onDataChildNotAvailable();
    }

    // Childcare
    interface LoadChildcaresCallback {

        void onChildcareLoaded(List<Object> childcares);

        void onDataChildcareNotAvailable();
    }

    interface GetChildcareCallback {

        void onChildcareLoaded(Childcare childcare);

        void onDataChildcareNotAvailable();
    }

    // Disease
    interface LoadDiseasesCallback {

        void onDiseaseLoaded(List<Object> objects);

        void onDataDiseaseNotAvailable();
    }

    interface GetDiseaseCallback {

        void onDiseaseLoaded(Disease disease);

        void onDataDiseaseNotAvailable();
    }

    // Injection schedule
    interface LoadInjSchedulesByChildIdCallback {

        void onInjSchedulesLoaded(List<InjSchedule> injSchedules);

        void onDataInjScheduleNotAvailable();
    }

    interface GetInjScheduleCallback {

        void onInjScheduleLoaded(InjSchedule injSchedule);

        void onDataInjScheduleNotAvailable();
    }

    // Injection vaccine
    interface LoadInjVaccinesCallback {

        void onInjVaccineLoaded(List<InjVaccine> injVaccines);

        void onDataInjVaccineNotAvailable();
    }

    interface GetInjVaccineCallback {

        void onInjVaccineLoaded(InjVaccine injVaccine);

        void onDataInjVaccineNotAvailable();
    }

    // Vaccine
    interface LoadVaccinesCallback {

        void onVaccineLoaded(List<Object> vaccines);

        void onDataVaccineNotAvailable();
    }

    interface GetVaccineCallback {

        void onVaccineLoaded(Vaccine vaccine);

        void onDataVaccineNotAvailable();
    }

    // Child and InjSchedule hava is_notify = 0
    interface LoadChildInjSchedulesCallback {

        void onChildInjSchedulesLoaded(List<ChildInjSchedule> childInjSchedules);

        void onDataChildInjScheduleNotAvailable();
    }

    // Health feed
    interface LoadHealthFeedsCallback {

        void onHealthFeedsLoaded(List<HealthFeed> healthFeeds);

        void onDataHealthFeedsNotAvailable();
    }

    interface GetHealthFeedCallback {

        void onHealthFeedLoaded(HealthFeed healthFeed);

        void onDataHealthFeedNotAvailable();
    }
}