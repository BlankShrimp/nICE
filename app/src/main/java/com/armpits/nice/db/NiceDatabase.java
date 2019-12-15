package com.armpits.nice.db;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.armpits.nice.models.Deadline;
import com.armpits.nice.models.Log;
import com.armpits.nice.models.Material;
import com.armpits.nice.models.Module;

@Database(entities = {Deadline.class, Module.class, Material.class, Log.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverter.class})

public abstract class NiceDatabase extends RoomDatabase {
    public abstract DaoDeadline deadlineDao();
    public abstract DaoModule   moduleDao();
    public abstract DaoMaterial materialDao();
    public abstract DaoLog logDao();

    private static volatile NiceDatabase INSTANCE;
    private static final int N_THREADS = 4;
    static final ExecutorService dbWriter = Executors.newFixedThreadPool(N_THREADS);

    public static NiceDatabase getDatabase(final Context context) {
        if (INSTANCE == null)
            synchronized (NiceDatabase.class) {
                if (INSTANCE == null)
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            NiceDatabase.class, "nice_database").build();
            }
        return INSTANCE;
    }

    public static List<Deadline> getAllDeadlines() {
        return INSTANCE.deadlineDao().getAll().getValue();
    }
    public static List<Material> getAllMaterials() {
        return INSTANCE.materialDao().getAll().getValue();
    }
    public static LiveData<List<Module>> getAllModules() {
        return INSTANCE.moduleDao().getAll();
    }
    public static LiveData<List<Log>> getAllLogs() {
        return INSTANCE.logDao().getAll();
    }

    public static void insert(Deadline... deadlines) {
        dbWriter.execute(() -> INSTANCE.deadlineDao().insertAll(deadlines));
    }
    public static void insert(Material... materials) {
        dbWriter.execute(() -> INSTANCE.materialDao().insertAll(materials));
    }
    public static void insert(Module... modules) {
        dbWriter.execute(() -> INSTANCE.moduleDao().insertAll(modules));
    }
    public static void insert(Log... logs) {
        dbWriter.execute(() -> INSTANCE.logDao().insertAll(logs));
    }

    public static void update(Deadline... deadlines) {
        dbWriter.execute(() -> INSTANCE.deadlineDao().insertAll(deadlines));
    }
    public static void update(Material... materials) {
        dbWriter.execute(() -> INSTANCE.materialDao().insertAll(materials));
    }
    public static void update(Module... modules) {
        dbWriter.execute(() -> INSTANCE.moduleDao().update(modules));
    }
}