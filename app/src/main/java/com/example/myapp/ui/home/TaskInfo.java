package com.example.myapp.ui.home;

import com.example.myapp.entity.TSitem;

import java.util.ArrayList;
import java.util.List;

public class TaskInfo {
    int TStype;
    int id;
    String type;
    String time1;
    String time2;
    String description;
    int overDue;
    int tixing;
    public TaskInfo()
    {
        type="计划";
        time1="2021年1月29日 15:30";
        time2="2021年1月29日 15:00";
        description="学习新思想，争做新青年";
        overDue=0;
        TStype=TSitem.SCHEDULE;
        tixing=1111;
    }
    public TaskInfo(TSitem item)
    {
        type=item.getType();
        time1=item.getTime1();
        time2=item.getTime2();
        description=item.getDescription();
        overDue=item.getOverDue();
        id=item.getId();
        TStype=item.getTStype();
        tixing=item.getTxing();
    }
    public static List<TaskInfo> ListItemToListInfo(List<TSitem> tSitems){
        List<TaskInfo>  infos=new ArrayList<>();
        for(TSitem x:tSitems)
        {
         infos.add(new TaskInfo(x));
        }
        return infos;
    }
}
