package com.dfens.demo.domain;

/**
 * Created by mattias on 2014-11-02.
 */
public class DfensEvent {

    public final String type;

    public final String zone;

    public final String client_name;

    public final String client_ip;

    public final String url;

    public DfensEvent(String type, String zone, String client_name, String client_ip, String url) {
        this.type = type;
        this.zone = zone;
        this.client_name = client_name;
        this.client_ip = client_ip;
        this.url = url;
    }
}
