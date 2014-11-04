package com.dfens.demo.domain;

import java.util.List;

/**
 * Created by mattias on 2014-11-03.
 */
public class PollEventResponse {

    public final List<PollEvent> results;

    public final int last_seq;

    public PollEventResponse(List<PollEvent> results, int last_seq) {
        this.results = results;
        this.last_seq = last_seq;
    }
}
