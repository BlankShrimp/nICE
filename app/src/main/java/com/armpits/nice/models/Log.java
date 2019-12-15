package com.armpits.nice.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Log {
    @PrimaryKey @NonNull public Date date;
    public String message;

    public Log(Date date, String message) {
        this.date = date;
        this.message = message;
    }
}
