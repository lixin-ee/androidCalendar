package com.example.myapp.Dao;

import android.widget.LinearLayout;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapp.entity.Task;

import java.util.Date;
import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    long insertTaskre(Task task);
    @Insert
    void insertTask(Task task);
    @Insert
    void insertTaskS(List<Task> taskList);
    @Delete
    void deleteTask(Task task);
    @Delete
    void deleteTasks(List<Task> taskList);
    @Update
    void updateTask(Task task);
    @Update
    void updateTasks(List<Task> taskList);
    @Query("SELECT * FROM tasks")
    List<Task> loadAll();
    @Query("SELECT * FROM tasks WHERE tid=(:taskid)")
    Task findTaskById(int taskid);
    @Query("SELECT * FROM tasks WHERE tid IN (:taskids)")
    List<Task> loadAllByIds(int[] taskids);
    @Query("SELECT * FROM tasks WHERE substr(endtime,1,11)=(:DateStr)")
    List<Task> findTaskbyDate(String DateStr);
   // @Query("SELECT * FROM tasks WHERE tid=(last_insert_rowid())")
    //Task findTaskByLastId();
}
