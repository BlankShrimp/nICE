package com.armpits.nice.db;

import androidx.room.TypeConverter;
import java.util.Date;

public class DateConverter {
    @TypeConverter
    public static Date longToDate(Long date){
        return date == null ? null: new Date(date);
    }

    @TypeConverter
    public static Long dateToLong(Date date){
        return date == null ? null : date.getTime();
    }
}