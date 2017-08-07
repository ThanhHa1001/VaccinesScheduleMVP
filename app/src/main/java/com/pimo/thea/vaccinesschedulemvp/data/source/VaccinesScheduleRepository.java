package com.pimo.thea.vaccinesschedulemvp.data.source;

import android.util.Log;

import com.pimo.thea.vaccinesschedulemvp.data.Child;
import com.pimo.thea.vaccinesschedulemvp.data.ChildInjSchedule;
import com.pimo.thea.vaccinesschedulemvp.data.Childcare;
import com.pimo.thea.vaccinesschedulemvp.data.Disease;
import com.pimo.thea.vaccinesschedulemvp.data.InjSchedule;
import com.pimo.thea.vaccinesschedulemvp.data.InjVaccine;
import com.pimo.thea.vaccinesschedulemvp.data.Vaccine;
import com.pimo.thea.vaccinesschedulemvp.data.source.local.VaccinesScheduleLocalDataSource;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by thea on 6/30/2017.
 */

public class VaccinesScheduleRepository implements VaccinesScheduleDataSource {

    private static VaccinesScheduleRepository INSTANCE = null;

    private final VaccinesScheduleLocalDataSource localDataSource;

    private Map<Long, Child> cacheChild;
    private Map<Long, InjSchedule> cacheInjScheduleByChildID;
    private Map<Long, Object> cacheDisease;
    private Map<Long, Object> cacheChildcare;
    private Map<Long, Object> cacheVaccine;

    private boolean cacheChildIsDirty = false;
    private boolean cacheInjSByChildIdIsDirty = false;
    private boolean cacheDiseaseIsDirty = false;
    private boolean cacheChildcareIsDirty = false;
    private boolean cacheVaccineIsDirty = false;

    private VaccinesScheduleRepository(VaccinesScheduleLocalDataSource localDataSource) {
        this.localDataSource = localDataSource;
    }

    public static VaccinesScheduleRepository getInstance(VaccinesScheduleLocalDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new VaccinesScheduleRepository(localDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getChildList(final LoadChildListCallback loadChildListCallback) {
        if (cacheChild != null && !cacheChildIsDirty) {
            loadChildListCallback.onChildLoaded(new ArrayList<>(cacheChild.values()));
            return;
        }

        localDataSource.getChildList(new LoadChildListCallback() {
            @Override
            public void onChildLoaded(List<Child> childList) {
                loadChildListCallback.onChildLoaded(new ArrayList<>(cacheChild.values()));
            }

            @Override
            public void onDataChildNotAvailable() {
                loadChildListCallback.onDataChildNotAvailable();
            }
        });
    }

    @Override
    public void getChildListWithNextInject(final LoadChildListCallback loadChildListCallback) {
        Log.d("repository", " get child list");
        if (cacheChild != null && !cacheChildIsDirty) {
            loadChildListCallback.onChildLoaded(new ArrayList<>(cacheChild.values()));
            return;
        }

        localDataSource.getChildListWithNextInject(new LoadChildListCallback() {
            @Override
            public void onChildLoaded(List<Child> childList) {
                refreshCacheChild(childList);
                loadChildListCallback.onChildLoaded(new ArrayList<>(cacheChild.values()));
            }

            @Override
            public void onDataChildNotAvailable() {
                loadChildListCallback.onDataChildNotAvailable();
            }
        });
    }

    @Override
    public void getChild(final long childId, final GetChildCallback getChildCallback) {
        Child childFromCache = getChildWithId(childId);

        if (childFromCache != null && !cacheChildIsDirty) {
            getChildCallback.onChildLoaded(childFromCache);
            return;
        }

        localDataSource.getChild(childId, new GetChildCallback() {
            @Override
            public void onChildLoaded(Child child) {
                getChildCallback.onChildLoaded(child);
            }

            @Override
            public void onDataChildNotAvailable() {
                getChildCallback.onDataChildNotAvailable();
            }
        });
    }

    @Override
    public void getChildWithNextInject(final long childId, final GetChildCallback getChildCallback) {
        Child childFromCache = getChildWithId(childId);

        if (childFromCache != null) {
            getChildCallback.onChildLoaded(childFromCache);
            return;
        }

        localDataSource.getChildWithNextInject(childId, new GetChildCallback() {
            @Override
            public void onChildLoaded(Child child) {
                if (cacheChild == null) {
                    cacheChild = new LinkedHashMap<>();
                }
                cacheChild.put(childId, child);
                getChildCallback.onChildLoaded(child);
            }

            @Override
            public void onDataChildNotAvailable() {
                getChildCallback.onDataChildNotAvailable();
            }
        });
    }

    @Override
    public long insertChild(Child child) {
        long childId = localDataSource.insertChild(child);
        return childId;
    }

    @Override
    public void updateChild(Child child) {
        localDataSource.updateChild(child);
    }

    @Override
    public void deleteChild(long childId) {
        localDataSource.deleteChild(childId);
    }

    @Override
    public void deleteAllChildList() {

    }

    @Override
    public void refreshChildList() {
        cacheChildIsDirty = true;
    }

    @Override
    public void getChildcares(final LoadChildcaresCallback loadChildcaresCallback) {
        if (cacheChildcare != null && !cacheChildcareIsDirty) {
            loadChildcaresCallback.onChildcareLoaded(new ArrayList<>(cacheChildcare.values()));
            return;
        }

        localDataSource.getChildcares(new LoadChildcaresCallback() {
            @Override
            public void onChildcareLoaded(List<Object> childcares) {
                refreshCacheChildcare(childcares);
                loadChildcaresCallback.onChildcareLoaded(childcares);
            }

            @Override
            public void onDataChildcareNotAvailable() {
                loadChildcaresCallback.onDataChildcareNotAvailable();
            }
        });
    }

    @Override
    public void getChildcare(final long childcareId, final GetChildcareCallback getChildcareCallback) {
        Childcare childcareFromCache = (Childcare) getChildcareWithId(childcareId);

        if (childcareFromCache != null) {
            getChildcareCallback.onChildcareLoaded(childcareFromCache);
            return;
        }

        localDataSource.getChildcare(childcareId, new GetChildcareCallback() {
            @Override
            public void onChildcareLoaded(Childcare childcare) {
                if (cacheChildcare == null) {
                    cacheChildcare = new LinkedHashMap<>();
                }
                cacheChildcare.put(childcareId, childcare);
                getChildcareCallback.onChildcareLoaded(childcare);
            }

            @Override
            public void onDataChildcareNotAvailable() {
                getChildcareCallback.onDataChildcareNotAvailable();
            }
        });

    }

    @Override
    public long insertChildcare(Childcare childcare) {
        long childcareId = localDataSource.insertChildcare(childcare);
        if (cacheChildcare == null) {
            cacheChildcare = new LinkedHashMap<>();
        }

        childcare.setChildcareId(childcareId);
        cacheChildcare.put(childcareId, childcare);
        return childcareId;
    }

    @Override
    public void updateChildcare(Childcare childcare) {

    }

    @Override
    public void refreshChildcares() {
        cacheChildcareIsDirty = true;
    }

    @Override
    public void getDiseases(final LoadDiseasesCallback loadDiseasesCallback) {
        if (cacheDisease != null && !cacheDiseaseIsDirty) {
            loadDiseasesCallback.onDiseaseLoaded(new ArrayList<>(cacheDisease.values()));
            return;
        }

        localDataSource.getDiseases(new LoadDiseasesCallback() {
            @Override
            public void onDiseaseLoaded(List<Object> diseases) {
                refreshCacheDisease(diseases);
                loadDiseasesCallback.onDiseaseLoaded(diseases);
            }

            @Override
            public void onDataDiseaseNotAvailable() {
                loadDiseasesCallback.onDataDiseaseNotAvailable();
            }
        });
    }

    @Override
    public void getDisease(final long diseaseId, final GetDiseaseCallback getDiseaseCallback) {
        Disease diseaseFromCache = (Disease) getDiseaseWithId(diseaseId);
        if (diseaseFromCache != null) {
            getDiseaseCallback.onDiseaseLoaded(diseaseFromCache);
            return;
        }

        localDataSource.getDisease(diseaseId, new GetDiseaseCallback() {
            @Override
            public void onDiseaseLoaded(Disease disease) {
                if (cacheDisease == null) {
                    cacheDisease = new LinkedHashMap<>();
                }
                cacheDisease.put(diseaseId, disease);
                getDiseaseCallback.onDiseaseLoaded(disease);
            }

            @Override
            public void onDataDiseaseNotAvailable() {
                getDiseaseCallback.onDataDiseaseNotAvailable();
            }
        });
    }

    @Override
    public long insertDisease(Disease disease) {
        long diseaseId = localDataSource.insertDisease(disease);

        if (cacheDisease == null) {
            cacheDisease = new LinkedHashMap<>();
        }

        disease.setDiseaseId(diseaseId);
        cacheDisease.put(diseaseId, disease);
        return diseaseId;
    }

    @Override
    public void updateDisease(Disease disease) {

    }

    @Override
    public void refreshDiseases() {
        cacheDiseaseIsDirty = true;
    }

    @Override
    public void getInjSchedulesByChildIdAndChildDob(long childId, long childDob, final LoadInjSchedulesByChildIdCallback loadInjSchedulesCallback) {
        if (cacheInjScheduleByChildID != null && !cacheInjSByChildIdIsDirty) {
            loadInjSchedulesCallback.onInjSchedulesLoaded(new ArrayList<>(cacheInjScheduleByChildID.values()));
            return;
        }

        localDataSource.getInjSchedulesByChildIdAndChildDob(childId, childDob, new LoadInjSchedulesByChildIdCallback() {
            @Override
            public void onInjSchedulesLoaded(List<InjSchedule> injSchedules) {
                refreshCacheInjScheduleByChildId(injSchedules);
                loadInjSchedulesCallback.onInjSchedulesLoaded(new ArrayList<>(cacheInjScheduleByChildID.values()));
            }

            @Override
            public void onDataInjScheduleNotAvailable() {
                loadInjSchedulesCallback.onDataInjScheduleNotAvailable();
            }
        });
    }

    @Override
    public void getInjSchedulesByChildId(final long childId, final LoadInjSchedulesByChildIdCallback loadInjSchedulesByChildIdCallback) {
        if (cacheInjScheduleByChildID != null && !cacheInjSByChildIdIsDirty) {
            loadInjSchedulesByChildIdCallback.onInjSchedulesLoaded(new ArrayList<>(cacheInjScheduleByChildID.values()));
            return;
        }

        localDataSource.getInjSchedulesByChildId(childId, new LoadInjSchedulesByChildIdCallback() {
            @Override
            public void onInjSchedulesLoaded(List<InjSchedule> injSchedules) {
                loadInjSchedulesByChildIdCallback.onInjSchedulesLoaded(new ArrayList<>(injSchedules));
            }

            @Override
            public void onDataInjScheduleNotAvailable() {
                loadInjSchedulesByChildIdCallback.onDataInjScheduleNotAvailable();
            }
        });
    }


    @Override
    public void getInjSchedule(long injScheduleID, final GetInjScheduleCallback getInjScheduleCallback) {
        Log.d("VSRepository", " injScheduleId: " + injScheduleID);
        InjSchedule injScheduleFromCache = getInjScheduleWithId(injScheduleID);

        if (injScheduleFromCache != null && !cacheInjSByChildIdIsDirty) {
            Log.d("VSRepository", " load from cache");
            getInjScheduleCallback.onInjScheduleLoaded(injScheduleFromCache);
            return;
        }

        localDataSource.getInjSchedule(injScheduleID, new GetInjScheduleCallback() {
            @Override
            public void onInjScheduleLoaded(InjSchedule injSchedule) {
                Log.d("VSRepository", " load from DB");
                getInjScheduleCallback.onInjScheduleLoaded(injSchedule);

            }

            @Override
            public void onDataInjScheduleNotAvailable() {
                getInjScheduleCallback.onDataInjScheduleNotAvailable();
            }
        });
    }

    @Override
    public long insertInjSchedule(InjSchedule injSchedule) {
        return localDataSource.insertInjSchedule(injSchedule);
    }

    @Override
    public void updateInjSchedule(InjSchedule injSchedule) {
        localDataSource.updateInjSchedule(injSchedule);
    }

    @Override
    public void updateInjScheduleNotified(long injScheduleId) {
        localDataSource.updateInjScheduleNotified(injScheduleId);
    }

    @Override
    public void deleteInjScheduleByChildId(long childId) {
        localDataSource.deleteInjScheduleByChildId(childId);
    }

    @Override
    public void refreshInjSchedules() {
        cacheInjSByChildIdIsDirty = true;
    }

    @Override
    public void getInjVaccines(LoadInjVaccinesCallback loadInjVaccinesCallback) {

    }

    @Override
    public void getInjVaccine(long injScheduleId, final GetInjVaccineCallback getInjVaccineCallback) {
        localDataSource.getInjVaccine(injScheduleId, new GetInjVaccineCallback() {
            @Override
            public void onInjVaccineLoaded(InjVaccine injVaccine) {
                getInjVaccineCallback.onInjVaccineLoaded(injVaccine);
            }

            @Override
            public void onDataInjVaccineNotAvailable() {
                getInjVaccineCallback.onDataInjVaccineNotAvailable();
            }
        });
    }

    @Override
    public long insertInjVaccine(InjVaccine injVaccine) {
        long injVaccineId = localDataSource.insertInjVaccine(injVaccine);
        return injVaccineId;
    }

    @Override
    public void updateInjVaccine(InjVaccine injVaccine) {
        localDataSource.updateInjVaccine(injVaccine);
    }

    @Override
    public void deleteInjVaccineByInjScheduleId(long injScheduleId) {

    }

    @Override
    public void refreshInjVaccines() {
    }

    @Override
    public void getVaccines(final LoadVaccinesCallback loadVaccinesCallback) {
        if (cacheVaccine != null && !cacheVaccineIsDirty) {
            loadVaccinesCallback.onVaccineLoaded(new ArrayList<>(cacheVaccine.values()));
            return;
        }

        localDataSource.getVaccines(new LoadVaccinesCallback() {
            @Override
            public void onVaccineLoaded(List<Object> vaccines) {
                refreshCacheVaccine(vaccines);
                loadVaccinesCallback.onVaccineLoaded(vaccines);
            }

            @Override
            public void onDataVaccineNotAvailable() {
                loadVaccinesCallback.onDataVaccineNotAvailable();
            }
        });
    }

    @Override
    public void getVaccine(final long vaccineId, final GetVaccineCallback getVaccineCallback) {
        Vaccine vaccineFromCache = (Vaccine) getVaccineWithId(vaccineId);
        if (vaccineFromCache != null) {
            getVaccineCallback.onVaccineLoaded(vaccineFromCache);
            return;
        }

        localDataSource.getVaccine(vaccineId, new GetVaccineCallback() {
            @Override
            public void onVaccineLoaded(Vaccine vaccine) {
                if (cacheVaccine == null) {
                    cacheVaccine = new LinkedHashMap<>();
                }
                cacheVaccine.put(vaccineId, vaccine);
                getVaccineCallback.onVaccineLoaded(vaccine);
            }

            @Override
            public void onDataVaccineNotAvailable() {
                getVaccineCallback.onDataVaccineNotAvailable();
            }
        });

    }

    @Override
    public long insertVaccine(Vaccine vaccine) {
        long vaccineId = localDataSource.insertVaccine(vaccine);
        if (cacheVaccine == null) {
            cacheVaccine = new LinkedHashMap<>();
        }
        vaccine.setVaccineId(vaccineId);
        cacheVaccine.put(vaccineId, vaccine);
        return vaccineId;
    }

    @Override
    public void updateVaccine(Vaccine vaccine) {

    }

    @Override
    public void refreshVaccines() {
        cacheVaccineIsDirty = true;
    }

    @Override
    public void getChildInjScheduleList(final LoadChildInjSchedulesCallback loadChildInjSchedulesCallback) {
        localDataSource.getChildInjScheduleList(new LoadChildInjSchedulesCallback() {
            @Override
            public void onChildInjSchedulesLoaded(List<ChildInjSchedule> childInjSchedules) {
                loadChildInjSchedulesCallback.onChildInjSchedulesLoaded(childInjSchedules);
            }

            @Override
            public void onDataChildInjScheduleNotAvailable() {
                loadChildInjSchedulesCallback.onDataChildInjScheduleNotAvailable();
            }
        });
    }

    private void refreshCacheChild(List<Child> childList) {
        if (cacheChild == null) {
            cacheChild = new LinkedHashMap<>();
        }

        cacheChild.clear();

        for (Child child : childList) {
            cacheChild.put(child.getId(), child);
        }
        cacheChildIsDirty = false;
    }

    private Child getChildWithId(long childId) {
        if (cacheChild == null || cacheChild.isEmpty()) {
            return null;
        } else {
            return cacheChild.get(childId);
        }
    }

    private void refreshCacheInjScheduleByChildId(List<InjSchedule> injScheduleList) {
        if (cacheInjScheduleByChildID == null) {
            cacheInjScheduleByChildID = new LinkedHashMap<>();
        }

        cacheInjScheduleByChildID.clear();

        for (InjSchedule injSchedule : injScheduleList) {
            cacheInjScheduleByChildID.put(injSchedule.getId(), injSchedule);
        }
        cacheInjSByChildIdIsDirty = false;
    }

    private InjSchedule getInjScheduleWithId(long injScheduleId) {
        if (cacheInjScheduleByChildID == null || cacheInjScheduleByChildID.isEmpty()) {
            return null;
        } else {
            return cacheInjScheduleByChildID.get(injScheduleId);
        }
    }

    private void refreshCacheDisease(List<Object> objects) {
        if (cacheDisease == null) {
            cacheDisease = new LinkedHashMap<>();
        }

        cacheDisease.clear();

        for (Object object : objects) {
            Disease disease = (Disease) object;
            cacheDisease.put(disease.getDiseaseId(), disease);
        }
        cacheDiseaseIsDirty = false;
    }

    private Object getDiseaseWithId(long diseaseId) {
        if (cacheDisease == null || cacheDisease.isEmpty()) {
            return null;
        } else {
            return cacheDisease.get(diseaseId);
        }
    }

    private void refreshCacheChildcare(List<Object> objects) {
        if (cacheChildcare == null) {
            cacheChildcare = new LinkedHashMap<>();
        }

        cacheChildcare.clear();

        for (Object object : objects) {
            Childcare childcare = (Childcare) object;
            cacheChildcare.put(childcare.getChildcareId(), childcare);
        }
        cacheChildcareIsDirty = false;
    }

    private Object getChildcareWithId(long childcareId) {
        if (cacheChildcare == null || cacheChildcare.isEmpty()) {
            return null;
        } else {
            return cacheChildcare.get(childcareId);
        }
    }

    private void refreshCacheVaccine(List<Object> objects) {
        if (cacheVaccine == null) {
            cacheVaccine = new LinkedHashMap<>();
        }

        cacheVaccine.clear();

        for (Object object : objects) {
            Vaccine vaccine = (Vaccine) object;
            cacheVaccine.put(vaccine.getVaccineId(), vaccine);
        }
        cacheVaccineIsDirty = false;
    }

    private Object getVaccineWithId(long vaccineId) {
        if (cacheVaccine == null || cacheVaccine.isEmpty()) {
            return null;
        } else {
            return cacheVaccine.get(vaccineId);
        }
    }

}
