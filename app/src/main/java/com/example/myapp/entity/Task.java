package com.example.myapp.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
public class Task implements TSitem{
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="tid")
    public int tid;
    @ColumnInfo(name="endtime")
    public String endDate;
    @ColumnInfo(name="fintime")
    public String finDate;
    @ColumnInfo(name = "overDue")
    public int overDue;
    @ColumnInfo(name="description")
    public  String description;
    @ColumnInfo(name="tixing")
    public int tixing;
    public Task(){}
    public Task(String endtime, String de,int txing)  {
        endDate=endtime;
        overDue=0;
        description=de;
        finDate="未完成";
        tixing=txing;
    }
    public Task(String endtime, String de,int txing,int id)
    {
        endDate=endtime;
        overDue=0;
        description=de;
        finDate="未完成";
        tixing=txing;
        this.tid=id;
    }
    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getTime1() {
        return endDate;
    }

    @Override
    public String getTime2() {
        return finDate;
    }

    @Override
    public String getType() {
        return "任务";
    }

    @Override
    public int getOverDue() {
        return overDue;
    }

    @Override
    public int getTStype() {
        return TASK;
    }

    @Override
    public int getTxing() {
        return tixing;
    }

    @Override
    public int getId() {
        return tid;
    }
}
