package com.dfens.demo.network;

import android.content.Context;
import com.dfens.demo.domain.DfensEvent;
import com.dfens.demo.domain.DfensEventResponse;
import com.dfens.demo.domain.PollEventResponse;
import com.dfens.demo.domain.Status;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

/**
 * Created by mattias on 2014-11-04.
 */

@EBean
public class NetClient {

    @Bean
    Caller caller;

    private Context context;

    public NetClient(Context context) {
        this.context = context;
    }

    public int getLastEventSeqNo() throws Exception {
        PollEventResponse response = getAllEventsSinceSeqNo(0);
        return response.last_seq;
    }

    public PollEventResponse getAllEventsSinceSeqNo(int seqNo) throws Exception {
        return caller.executeGet("/events/_changes?since=" + seqNo, PollEventResponse.class);
    }

    public DfensEventResponse getEventDetails(String eventId) throws Exception {
        return caller.executeGet("/events/" + eventId, DfensEventResponse.class);
    }

    public Status sendKeepBlockCommand(DfensEvent eventToKeepBlocking) throws Exception {
        DfensEvent keepCommand = new DfensEvent("CMD_KEEP_BLOCKING",
                eventToKeepBlocking.zone, eventToKeepBlocking.client_name,
                eventToKeepBlocking.client_ip, eventToKeepBlocking.url);
        return caller.executePost("/commands", Status.class, keepCommand);
    }

    public Status sendUnblockCommand(DfensEvent eventToUnblock) throws Exception {
        DfensEvent unblockCommand = new DfensEvent("CMD_UNBLOCK",
                eventToUnblock.zone, eventToUnblock.client_name,
                eventToUnblock.client_ip, eventToUnblock.url);
        return caller.executePost("/commands", Status.class, unblockCommand);
    }

}
