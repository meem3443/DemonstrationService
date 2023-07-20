package com.police.demonstrationservice.rest_api.notification;

import android.os.Parcel;
import android.os.Parcelable;

public class NotificationRequestCnt implements Parcelable {
    private String cnt;
    private String caltime;
    private String time;
    private String location;
    private String distance;
    private String region;
    private String wind;
    private String stdnoise;
    private String noise;

    protected NotificationRequestCnt(Parcel in) {
        cnt = in.readString();
        caltime = in.readString();
        time = in.readString();
        location = in.readString();
        distance = in.readString();
        region = in.readString();
        wind = in.readString();
        stdnoise = in.readString();
        noise = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cnt);
        dest.writeString(caltime);
        dest.writeString(time);
        dest.writeString(location);
        dest.writeString(distance);
        dest.writeString(region);
        dest.writeString(wind);
        dest.writeString(stdnoise);
        dest.writeString(noise);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NotificationRequestCnt> CREATOR = new Creator<NotificationRequestCnt>() {
        @Override
        public NotificationRequestCnt createFromParcel(Parcel in) {
            return new NotificationRequestCnt(in);
        }

        @Override
        public NotificationRequestCnt[] newArray(int size) {
            return new NotificationRequestCnt[size];
        }
    };

    public String getCnt() { return this.cnt; }

    public NotificationRequestCnt(String cnt, String caltime, String time, String location, String distance, String region, String wind, String stdnoise, String noise) {
        this.cnt = cnt;
        this.caltime = caltime;
        this.time = time;
        this.location = location;
        this.distance = distance;
        this.region = region;
        this.wind = wind;
        this.stdnoise = stdnoise;
        this.noise = noise;
    }
}
