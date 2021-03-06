package com.armpits.nice.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.List;

@Entity
public class Module {
    @PrimaryKey @NonNull
    public final String code;
    public final String title;
    public Date lastUpdate;
    public boolean enableNotifications;
    public boolean enableDownloads;
    public boolean addDDLsToCalendar;
    public boolean showInList;

    @Ignore
    public final List<Material> materials;
    @Ignore
    public final List<Deadline> deadlines;

    public Module(String title, String code, Date lastUpdate,
                  boolean enableNotifications, boolean addDDLsToCalendar, boolean enableDownloads,
                  boolean showInList, List<Material> materials, List<Deadline> deadlines) {
        this.title = title;
        this.code = code;
        this.lastUpdate = lastUpdate;
        this.enableNotifications = enableNotifications;
        this.enableDownloads = enableDownloads;
        this.addDDLsToCalendar = addDDLsToCalendar;
        this.materials = materials;
        this.deadlines = deadlines;
        this.showInList = showInList;
    }

    public Module(String title, String code, Date lastUpdate, boolean enableNotifications,
                  boolean addDDLsToCalendar, boolean enableDownloads, boolean showInList) {
        this (title, code, lastUpdate, enableNotifications, addDDLsToCalendar, enableDownloads,
                showInList, null, null);
    }

    @NonNull
    @Override
    public String toString() {
        return code +":"+ enableDownloads;
    }
}
