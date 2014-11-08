package com.dfens.demo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import com.dfens.demo.domain.DfensEvent;
import com.dfens.demo.network.NetClient;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.NoTitle;

/**
 * Created by mattias on 2014-11-02.
 */

@NoTitle
@EActivity(R.layout.activity_device_list)
public class DeviceListActivity extends Activity {

    @Bean
    NetClient netClient;

    @Extra(Constructor.EXTRAS_DFENS_EVENT)
    DfensEvent event;

    @Extra(Constructor.EXTRAS_DFENS_EVENT_SEQ_NO)
    int seqNo;

    private BroadcastReceiver dfensEventReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            event = ((DfensApplication) getApplication()).getDfensEvent();
            seqNo = ((DfensApplication) getApplication()).getSeqNo();
            startEventHandlerActivity(event, seqNo);
        }
    };

    public static class Constructor extends Intent {

        public static final String EXTRAS_DFENS_EVENT = "extras-dfens-event";

        public static final String EXTRAS_DFENS_EVENT_SEQ_NO = "extras-dfens-event-seq-no";

        public Constructor(Context context, DfensEvent event, int seqNo) {
            super(context, DeviceListActivity_.class);
            putExtra(EXTRAS_DFENS_EVENT, event);
            putExtra(EXTRAS_DFENS_EVENT_SEQ_NO, seqNo);
            setFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TOP);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            //Start poll service again
            startPollService();
            Log.d("Dfens", "DeviceListActivity...starting poll service after returning from user dialog");

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Dfens", "OnCreate DeviceListActivity...");

        if (event != null) {
            //Receiving event from service
            Log.d("Dfens", "DeviceListActivity...receiving event from Service. " + seqNo);
//            startActivityForResult(new EventDialogActivity.Constructor(DeviceListActivity.this, event, seqNo), 0);
            startEventHandlerActivity(event, seqNo);
        } else {
            Log.d("Dfens", "DeviceListActivity...starting poll service.");
            startPollService();
        }

        IntentFilter intentFilter = new IntentFilter("new_dfens_event");
        registerReceiver(dfensEventReceiver, intentFilter);

    }

    private void startEventHandlerActivity(DfensEvent dfensEvent, int sequenceNo) {
        startActivityForResult(new EventDialogActivity.Constructor(DeviceListActivity.this, dfensEvent, sequenceNo), 0);
    }


    private void startPollService() {
        //Start poll service
        PollService_.intent(getApplication()).pollUntilEventFound().start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(dfensEventReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
