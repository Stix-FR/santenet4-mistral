package com.example.healthtracker;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface MasseCorporelleDao {
    @Insert
    void insert(MasseCorporelleEntity masseCorporelle);

    @Query("SELECT * FROM masse_corporelle")
    List<MasseCorporelleEntity> getAll();
}
