package com.armpits.nice.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import java.util.Date;
import java.util.List;

@Entity
public class Module {
    @ColumnInfo(name = "title") public final String title;
    @ColumnInfo(name = "code") public final String code;
    @ColumnInfo(name = "last_update") public final Date lastUpdate;
    @ColumnInfo(name = "enable_notifications") public final boolean enableNotifications;
    @ColumnInfo(name = "enable_downloads") public final boolean enableDownloads;
    @ColumnInfo(name = "add_ddls_to_calendar") public final boolean addDDLsToCalendar;

    public final List<Material> materials;
    public final List<Deadline> deadlines;

    public Module(String title, String code, Date lastUpdate,
                  boolean enableNotifications, boolean addDDLsToCalendar, boolean enableDownloads,
                  List<Material> materials, List<Deadline> deadlines) {
        this.title = title;
        this.code = code;
        this.lastUpdate = lastUpdate;
        this.enableNotifications = enableNotifications;
        this.enableDownloads = enableDownloads;
        this.addDDLsToCalendar = addDDLsToCalendar;
        this.materials = materials;
        this.deadlines = deadlines;
    }

    public Module(String title, String code, Date lastUpdate,
                  boolean enableNotifications, boolean addDDLsToCalendar, boolean enableDownloads) {
        this (title, code, lastUpdate, enableNotifications, addDDLsToCalendar, enableDownloads,
                null, null);
    }
}
