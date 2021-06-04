package com.example.myapp.ui.home;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapp.CalendarLib.Day;
import com.example.myapp.CalendarLib.MyCalendar;
@RequiresApi(api = Build.VERSION_CODES.N)
public class HomeViewModel extends ViewModel {
    private int year;
    private int month;
    private int day;


    public HomeViewModel() {
        Day today=MyCalendar.getToday();
        this.year=today.getYear();
        this.month=today.getMonth();
        this.day=today.getDay();
    }
    public void setData(int year,int month,int day) {
        this.year = year;
        this.month=month;
        this.day=day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public int getDay() {
        return day;
    }
}