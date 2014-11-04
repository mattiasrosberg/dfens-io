package com.dfens.demo.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mattias on 2014-11-02.
 */
public class DfensEvent implements Parcelable {

    public final String type;

    public final String zone;

    public final String client_name;

    public final String client_ip;

    public final String url;

    public static final Creator CREATOR = new Creator() {
        public DfensEvent createFromParcel(Parcel in) {
            return new DfensEvent(in);
        }

        public DfensEvent[] newArray(int size) {
            return new DfensEvent[size];
        }
    };

    public DfensEvent(String type, String zone, String client_name, String client_ip, String url) {
        this.type = type;
        this.zone = zone;
        this.client_name = client_name;
        this.client_ip = client_ip;
        this.url = url;
    }

    public DfensEvent(Parcel in) {
        this.type = in.readString();
        this.zone = in.readString();
        this.client_name = in.readString();
        this.client_ip = in.readString();
        this.url = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(type);
        parcel.writeString(zone);
        parcel.writeString(client_name);
        parcel.writeString(client_ip);
        parcel.writeString(url);
    }
}
