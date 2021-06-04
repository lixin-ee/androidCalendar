package com.example.myapp.ui.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapp.CalendarLib.Day;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class DateInfo {
    String day;
    List<String> bottomText;
    DateInfo(Day Day)
    {
     day=""+Day.getDay();
     bottomText=Day.getBottomtext();
    }
    private DateInfo(){day="";bottomText=new ArrayList<>();}
    public static List<DateInfo> DaysToDateInfos(List<Day> days)
    {
        List<DateInfo> list= new ArrayList<>();
        int offset=days.get(0).getWeekDay();
        for(int i=0;i<offset-1;i++){
           list.add(new DateInfo());
        }
        for(Day x:days){
         list.add(new DateInfo(x));
        }
        return  list;
    }

}

