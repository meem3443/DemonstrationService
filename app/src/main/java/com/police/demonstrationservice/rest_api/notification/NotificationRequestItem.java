package com.police.demonstrationservice.rest_api.notification;

import android.os.Parcel;
import android.os.Parcelable;

public class NotificationRequestItem implements Parcelable {
    private String itemname;
    private String cnt;
    private String sig;
    private String storage;

    public NotificationRequestItem(String itemname, String cnt, String sig, String storage) {
        this.itemname = itemname;
        this.cnt = cnt;
        this.sig = sig;
        this.storage = storage;
    }

    protected NotificationRequestItem(Parcel in) {
        itemname = in.readString();
        cnt = in.readString();
        sig = in.readString();
        storage = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(itemname);
        dest.writeString(cnt);
        dest.writeString(sig);
        dest.writeString(storage);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NotificationRequestItem> CREATOR = new Creator<NotificationRequestItem>() {
        @Override
        public NotificationRequestItem createFromParcel(Parcel in) {
            return new NotificationRequestItem(in);
        }

        @Override
        public NotificationRequestItem[] newArray(int size) {
            return new NotificationRequestItem[size];
        }
    };
}
