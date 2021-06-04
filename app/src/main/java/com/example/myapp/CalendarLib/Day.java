package com.example.myapp.CalendarLib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.myapp.CalendarLib.LunarCalendar.*;

public class Day {
    private int year;
    private int month;
    private int day;
    private int weekDay;
    private LunarDay lunarDay;
    public Day(int y,int m,int d,int wd){

            year = y;
            month = m;
            day = d;
            lunarDay = new LunarDay(y, m, d);
            weekDay = wd;
    }
    public Day(int ly,int lm,int ld,int wd,int isLeapMonth)
    {
           year=ly;
           month=lm;
           day=ld;
           lunarDay=new LunarDay(year,month,day,isLeapMonth);
           weekDay=wd;
    }

    public int getDay() {
        return day;
    }

    public int getYear() {
        return year;
    }

    public int getWeekDay() {
        return weekDay;
    }

    public int getMonth() {
        return month;
    }

    public List<String> getBottomtext(){
        List<String> texts=new ArrayList<String>();
       texts.add( getLunarText(year,month,day));
       Collections.addAll(texts,  getSpecialFestival(year,month,day) );
        return texts;
    }
    public LunarDay getLunarDay() {
        return lunarDay;
    }

    public class LunarDay {
        private int year;
        private int month;
        private int day;
        private int isLeapMonth;
        public LunarDay(int year,int month,int day) {
                int [] ymd= LunarUtil.solarToLunar(year,month,day);
                this.year=ymd[0];
                this.month=ymd[1];
                this.day=ymd[2];
                this.isLeapMonth= ymd[3];
        }
        public LunarDay(int ly,int lm,int ld,int isLeap)
        {
            this.year=ly;
            this.month=lm;
            this.day=ld;
            isLeapMonth=isLeap;
        }
        public int getDay() {
            return day;
        }
        public int getMonth() {
            return month;
        }
        public int getYear() {
            return year;
        }

        public int getIsLeapMonth() {
            return isLeapMonth;
        }

        public Day getOutDay(){return Day.this;}
    }
}
