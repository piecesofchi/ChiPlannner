package com.example.chiplanner;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks") // Nama tabel
public class Task {
    @PrimaryKey(autoGenerate = true)
    public int id; // ID unik otomatis

    public String title;
    public String description;
    public boolean isCompleted;

    // Constructor
    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.isCompleted = false;
    }
}