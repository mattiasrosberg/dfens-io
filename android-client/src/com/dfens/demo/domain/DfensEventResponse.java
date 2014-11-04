package com.dfens.demo.domain;

import java.util.List;

/**
 * Created by mattias on 2014-11-02.
 */
public class DfensEventResponse {

    public final String _rev;

    public final String _id;

    public final DfensEvent event;

    public DfensEventResponse(String _rev, String _id, DfensEvent event) {
        this._rev = _rev;
        this._id = _id;
        this.event = event;
    }
}
