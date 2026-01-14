package com.example.chiplanner.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
public class Task {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String title;
    public String description;

    @ColumnInfo(name = "is_completed")
    public boolean isCompleted = false;
}
