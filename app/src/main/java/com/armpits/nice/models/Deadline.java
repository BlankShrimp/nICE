package com.armpits.nice.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import java.util.Date;

@Entity(primaryKeys = {"moduleCode", "date"})
public class Deadline {
    @NonNull public final String moduleCode;
    @NonNull public Date date;
    public Date dateAddedToCalendar;
    public boolean shouldNotify;

    public Deadline(String moduleCode, Date date, Date dateAddedToCalendar, boolean shouldNotify) {
        this.moduleCode = moduleCode;
        this.date = date;
        this.dateAddedToCalendar = dateAddedToCalendar;
        this.shouldNotify = shouldNotify;
    }
}
