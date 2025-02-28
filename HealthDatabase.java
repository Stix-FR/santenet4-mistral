
package com.example.healthtracker;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(
    entities = {
        TensionEntity.class,
        DiabeteEntity.class,
        MasseCorporelleEntity.class,
        SouffleEntity.class
    },
    version = 1
)
public abstract class HealthDatabase extends RoomDatabase {
    public abstract TensionDao tensionDao();
    public abstract DiabeteDao diabeteDao();
    public abstract MasseCorporelleDao masseCorporelleDao();
    public abstract SouffleDao souffleDao();
}
