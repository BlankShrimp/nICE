package com.armpits.nice.models;

import java.util.Date;

public class Deadline {
    public final String moduleCode;
    public final Date date;
    public final Date dateAddedToCalendar;
    public final boolean shouldNotify;

    public Deadline(String moduleCode, Date date, Date dateAddedToCalendar, boolean shouldNotify) {
        this.moduleCode = moduleCode;
        this.date = date;
        this.dateAddedToCalendar = dateAddedToCalendar;
        this.shouldNotify = shouldNotify;
    }
}
