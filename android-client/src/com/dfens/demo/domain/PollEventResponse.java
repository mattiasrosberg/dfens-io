package com.dfens.demo.domain;

import java.util.List;

/**
 * Created by mattias on 2014-11-03.
 */
public class PollEventResponse {

    public final List<PollEvent> results;

    public PollEventResponse(List<PollEvent> results) {
        this.results = results;
    }
}
