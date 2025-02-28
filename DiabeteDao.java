
package com.example.healthtracker;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface DiabeteDao {
    @Insert
    void insert(DiabeteEntity diabete);

    @Query("SELECT * FROM diabete")
    List<DiabeteEntity> getAll();
}
