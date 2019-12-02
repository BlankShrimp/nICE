package com.armpits.nice.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import com.armpits.nice.models.Deadline;

@Dao
public interface DaoDeadline {
    @Query("SELECT * FROM deadline")
    LiveData<List<Deadline>> getAll();

    @Query("SELECT * FROM deadline WHERE date >= datetime()")
    LiveData<List<Deadline>> getUpcoming();

    @Insert
    void insertAll(Deadline... deadlines);

    @Update
    void update(Deadline... deadlines);

    @Delete
    void delete(Deadline deadline);
}
