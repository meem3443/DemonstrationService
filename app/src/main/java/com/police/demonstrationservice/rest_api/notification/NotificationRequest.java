package com.police.demonstrationservice.rest_api.notification;

import android.os.Parcel;
import android.os.Parcelable;

public class NotificationRequest implements Parcelable {
    private String currenttime;
    private String caltime;
    private String time;
    private String location;
    private String distance;
    private String region;
    private String wind;
    private String stdnoise;
    private String noise;
    private String name;
    private String equinoise;
    private String maxnoise;
    private NotificationRequestCnt[] demo;
    private String hour;
    private String minute;
    private String noisedivision;
    private NotificationRequestItem[] item;
    private NotificationRequestAction[] action;
    private int notificationType;

    public NotificationRequestCnt[] getDemo() {
        return this.demo;
    }

    public NotificationRequest(String name, String equinoise, String maxnoise, String currenttime) {
        this.name = name;
        this.equinoise = equinoise;
        this.maxnoise = maxnoise;
        this.currenttime = currenttime;
    }

    public NotificationRequest(String caltime, String time, String location,
                               String distance, String region, String wind,
                               String stdnoise, String noise, String name, String currenttime) {
        this.caltime = caltime;
        this.time = time;
        this.location = location;
        this.distance = distance;
        this.region = region;
        this.wind = wind;
        this.stdnoise = stdnoise;
        this.noise = noise;
        this.name = name;
        this.currenttime = currenttime;
    }

    public NotificationRequest(NotificationRequestCnt[] demo, String currenttime, String name) {
        this.demo = demo;
        this.currenttime = currenttime;
        this.name = name;
    }

    public NotificationRequest(String hour, String minute, String caltime, String time, String location,
                               String distance, String region, String noisedivision, String wind,
                               String stdnoise, String noise, String currenttime, String name) {
        this.hour = hour;
        this.minute = minute;
        this.caltime = caltime;
        this.time = time;
        this.location = location;
        this.distance = distance;
        this.region = region;
        this.noisedivision = noisedivision;
        this.wind = wind;
        this.stdnoise = stdnoise;
        this.noise = noise;
        this.currenttime = currenttime;
        this.name = name;
    }

    public NotificationRequest(String caltime, String time, String location, String distance,
                               String region, String wind, String stdnoise, String noise,
                               NotificationRequestItem[] item, NotificationRequestAction[] action,
                               String currenttime, String name) {
        this.caltime = caltime;
        this.time = time;
        this.location = location;
        this.distance = distance;
        this.region = region;
        this.wind = wind;
        this.stdnoise = stdnoise;
        this.noise = noise;
        this.item = item;
        this.action = action;
        this.currenttime = currenttime;
        this.name = name;
    }

    public NotificationRequest(NotificationRequestCnt[] demo, NotificationRequestItem[] item,
                               NotificationRequestAction[] action, String currenttime, String name) {
        this.demo = demo;
        this.item = item;
        this.action = action;
        this.currenttime = currenttime;
        this.name = name;
    }

    public NotificationRequest(String hour, String minute, String caltime, String time, String location,
                               String distance, String region, String noisedivision, String wind,
                               String stdnoise, String noise, NotificationRequestItem[] item,
                               NotificationRequestAction[] action, String currenttime, String name) {
        this.hour = hour;
        this.minute = minute;
        this.caltime = caltime;
        this.time = time;
        this.location = location;
        this.distance = distance;
        this.region = region;
        this.noisedivision = noisedivision;
        this.wind = wind;
        this.stdnoise = stdnoise;
        this.noise = noise;
        this.item = item;
        this.action = action;
        this.currenttime = currenttime;
        this.name = name;
    }

    public NotificationRequest(String hour, String minute, String caltime, String time,
                               String location, String distance, String region, String wind,
                               String noise, NotificationRequestItem[] item,
                               NotificationRequestAction[] action, String currenttime, String name) {
        this.hour = hour;
        this.minute = minute;
        this.caltime = caltime;
        this.time = time;
        this.location = location;
        this.distance = distance;
        this.region = region;
        this.wind = wind;
        this.noise = noise;
        this.item = item;
        this.action = action;
        this.currenttime = currenttime;
        this.name = name;
    }

    public NotificationRequest(NotificationRequestItem[] item, String name, String currenttime) {
        this.item = item;
        this.name = name;
        this.currenttime = currenttime;
    }

    protected NotificationRequest(Parcel in) {
        currenttime = in.readString();
        caltime = in.readString();
        time = in.readString();
        location = in.readString();
        distance = in.readString();
        region = in.readString();
        wind = in.readString();
        stdnoise = in.readString();
        noise = in.readString();
        name = in.readString();
        equinoise = in.readString();
        maxnoise = in.readString();
        demo = in.createTypedArray(NotificationRequestCnt.CREATOR);
        hour = in.readString();
        minute = in.readString();
        noisedivision = in.readString();
        item = in.createTypedArray(NotificationRequestItem.CREATOR);
        action = in.createTypedArray(NotificationRequestAction.CREATOR);
        notificationType = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(currenttime);
        dest.writeString(caltime);
        dest.writeString(time);
        dest.writeString(location);
        dest.writeString(distance);
        dest.writeString(region);
        dest.writeString(wind);
        dest.writeString(stdnoise);
        dest.writeString(noise);
        dest.writeString(name);
        dest.writeString(equinoise);
        dest.writeString(maxnoise);
        dest.writeTypedArray(demo, flags);
        dest.writeString(hour);
        dest.writeString(minute);
        dest.writeString(noisedivision);
        dest.writeTypedArray(item, flags);
        dest.writeTypedArray(action, flags);
        dest.writeInt(notificationType);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NotificationRequest> CREATOR = new Creator<NotificationRequest>() {
        @Override
        public NotificationRequest createFromParcel(Parcel in) {
            return new NotificationRequest(in);
        }

        @Override
        public NotificationRequest[] newArray(int size) {
            return new NotificationRequest[size];
        }
    };

    public String getCurrenttime() {
        return this.currenttime;
    }
}