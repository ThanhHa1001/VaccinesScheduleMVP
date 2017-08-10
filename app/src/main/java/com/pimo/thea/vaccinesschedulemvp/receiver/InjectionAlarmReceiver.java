package com.pimo.thea.vaccinesschedulemvp.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.pimo.thea.vaccinesschedulemvp.data.Child;
import com.pimo.thea.vaccinesschedulemvp.data.ChildInjSchedule;
import com.pimo.thea.vaccinesschedulemvp.data.InjSchedule;
import com.pimo.thea.vaccinesschedulemvp.data.source.VaccinesScheduleDataSource;
import com.pimo.thea.vaccinesschedulemvp.data.source.VaccinesScheduleRepository;
import com.pimo.thea.vaccinesschedulemvp.data.source.local.VaccinesScheduleLocalDataSource;
import com.pimo.thea.vaccinesschedulemvp.data.source.remote.VaccinesScheduleRemoteDataSource;
import com.pimo.thea.vaccinesschedulemvp.service.InjectionService;
import com.pimo.thea.vaccinesschedulemvp.utils.DateTimeHelper;

import java.util.List;

/**
 * Created by thea on 8/4/2017.
 */

public class InjectionAlarmReceiver extends WakefulBroadcastReceiver {
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    @Override
    public void onReceive(Context context, Intent intent) {
        long injectionId = intent.getLongExtra(InjectionService.INJECTION_ID_ARGS, -1L);
        String nameChild = intent.getStringExtra(InjectionService.NAME_CHILD_ARGS);
        String nameVaccine = intent.getStringExtra(InjectionService.NAME_VACCINE_ARGS);

        Log.d("InjectionAlarmReceiver", "injection id:" + injectionId);
        Intent intentService = new Intent(context, InjectionService.class);
        intentService.putExtra(InjectionService.INJECTION_ID_ARGS, injectionId);
        intentService.putExtra(InjectionService.NAME_CHILD_ARGS, nameChild);
        intentService.putExtra(InjectionService.NAME_VACCINE_ARGS, nameVaccine);
        startWakefulService(context, intentService);
    }

    public void setAlarmByChildId(final Context context, final String nameChild, long childId) {
        VaccinesScheduleRepository repository = VaccinesScheduleRepository.getInstance(
                VaccinesScheduleRemoteDataSource.getInstance(context),
                VaccinesScheduleLocalDataSource.getInstance(context));
        repository.getInjSchedulesByChildId(childId, new VaccinesScheduleDataSource.LoadInjSchedulesByChildIdCallback() {
            @Override
            public void onInjSchedulesLoaded(List<InjSchedule> injSchedules) {
                for (InjSchedule inj : injSchedules) {
                    long injId = inj.getId();
                    Log.d("InjectionAlarmReceiver", "repository injection id:" + injId);
                    alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent(context, InjectionAlarmReceiver.class);
                    intent.putExtra(InjectionService.INJECTION_ID_ARGS, injId);
                    intent.putExtra(InjectionService.NAME_CHILD_ARGS, nameChild);
                    intent.putExtra(InjectionService.NAME_VACCINE_ARGS, inj.getTitle());

                    long setDateTimeAlarm = DateTimeHelper.convertToAt7ClockBeforeThatADay(inj.getDayInjection());
                    pendingIntent = PendingIntent.getBroadcast(context, (int) injId, intent, 0);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, setDateTimeAlarm, pendingIntent);
                }
            }

            @Override
            public void onDataInjScheduleNotAvailable() {

            }
        });

        ComponentName receiver = new ComponentName(context, InjectionBootReceiver.class);
        PackageManager packageManager = context.getPackageManager();

        packageManager.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    public  void setAlarmByInjScheduleId(Context context, long injScheduleId) {
        VaccinesScheduleRepository repository =
                VaccinesScheduleRepository.getInstance(
                        VaccinesScheduleRemoteDataSource.getInstance(context),
                        VaccinesScheduleLocalDataSource.getInstance(context));

        final InjSchedule[] injSs = {null};
        final Child[] childs = {null};

        repository.getInjSchedule(injScheduleId, new VaccinesScheduleDataSource.GetInjScheduleCallback() {
            @Override
            public void onInjScheduleLoaded(InjSchedule injSchedule) {
                injSs[0] = injSchedule;
            }

            @Override
            public void onDataInjScheduleNotAvailable() {
            }
        });

        repository.getChild(injSs[0].getChildId(), new VaccinesScheduleDataSource.GetChildCallback() {
            @Override
            public void onChildLoaded(Child child) {
                childs[0] = child;
            }

            @Override
            public void onDataChildNotAvailable() {

            }
        });

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, InjectionAlarmReceiver.class);
        intent.putExtra(InjectionService.INJECTION_ID_ARGS, injScheduleId);
        intent.putExtra(InjectionService.NAME_CHILD_ARGS, childs[0].getFullName());
        intent.putExtra(InjectionService.NAME_VACCINE_ARGS, injSs[0].getTitle());

        long setDateTimeAlarm = DateTimeHelper.convertToAt7ClockBeforeThatADay(injSs[0].getDayInjection());
        pendingIntent = PendingIntent.getBroadcast(context, (int) injScheduleId, intent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, setDateTimeAlarm, pendingIntent);

        ComponentName receiver = new ComponentName(context, InjectionBootReceiver.class);
        PackageManager packageManager = context.getPackageManager();

        packageManager.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    public void setAlarmFromBootReceiver(Context context, List<ChildInjSchedule> childInjSchedules) {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, InjectionAlarmReceiver.class);
        for (ChildInjSchedule c : childInjSchedules) {
            long injId = c.getInjScheduleId();
            intent.putExtra(InjectionService.INJECTION_ID_ARGS, injId);
            intent.putExtra(InjectionService.NAME_CHILD_ARGS, c.getChildName());
            intent.putExtra(InjectionService.NAME_VACCINE_ARGS, c.getVaccineName());

            long setDateTimeAlarm = DateTimeHelper.convertToAt7ClockBeforeThatADay(c.getDayInjection());
            pendingIntent = PendingIntent.getBroadcast(context, (int) injId, intent, 0);
            alarmManager.set(AlarmManager.RTC_WAKEUP, setDateTimeAlarm, pendingIntent);
        }

        ComponentName receiver = new ComponentName(context, InjectionBootReceiver.class);
        PackageManager packageManager = context.getPackageManager();

        packageManager.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

    }

    public void cancelAlarmByChildId(final Context context, long childId) {
        if (alarmManager == null) {
            return;
        }

        VaccinesScheduleRepository repository = VaccinesScheduleRepository.getInstance(
                VaccinesScheduleRemoteDataSource.getInstance(context),
                VaccinesScheduleLocalDataSource.getInstance(context));
        repository.getInjSchedulesByChildId(childId, new VaccinesScheduleDataSource.LoadInjSchedulesByChildIdCallback() {
            @Override
            public void onInjSchedulesLoaded(List<InjSchedule> injSchedules) {
                for (InjSchedule inj : injSchedules) {
                    int requestCode = (int) inj.getId();
                    Intent intent = new Intent(context, InjectionAlarmReceiver.class);
                    pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
                    alarmManager.cancel(pendingIntent);


                }
            }

            @Override
            public void onDataInjScheduleNotAvailable() {

            }
        });

        ComponentName receiver = new ComponentName(context, InjectionBootReceiver.class);
        PackageManager packageManager = context.getPackageManager();

        packageManager.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

    }

    public void cancelAlarmByInjScheduleId(Context context, long injScheduleId) {
        if (alarmManager == null) {
            return;
        }

        Intent intent = new Intent(context, InjectionAlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(context, (int) injScheduleId, intent, 0);
        alarmManager.cancel(pendingIntent);

        ComponentName receiver = new ComponentName(context, InjectionBootReceiver.class);
        PackageManager packageManager = context.getPackageManager();

        packageManager.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

    }
}
