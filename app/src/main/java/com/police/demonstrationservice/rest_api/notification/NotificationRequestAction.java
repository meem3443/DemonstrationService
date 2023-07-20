package com.police.demonstrationservice.rest_api.notification;

import android.os.Parcel;
import android.os.Parcelable;

public class NotificationRequestAction implements Parcelable {
    private String actionetc;

    public NotificationRequestAction(String actionetc) {
        this.actionetc = actionetc;
    }

    protected NotificationRequestAction(Parcel in) {
        actionetc = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(actionetc);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NotificationRequestAction> CREATOR = new Creator<NotificationRequestAction>() {
        @Override
        public NotificationRequestAction createFromParcel(Parcel in) {
            return new NotificationRequestAction(in);
        }

        @Override
        public NotificationRequestAction[] newArray(int size) {
            return new NotificationRequestAction[size];
        }
    };
}
