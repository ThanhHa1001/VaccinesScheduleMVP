package com.pimo.thea.vaccinesschedulemvp.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.pimo.thea.vaccinesschedulemvp.R;
import com.pimo.thea.vaccinesschedulemvp.data.source.VaccinesScheduleRepository;
import com.pimo.thea.vaccinesschedulemvp.data.source.local.VaccinesScheduleLocalDataSource;
import com.pimo.thea.vaccinesschedulemvp.data.source.remote.VaccinesScheduleRemoteDataSource;
import com.pimo.thea.vaccinesschedulemvp.detail.injchild.DetailInjCActivity;
import com.pimo.thea.vaccinesschedulemvp.receiver.InjectionAlarmReceiver;

/**
 * Created by thea on 8/4/2017.
 */

public class InjectionService extends IntentService {
    public static final String INJECTION_ID_ARGS = "injectionIdArgs";
    public static final String NAME_CHILD_ARGS = "nameChildArgs";
    public static final String NAME_VACCINE_ARGS = "nameVaccineArgs";

    private NotificationManager notificationManager;
    private long notificationId = 1L;

    public InjectionService() {
        super("InjectionService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        notificationId = intent.getLongExtra(INJECTION_ID_ARGS, -1L);
        Log.d("InjectionService", "on handle intent notify id: " + notificationId);
        String nameChild = intent.getStringExtra(NAME_CHILD_ARGS);
        String nameVaccine = intent.getStringExtra(NAME_VACCINE_ARGS);

        String contentTextNotification = getString(R.string.notification_injection_child, nameChild, nameVaccine);

        sendNotification(contentTextNotification, notificationId);

        VaccinesScheduleRepository repository =
                VaccinesScheduleRepository.getInstance(
                        VaccinesScheduleRemoteDataSource.getInstance(this),
                        VaccinesScheduleLocalDataSource.getInstance(this));

        repository.updateInjScheduleNotified(notificationId);

        InjectionAlarmReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String msg, long notificationId) {
        notificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        Log.d("InjectionService", "set notification notification id: " + notificationId);
        Intent resultIntent = new Intent(this, DetailInjCActivity.class);
        resultIntent.putExtra(DetailInjCActivity.INJECTION_ID_ARGS, notificationId);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(this, (int) notificationId,
                resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher_notification)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round))
                        .setContentTitle(getString(R.string.notification_content_title))
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        notificationManager.notify((int) notificationId, mBuilder.build());
    }
}
