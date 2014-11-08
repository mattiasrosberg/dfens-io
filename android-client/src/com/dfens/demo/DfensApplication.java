package com.dfens.demo;

import android.app.Application;
import com.dfens.demo.domain.DfensEvent;

/**
 * Created by mattias on 2014-11-08.
 */
public class DfensApplication extends Application {

    private DfensEvent dfensEvent;

    private int seqNo;

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
}
