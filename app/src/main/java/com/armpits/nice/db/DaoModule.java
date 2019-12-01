package com.armpits.nice.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

import com.armpits.nice.models.Module;

@Dao
public interface DaoModule {
    @Query("SELECT * FROM module")
    List<Module> getAll();

    @Query("SELECT * FROM module WHERE enableDownloads")
    List<Module> getToDownload();

    @Query("SELECT * FROM module WHERE enableNotifications")
    List<Module> getToNotify();

    @Query("SELECT * FROM module WHERE addDDLsToCalendar")
    List<Module> getToAddToCalendar();

    @Insert
    void insertAll(Module... modules);

    @Delete
    void delete(Module module);
}