package com.armpits.nice.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

import com.armpits.nice.models.Deadline;

@Dao
public interface DaoDeadline {
    @Query("SELECT * FROM deadline")
    List<Deadline> getAll();

    @Query("SELECT * FROM deadline WHERE date >= datetime()")
    List<Deadline> getUpcoming();

    @Insert
    void insertAll(Deadline... deadlines);

    @Delete
    void delete(Deadline deadline);
}
