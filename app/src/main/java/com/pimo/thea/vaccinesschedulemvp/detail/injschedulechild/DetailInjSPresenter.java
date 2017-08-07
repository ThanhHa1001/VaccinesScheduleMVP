package com.pimo.thea.vaccinesschedulemvp.detail.injschedulechild;

import android.app.Activity;
import android.content.Context;

import com.pimo.thea.vaccinesschedulemvp.R;
import com.pimo.thea.vaccinesschedulemvp.data.Child;
import com.pimo.thea.vaccinesschedulemvp.data.InjSchedule;
import com.pimo.thea.vaccinesschedulemvp.data.source.VaccinesScheduleDataSource;
import com.pimo.thea.vaccinesschedulemvp.data.source.VaccinesScheduleRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thea on 8/3/2017.
 */

public class DetailInjSPresenter implements DetailInjSContract.Presenter {

    private VaccinesScheduleRepository repository;
    private DetailInjSContract.View view;
    private long childId;
    private long childDob;
    private Context context;

    private boolean firstLoad = true;

    public DetailInjSPresenter(long childId, long childDob, VaccinesScheduleRepository repository, DetailInjSContract.View view, Context context) {
        this.childId = childId;
        this.childDob = childDob;
        this.repository = repository;
        this.view = view;
        this.context = context;
    }

    @Override
    public void bind() {
        loadChildById();
        loadListInjSchedulesByChildID(false);
    }

    @Override
    public void loadChildById() {
        repository.getChild(childId, new VaccinesScheduleDataSource.GetChildCallback() {
            @Override
            public void onChildLoaded(Child child) {
                view.showFullnameChildInAppbarLayout(child.getFullName());
                view.showInformationChildInAppBarLayout(child);
            }

            @Override
            public void onDataChildNotAvailable() {
                view.showNoInformationChildInAppBarLayout();
            }
        });
    }

    @Override
    public void loadListInjSchedulesByChildID(boolean forceLoad) {
        loadAllListInjSchedulesByChildID(forceLoad || firstLoad);
        firstLoad = false;
    }

    @Override
    public void result(int requestCode, int resultCode) {
        if (requestCode == DetailInjSFragment.REQUEST_EDIT_CHILD && resultCode == Activity.RESULT_OK) {
            repository.refreshChildList();
        }
    }

    private void loadAllListInjSchedulesByChildID(boolean forceLoad) {
        if (forceLoad) {
            repository.refreshInjSchedules();
        }

        repository.getInjSchedulesByChildIdAndChildDob(childId, childDob, new VaccinesScheduleDataSource.LoadInjSchedulesByChildIdCallback() {
            @Override
            public void onInjSchedulesLoaded(List<InjSchedule> injSchedules) {
                if (!view.isActive()) {
                    return;
                }
                processInjScheduleByChildID(injSchedules);
            }

            @Override
            public void onDataInjScheduleNotAvailable() {
                if (!view.isActive()) {
                    return;
                }

                // show notify error because not loaded data
                view.showNoInjScheduleByChildID();
            }
        });
    }

    private void processInjScheduleByChildID(List<InjSchedule> injSchedules) {
        List<Object> objects = new ArrayList<>();
        int numberPrevious = -1;
        for (InjSchedule inj : injSchedules) {
            int ageMonths = inj.getAgeMonths();
            if (ageMonths == 0 && numberPrevious != ageMonths) {
                objects.add(context.getResources().getString(R.string.new_born));
                numberPrevious = ageMonths;
            } else if (ageMonths != 0 && numberPrevious != ageMonths) {
                objects.add(ageMonths + " " + context.getResources().getString(R.string.age_months));
                numberPrevious = ageMonths;
            }
            objects.add(inj);
        }
        view.showListInjScheduleByChildID(objects);
    }
}
