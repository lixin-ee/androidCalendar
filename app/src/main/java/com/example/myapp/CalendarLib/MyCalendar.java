package com.example.myapp.CalendarLib;

import android.icu.util.GregorianCalendar;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Debug;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

import static com.example.myapp.CalendarLib.TrunkBranchAnnals.*;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MyCalendar {
    private static int [] MonthNumber={31,28,31,30,31,30,31,31,30,31,30,31};

public static Day getToday(){
 GregorianCalendar gregorianCalendar=new GregorianCalendar();
int weekday=gregorianCalendar.get(Calendar.DAY_OF_WEEK);
int year=gregorianCalendar.get(Calendar.YEAR);
int date=gregorianCalendar.get(Calendar.DATE);
int month=gregorianCalendar.get(Calendar.MONTH)+1;
if(weekday==1) weekday=7;
else weekday=weekday-1;
return new Day(year,month,date,weekday);
}

public static Day getDay(int y,int m,int d){
    GregorianCalendar gregorianCalendar=new GregorianCalendar(y,m-1,d);
        int weekday=gregorianCalendar.get(Calendar.DAY_OF_WEEK);
    if(weekday==1) weekday=7;
    else weekday=weekday-1;
    return new Day(y,m,d,weekday);
}
public static Day getNextDay(int y,int m,int d){
    GregorianCalendar gregorianCalendar=new GregorianCalendar(y,m-1,d);
    gregorianCalendar.add(Calendar.DATE,1);
    int weekday=gregorianCalendar.get(Calendar.DAY_OF_WEEK);
    int year=gregorianCalendar.get(Calendar.YEAR);
    int date=gregorianCalendar.get(Calendar.DATE);
    int month=gregorianCalendar.get(Calendar.MONTH)+1;
    if(weekday==1) weekday=7;
    else weekday=weekday-1;
    return new Day(year,month,date,weekday);
}
public static Day getPreDay(int y,int m,int d){
    GregorianCalendar gregorianCalendar=new GregorianCalendar(y,m-1,d);
    gregorianCalendar.add(Calendar.DATE,-1);
    int weekday=gregorianCalendar.get(Calendar.DAY_OF_WEEK);
    int year=gregorianCalendar.get(Calendar.YEAR);
    int date=gregorianCalendar.get(Calendar.DATE);
    int month=gregorianCalendar.get(Calendar.MONTH)+1;
    if(weekday==1) weekday=7;
    else weekday=weekday-1;
    return new Day(year,month,date,weekday);
}
public static List<Day> getMonth(int y, int m) {
        ArrayList<Day> MonthDays=new ArrayList<Day>();
        GregorianCalendar gregorianCalendar = new GregorianCalendar(y, m-1, 1);
        if(gregorianCalendar.isLeapYear(y)){
            if(m==2){
                for(int x=1;x<=MonthNumber[m-1]+1;x++)
                {
                    MonthDays.add(getDay(y,m,x));
                }
                return MonthDays;
            }
        }
        for(int x=1;x<=MonthNumber[m-1];x++)
        {
         MonthDays.add(getDay(y,m,x));
        }
        return MonthDays;
    }

    public static List<Day> getPre(int NowYear, int NowMonth) {
    if(NowMonth==1)
        return getMonth(NowYear-1,12);
    return getMonth(NowYear,NowMonth-1);
    }
    public static List<Day> getNext(int NowYear, int NowMonth){
    if(NowMonth==12)
        return getMonth(NowYear+1,1);
    return getMonth(NowYear,NowMonth+1);
    }
    public static String getTrunkBranch(int year){
        return getTrunkBranchYear(year);
    }
}
