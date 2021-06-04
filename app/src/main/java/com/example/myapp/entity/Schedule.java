package com.example.myapp.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "schedules")
public class Schedule implements TSitem{
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name ="sid")
    public int sid;
    @ColumnInfo(name="endtime")
    public String endDate;
    @ColumnInfo(name="starttime")
    public String startDate;
    @ColumnInfo(name="overDue")
    public int overDue;
    @ColumnInfo(name="description")
    public String description;
    @ColumnInfo(name="tixing")
    public int tixing;
    public Schedule(){}
    public Schedule(String starttime, String description,String endtime,int txing){
      endDate=endtime;
      startDate=starttime;
      this.description=description;
      overDue=0;
      tixing=txing;
    }
    public Schedule(String starttime, String description,String endtime,int txing,int id){
        endDate=endtime;
        startDate=starttime;
        this.description=description;
        overDue=0;
        tixing=txing;
        sid=id;
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
        return startDate;
    }

    @Override
    public String getType() {
        return "计划";
    }

    @Override
    public int getOverDue() {
        return overDue;
    }
    @Override
    public int getTStype() {
        return SCHEDULE;
    }

    @Override
    public int getTxing() {
        return tixing;
    }


    @Override
    public int getId() {
        return sid;
    }

}
