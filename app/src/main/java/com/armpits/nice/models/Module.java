package com.armpits.nice.models;

import java.util.Date;
import java.util.List;

public class Module {
    public final String title;
    public final String code;
    public final Date lastUpdate;
    public final boolean enableNotifications;
    public final boolean enableDownloads;
    public final boolean addDDLsToCalendar;
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
