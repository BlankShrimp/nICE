package com.armpits.nice.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

import com.armpits.nice.models.Material;

@Dao
public interface DaoMaterial {
    @Query("SELECT * FROM material")
    List<Material> getAll();

    @Query("SELECT * FROM material WHERE module_code = :moduleCode")
    List<Material> getByModuleCode(String moduleCode);

    @Insert
    void insertAll(Material... materials);

    @Delete
    void delete(Material material);
}