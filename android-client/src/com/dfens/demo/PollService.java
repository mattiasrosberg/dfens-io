package com.dfens.demo;

import android.app.*;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.SystemClock;
import android.util.Log;
import com.dfens.demo.domain.DfensEvent;
import com.dfens.demo.domain.DfensEventResponse;
import com.dfens.demo.domain.PollEventResponse;
import com.dfens.demo.network.NetClient;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EIntentService;
import org.androidannotations.annotations.ServiceAction;

import java.util.List;

/**
 * Created by mattias on 2014-11-08.
 */
@EIntentService
public class PollService extends IntentService {

    private static final int MAX_POLL_TIME = 1 * 60 * 1000;   //15 minutes

    @Bean
    NetClient netClient;

    @App
    DfensApplication application;

    boolean keepRunning;
    private long startTime;

    public PollService() {
        super("PollService");
    }

    @ServiceAction
    protected void pollUntilEventFound() {
        keepRunning = true;

        startTime = System.currentTimeMillis();

        while (keepRunning && (application.isKeepPollServiceRunning()) || ((startTime + MAX_POLL_TIME) > System.currentTimeMillis())) {
            try {
                SharedPreferences prefs = getSharedPreferences("Dfens", MODE_PRIVATE);
                PollEventResponse pollEventResponse = netClient.getAllEventsSinceSeqNo(prefs.getInt("maxSeqNo", 0));
                Log.d("Dfens", "Number of events:" + pollEventResponse.results.size() + "     SeqNo:" + prefs.getInt("maxSeqNo", 0) + ", LastSeqNo: " + pollEventResponse.last_seq);

                if (pollEventResponse.results.size() > 0) {
                    DfensEventResponse dfensEventResponse = netClient.getEventDetails(pollEventResponse.results.get(0).id);
                    if (dfensEventResponse.event != null) {
                        keepRunning = false;
                        Log.d("Dfens", "Passing Dfens event from service to DeviceListActivity..." + pollEventResponse.last_seq);
                        Intent startActivityIntent = new DeviceListActivity.Constructor(PollService.this, dfensEventResponse.event, pollEventResponse.last_seq);
                        if (isApplicationSentToBackground(this)) {
                            createNotification(startActivityIntent, dfensEventResponse.event);
                        } else {
                            ((DfensApplication) getApplication()).setDfensEvent(dfensEventResponse.event);
                            ((DfensApplication) getApplication()).setSeqNo(pollEventResponse.last_seq);

                            Intent newDfensEvent = new Intent("new_dfens_event");
                            sendBroadcast(newDfensEvent);
                        }
                    } else {
                        Log.d("Dfens", "No dfens events to deal with");
                    }
                } else {
                    Log.d("Dfens", "No events from polling");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            SystemClock.sleep(3000);
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if ((startTime + MAX_POLL_TIME) > System.currentTimeMillis()) {
            Log.d("Dfens", "Destroying PollService...event found");
        } else {
            Log.d("Dfens", "Destroying PollService...max poll time reached");
        }

    }

    public void createNotification(Intent startActivityIntent, DfensEvent event) {
        // Prepare intent which is triggered if the
        // notification is selected

        PendingIntent pIntent = PendingIntent.getActivity(this, 0, startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // Build notification
        // Actions are just fake
        Notification noti = new Notification.Builder(this)
                .setContentTitle("Dfens blocked " + event.client_name)
                .setContentText("Request to " + event.url + " has been blocked.").setSmallIcon(R.drawable.dfens_notification_icon)
                .setContentIntent(pIntent).setVibrate(new long[]{1000, 1000, 1000, 1000, 1000}).setSound(alarmSound)
                .build();


        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, noti);

    }

    public boolean isApplicationSentToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }
}
