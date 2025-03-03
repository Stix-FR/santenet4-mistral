
package com.example.healthtracker;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface SouffleDao {
    @Insert
    void insert(SouffleEntity souffle);

    @Query("SELECT * FROM souffle")
    List<SouffleEntity> getAll();
}
