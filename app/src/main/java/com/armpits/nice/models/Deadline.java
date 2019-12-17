package com.armpits.nice.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import java.util.Date;

@Entity(primaryKeys = {"moduleCode", "date", "title"})
public class Deadline {
    @NonNull public final String moduleCode;
    @NonNull public Date date;
    public String moduleTitle;
    @NonNull public String title;
    public boolean shouldNotify;

    public Deadline(String moduleCode, String moduleTitle, Date date, String title,
                    boolean shouldNotify) {
        this.moduleCode = moduleCode;
        this.moduleTitle = moduleTitle;
        this.date = date;
        this.title = title;
        this.shouldNotify = shouldNotify;
    }

    public boolean equalsTo(Deadline m) {
        return moduleCode.equals(m.moduleCode)
                && date.equals(m.date);
    }
}
