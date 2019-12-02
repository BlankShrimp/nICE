package com.armpits.nice.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

import com.armpits.nice.models.Material;

@Dao
public interface DaoMaterial {
    @Query("SELECT * FROM material")
    LiveData<List<Material>> getAll();

    @Query("SELECT * FROM material WHERE moduleCode = :moduleCode")
    LiveData<List<Material>> getByModuleCode(String moduleCode);

    @Insert
    void insertAll(Material... materials);

    @Delete
    void delete(Material material);
}