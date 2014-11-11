package com.dfens.demo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import com.dfens.demo.domain.DfensEvent;
import com.dfens.demo.network.NetClient;
import org.androidannotations.annotations.*;

/**
 * Created by mattias on 2014-11-02.
 */

@NoTitle
@EActivity(R.layout.activity_device_list)
public class DeviceListActivity extends Activity {

    @ViewById(R.id.switch_1)
    Switch switch1;

    @ViewById(R.id.switch_2)
    Switch switch2;

    @ViewById(R.id.switch_3)
    Switch switch3;

    @ViewById(R.id.switch_4)
    Switch switch4;

    @ViewById(R.id.switch_5)
    Switch switch5;

    @ViewById(R.id.switch_6)
    Switch switch6;

    @ViewById(R.id.switch_7)
    Switch switch7;

    @ViewById(R.id.switch_8)
    Switch switch8;

    @ViewById(R.id.switch_9)
    Switch switch9;

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

    @Click(R.id.settings_image)
    public void onClickSettingsImage(View button) {
        switch1.setVisibility(switch1.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        switch2.setVisibility(switch2.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        switch3.setVisibility(switch3.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        switch4.setVisibility(switch4.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        switch5.setVisibility(switch5.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        switch6.setVisibility(switch6.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        switch7.setVisibility(switch7.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        switch8.setVisibility(switch8.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        switch9.setVisibility(switch9.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
    }
}
