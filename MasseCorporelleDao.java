package com.example.healthtracker;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MasseCorporelleDao {
    @Query("SELECT * FROM masse_corporelle ORDER BY timestamp DESC")
    List<MasseCorporelleEntity> getAll();

    @Insert
    void insert(MasseCorporelleEntity masseCorporelle);

    @Delete
    void delete(MasseCorporelleEntity masseCorporelle);
}