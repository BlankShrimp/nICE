package com.armpits.nice.db;

import com.armpits.nice.models.Deadline;
import com.armpits.nice.models.Material;
import com.armpits.nice.models.Module;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Deadline.class, Module.class, Material.class}, version = 1)
@TypeConverters({DateConverter.class})

public abstract class NiceDatabase extends RoomDatabase {
    public abstract Deadline deadlineDao();
    public abstract Module   moduleDao();
    public abstract Material materialDao();
}