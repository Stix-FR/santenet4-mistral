package com.example.healthtracker;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tension")
public class TensionEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String value;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
