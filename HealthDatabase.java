package com.example.healthtracker;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {TensionEntity.class, DiabeteEntity.class, SouffleEntity.class, MasseCorporelleEntity.class}, version = 1)
public abstract class HealthDatabase extends RoomDatabase {
    public abstract TensionDao tensionDao();
    public abstract DiabeteDao diabeteDao();
    public abstract SouffleDao souffleDao();
    public abstract MasseCorporelleDao masseCorporelleDao();
}
