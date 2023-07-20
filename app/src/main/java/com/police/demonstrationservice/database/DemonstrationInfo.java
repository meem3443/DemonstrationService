package com.police.demonstrationservice.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DemonstrationInfo {
    @PrimaryKey(autoGenerate = true) int id;
    @ColumnInfo(name = "type") String type;
    @ColumnInfo(name = "currentTime") String currentTime;
    @ColumnInfo(name = "notificationType") int notificationType;

    public DemonstrationInfo(String type, String currentTime, int notificationType) {
        this.type = type;
        this.currentTime = currentTime;
        this.notificationType = notificationType;
    }

    public String getType() { return this.type; }
    public String getCurrentTime() { return this.currentTime; }
    public int getNotificationType() { return this.notificationType; }
}