package com.armpits.nice.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import com.armpits.nice.models.Log;

import java.util.List;

@Dao
public interface DaoLog {
    @Query("SELECT * FROM log")
    LiveData<List<Log>> getAll();

    @Insert
    void insertAll(Log... logs);

    @Delete
    void delete(Log log);
}
