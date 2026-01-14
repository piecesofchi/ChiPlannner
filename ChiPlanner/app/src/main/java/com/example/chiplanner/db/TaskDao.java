package com.example.chiplanner.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY id DESC")
    List<Task> getAllTasks();

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    Task getTaskById(int taskId);

    @Insert
    void insertTask(Task task);

    @Update
    void updateTask(Task task);

    @Delete
    void deleteTask(Task task);
}
