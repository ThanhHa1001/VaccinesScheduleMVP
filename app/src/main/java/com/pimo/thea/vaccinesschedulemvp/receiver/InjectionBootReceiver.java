package com.pimo.thea.vaccinesschedulemvp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.pimo.thea.vaccinesschedulemvp.data.ChildInjSchedule;
import com.pimo.thea.vaccinesschedulemvp.data.source.VaccinesScheduleDataSource;
import com.pimo.thea.vaccinesschedulemvp.data.source.VaccinesScheduleRepository;
import com.pimo.thea.vaccinesschedulemvp.data.source.local.VaccinesScheduleLocalDataSource;

import java.util.List;

/**
 * Created by thea on 8/4/2017.
 */

public class InjectionBootReceiver extends BroadcastReceiver {
    private InjectionAlarmReceiver injectionAlarmReceiver = new InjectionAlarmReceiver();

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            VaccinesScheduleRepository repository =
                    VaccinesScheduleRepository.getInstance(VaccinesScheduleLocalDataSource.getInstance(context));

            repository.getChildInjScheduleList(new VaccinesScheduleDataSource.LoadChildInjSchedulesCallback() {
                @Override
                public void onChildInjSchedulesLoaded(List<ChildInjSchedule> childInjSchedules) {
                    injectionAlarmReceiver.setAlarmFromBootReceiver(context, childInjSchedules);
                }

                @Override
                public void onDataChildInjScheduleNotAvailable() {

                }
            });
        }
    }
}
