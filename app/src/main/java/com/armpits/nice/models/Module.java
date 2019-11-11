package com.armpits.nice.models;

import java.util.Date;
import java.util.List;

public class Module {
    public final String title;
    public final Date lastUpdate;
    public final boolean enableNotifications;
    public final boolean addDDLsToCalendar;
    public final List<Material> materials;
    public final List<Deadline> deadlines;

    public Module(String title, Date lastUpdate, boolean enableNotifications,
                  boolean addDDLsToCalendar, List<Material> materials, List<Deadline> deadlines) {
        this.title = title;
        this.lastUpdate = lastUpdate;
        this.enableNotifications = enableNotifications;
        this.addDDLsToCalendar = addDDLsToCalendar;
        this.materials = materials;
        this.deadlines = deadlines;
    }
}
