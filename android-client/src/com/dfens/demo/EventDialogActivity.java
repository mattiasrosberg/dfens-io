package com.dfens.demo;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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
import com.dfens.demo.utils.UIManager;
import org.androidannotations.annotations.*;

/**
 * Created by mattias on 2014-11-04.
 */

@NoTitle
@EActivity(R.layout.activity_event_handler_dialog)
public class EventDialogActivity extends Activity implements DialogInterface.OnCancelListener, DialogInterface.OnClickListener {

    @ViewById(R.id.event_handler_event_text)
    TextView eventText;

//    @Pref
//    Prefs_ prefs;

    SharedPreferences prefs;

    @Bean
    NetClient netClient;

    @Bean
    UIManager uiManager;

    @Extra(Constructor.EXTRAS_DFENS_EVENT)
    DfensEvent event;

    @Extra(Constructor.EXTRAS_DFENS_EVENT_SEQ_NO)
    int seqNo;

    @Override
    public void onCancel(DialogInterface dialogInterface) {
        //Do nothing
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        dialogInterface.dismiss();
    }

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
        uiManager.setActivity(this);
        prefs = getSharedPreferences("Dfens", MODE_PRIVATE);
    }

    @AfterViews
    public void setupViews() {
        eventText.setText("Your dfens unit has blocked a request from: " + event.client_name + " to " + event.url);
    }

    @Click(R.id.event_handler_unblock_button)
    public void onClickUnblockEvent(View button) {
        sendUnblockCommand();
    }

    @Click(R.id.event_handler_keep_block_button)
    public void onClickKeepBlockButton(View button) {
        sendKeepBlockCommand();
    }

    @Background
    protected void sendUnblockCommand() {
        uiManager.showProgressDialog(false, this);
        try {
            Status status = netClient.sendUnblockCommand(event);
            if (status.ok) {
                Log.d("Dfens", "Setting maxSeqNo to " + seqNo + " in EventDialogActivity");
                prefs.edit().putInt("maxSeqNo", seqNo).commit();
                setResult(RESULT_OK);
            }
            finish();
        } catch (Exception e) {
            uiManager.showOkDialog("Message", "An error occured while connecting to server", this);
            e.printStackTrace();
        } finally {
            uiManager.dismissProgressDialog();
        }
    }

    @Background
    protected void sendKeepBlockCommand() {
        uiManager.showProgressDialog(false, this);
        try {
            Status status = netClient.sendKeepBlockCommand(event);
            if (status.ok) {
                Log.d("Dfens", "Setting maxSeqNo to " + seqNo + " in EventDialogActivity");
                prefs.edit().putInt("maxSeqNo", seqNo).commit();
                setResult(RESULT_OK);
            }
            finish();
        } catch (Exception e) {
            uiManager.showOkDialog("Message", "An error occured while connecting to server", this);
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
