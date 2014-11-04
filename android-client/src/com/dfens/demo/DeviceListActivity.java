package com.dfens.demo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import com.dfens.demo.domain.DfensEventResponse;
import com.dfens.demo.domain.PollEventResponse;
import com.dfens.demo.network.NetClient;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NoTitle;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by mattias on 2014-11-02.
 */

@NoTitle
@EActivity(R.layout.activity_device_list)
public class DeviceListActivity extends Activity {

    @Bean
    NetClient netClient;

    private Timer pollTimer = new Timer();

    private class CheckForEventTask extends TimerTask {

        @Override
        public void run() {
            try {
                SharedPreferences prefs = getSharedPreferences("Dfens", MODE_PRIVATE);
                PollEventResponse pollEventResponse = netClient.getAllEventsSinceSeqNo(prefs.getInt("maxSeqNo", 0));
                Log.d("Dfens", "Number of events:" + pollEventResponse.results.size() + "     SeqNo:" + prefs.getInt("maxSeqNo", 0) + ", LastSeqNo: " + pollEventResponse.last_seq);

                if (pollEventResponse.results.size() > 0) {
                    DfensEventResponse dfensEventResponse = netClient.getEventDetails(pollEventResponse.results.get(0).id);
                    if (dfensEventResponse.event != null) {
                        startActivityForResult(new EventDialogActivity.Constructor(DeviceListActivity.this, dfensEventResponse.event, pollEventResponse.last_seq), 0);
                    } else {
                        Log.d("Dfens", "No dfens events to deal with");
                    }
                } else {
                    Log.d("Dfens", "No events from polling");
                }

//                Status status = netClient.sendKeepBlockCommand(dfensEventResponse.event.get(0));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    ;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            pollTimer.cancel();
            pollTimer = new Timer();
            pollTimer.scheduleAtFixedRate(new CheckForEventTask(), 2000, 3000);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pollTimer.scheduleAtFixedRate(new CheckForEventTask(), 2000, 3000);
//        pollTimer.schedule(checkForEventTask, 2000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        pollTimer.cancel();
    }
}
