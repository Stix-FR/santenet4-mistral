
package com.example.healthtracker;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface TensionDao {
    @Insert
    void insert(TensionEntity tension);

    @Query("SELECT * FROM tension ORDER BY timestamp DESC")
    List<TensionEntity> getAll();
}
