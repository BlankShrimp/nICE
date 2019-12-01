package com.armpits.nice.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import java.util.Date;

@Entity
public class Deadline {
    @ColumnInfo(name = "module_code") public String moduleCode;
    @ColumnInfo(name = "date") public Date date;
    @ColumnInfo(name = "date_added") public Date dateAddedToCalendar;
    @ColumnInfo(name = "should_notify") public boolean shouldNotify;

    public Deadline(String moduleCode, Date date, Date dateAddedToCalendar, boolean shouldNotify) {
        this.moduleCode = moduleCode;
        this.date = date;
        this.dateAddedToCalendar = dateAddedToCalendar;
        this.shouldNotify = shouldNotify;
    }
}
