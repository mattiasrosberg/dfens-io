package com.dfens.demo.domain;

/**
 * Created by mattias on 2014-11-03.
 */
public class PollEvent {

    public final int seq;

    public final String id;

    public PollEvent(int seq, String id) {
        this.seq = seq;
        this.id = id;
    }
}
