package com.example.myapp.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.myapp.entity.Schedule;


import java.sql.Date;
import java.util.List;

@Dao
public interface ScheduleDao {
    @Insert
    long insertSchedulere(Schedule schedule);
    @Insert
    void insertSchedule(Schedule schedule);
    @Insert
    void insertSchedules(List<Schedule> scheduleList);
    @Delete
    void deleteSchedule(Schedule user);
    @Delete
    void deleteSchedule(List<Schedule> scheduleList);
    @Update
    void updateSchedule(Schedule schedule);
    @Update
    void updateSchedules(List<Schedule> scheduleList);
    @Query("SELECT * FROM schedules")
    List<Schedule> loadAll();
    @Query("SELECT * FROM schedules WHERE sid=(:scheid)")
    Schedule findScheById(int scheid);
    @Query("SELECT * FROM schedules WHERE sid IN(:scheids)")
    List<Schedule> loadAllByIds(int[] scheids);
    @Query("SELECT * FROM schedules WHERE substr(starttime,1,11)=(:DateStr)")
    List<Schedule> findScheByDate(String DateStr);
    //@Query("SELECT * FROM schedules WHERE sid =(last_insert_rowid())")
    // Schedule findScheByLastId();
}
