package com.example.myapp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.example.myapp.entity.*;
import com.example.myapp.Dao.*;
@Database(entities ={Task.class, Schedule.class,User.class},version =1)
public abstract class MyDatabase extends RoomDatabase {
private static volatile MyDatabase myDatabase;
public static MyDatabase getInstance(Context context){
    if(myDatabase==null){
        synchronized (MyDatabase.class){
            if(myDatabase==null){
                myDatabase= Room.databaseBuilder
                        (context.getApplicationContext(),MyDatabase.class,"MyAppDatabase.db")
                        .addMigrations().build();
            }
        }
    }
    return myDatabase;
}
public abstract TaskDao taskDao();
public abstract ScheduleDao scheduleDao();
public abstract UserDao userDao();
}
