
package com.example.healthtracker;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "masse_corporelle")
public class MasseCorporelleEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String value; // IMC
    private float poids;
    private float taille;
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

    public float getPoids() {
        return poids;
    }

    public void setPoids(float poids) {
        this.poids = poids;
    }

    public float getTaille() {
        return taille;
    }

    public void setTaille(float taille) {
        this.taille = taille;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
