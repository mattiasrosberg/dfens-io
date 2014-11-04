package com.dfens.demo.domain;

/**
 * Created by mattias on 2014-11-04.
 */
public class Status {

    public final boolean ok;
    public final String id;
    public final String rev;

    public Status(boolean ok, String id, String rev) {
        this.ok = ok;
        this.id = id;
        this.rev = rev;
    }
}
