package com.example.healthtracker;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "masse_corporelle")
public class MasseCorporelleEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String value;
    private long timestamp;

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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}