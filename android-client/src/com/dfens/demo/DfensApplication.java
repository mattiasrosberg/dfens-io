package com.dfens.demo;

import android.app.Application;
import com.dfens.demo.domain.DfensEvent;
import org.androidannotations.annotations.EApplication;

/**
 * Created by mattias on 2014-11-08.
 */
@EApplication
public class DfensApplication extends Application {

    private DfensEvent dfensEvent;

    private int seqNo;

    private boolean keepPollServiceRunning;

    public DfensEvent getDfensEvent() {
        return dfensEvent;
    }

    public void setDfensEvent(DfensEvent dfensEvent) {
        this.dfensEvent = dfensEvent;
    }

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }

    public boolean isKeepPollServiceRunning() {
        return keepPollServiceRunning;
    }

    public void setKeepPollServiceRunning(boolean keepPollServiceRunning) {
        this.keepPollServiceRunning = keepPollServiceRunning;
    }
}
