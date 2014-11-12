package com.dfens.demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import com.dfens.demo.domain.DfensEvent;
import com.dfens.demo.domain.Status;
import com.dfens.demo.network.NetClient;
import org.androidannotations.annotations.*;

/**
 * Created by mattias on 2014-11-04.
 */

@NoTitle
@EActivity(R.layout.activity_event_handler_dialog)
public class EventDialogActivity extends Activity {

    @ViewById(R.id.event_handler_event_text)
    TextView eventText;

//    @Pref
//    Prefs_ prefs;

    SharedPreferences prefs;

    @Bean
    NetClient netClient;

    @Extra(Constructor.EXTRAS_DFENS_EVENT)
    DfensEvent event;

    @Extra(Constructor.EXTRAS_DFENS_EVENT_SEQ_NO)
    int seqNo;

    public static class Constructor extends Intent {

        public static final String EXTRAS_DFENS_EVENT = "extras-dfens-event";

        public static final String EXTRAS_DFENS_EVENT_SEQ_NO = "extras-dfens-event-seq-no";

        public Constructor(Context context, DfensEvent event, int seqNo) {
            super(context, EventDialogActivity_.class);
            putExtra(EXTRAS_DFENS_EVENT, event);
            putExtra(EXTRAS_DFENS_EVENT_SEQ_NO, seqNo);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences("Dfens", MODE_PRIVATE);
    }

    @AfterViews
    public void setupViews() {
        eventText.setText("Your dfens unit has blocked a request from: " + event.client_name + " to " + event.url);
    }

    @Click(R.id.event_handler_unblock_button)
    public void onClickUnblockEvent(View button) {
        sendUnblockCommand();

        prefs.edit().putInt("maxSeqNo", seqNo).commit();
        setResult(RESULT_OK);
        finish();
    }

    @Click(R.id.event_handler_keep_block_button)
    public void onClickKeepBlockButton(View button) {
        sendKeepBlockCommand();
        
        prefs.edit().putInt("maxSeqNo", seqNo).commit();
        setResult(RESULT_OK);
        finish();
    }

    @Background
    protected void sendUnblockCommand() {
        try {
            Status status = netClient.sendUnblockCommand(event);
//            if (status.ok) {
//                Log.d("Dfens", "Setting maxSeqNo to " + seqNo + " in EventDialogActivity");
//                prefs.edit().putInt("maxSeqNo", seqNo).commit();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Background
    protected void sendKeepBlockCommand() {
        try {
            Status status = netClient.sendKeepBlockCommand(event);
//            if (status.ok) {
//                Log.d("Dfens", "Setting maxSeqNo to " + seqNo + " in EventDialogActivity");
//                prefs.edit().putInt("maxSeqNo", seqNo).commit();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
