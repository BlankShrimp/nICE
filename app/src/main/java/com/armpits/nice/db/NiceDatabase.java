package com.armpits.nice.db;

import android.content.Context;

import com.armpits.nice.models.Deadline;
import com.armpits.nice.models.Material;
import com.armpits.nice.models.Module;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Deadline.class, Module.class, Material.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverter.class})

public abstract class NiceDatabase extends RoomDatabase {
    public abstract DaoDeadline deadlineDao();
    public abstract DaoModule   moduleDao();
    public abstract DaoMaterial materialDao();

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
    public static List<Module> getAllModules() {
        return INSTANCE.moduleDao().getAll().getValue();
    }

    public static void insert(Deadline deadline) {
        dbWriter.execute(() -> INSTANCE.deadlineDao().insertAll(deadline));
    }
    public static void insert(Material material) {
        dbWriter.execute(() -> INSTANCE.materialDao().insertAll(material));
    }
    public static void insert(Module module) {
        dbWriter.execute(() -> INSTANCE.moduleDao().insertAll(module));
    }
}