package com.example.myapp.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.RequiresApi;

import com.example.myapp.CalendarLib.Day;
import com.example.myapp.CalendarLib.MyCalendar;
import com.example.myapp.R;
import static com.example.myapp.CalendarLib.MyCalendar.*;
import com.example.myapp.CalendarLib.*;
import java.util.List;
import static com.example.myapp.ui.home.DateInfo.*;
@RequiresApi(api = Build.VERSION_CODES.N)
public class MyCalendarView extends RelativeLayout {
    private ViewFlipper flipper;
    private Day currentDay;
    private GestureDetector myGestureDectector;
    private Context context;
    private int MonthOffset;
    private TextView yeartext;
    private TextView monthtext;
    private TextView daytext;
    private Button dayButton;
    private Calendarview currentView;
    private OnDayChangedListener onDayChangedListener;
    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() - e2.getX() > 120) {
                // 像左滑动
                ToNextMonth();
                return true;
            } else if (e1.getX() - e2.getX() < -120) {
                // 向右滑动
               ToPreMonth();
                return true;
            }
            return false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        myGestureDectector.onTouchEvent(ev);
        return false;
    }

    public MyCalendarView(Context context) {
        this(context,null);
    }

    public MyCalendarView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyCalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }
    public Calendarview.OnDaySelectedListener onDaySelectedListener=new Calendarview.OnDaySelectedListener() {
        @Override
        public void onDaySeleceted(int year, int month, int day) {
            currentDay=getDay(year,month,day);
            setDateText();
            onDayChangedListener.onDayChange(currentDay.getYear(),currentDay.getMonth(),currentDay.getDay());
        }
    };
    public MyCalendarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context=context;
           //TypedArray attributes=context.obtainStyledAttributes(attrs, R.styleable.MyCalendarView,defStyleAttr,defStyleRes);
    }
    public void init(int year,int month,int day){
        LunarCalendar.init(context);
        currentDay=getDay(year,month,day);
        LayoutInflater.from(context).inflate(R.layout.mycalendar_view,this,true);
        dayButton=findViewById(R.id.daybutton);
        yeartext=findViewById(R.id.textView2);
        monthtext=findViewById(R.id.textView3);
        daytext=findViewById(R.id.textView5);
        dayButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Day today=getToday();
                if(currentDay.getYear()==today.getYear()&&currentDay.getMonth()==today.getMonth())
                {
                    currentDay=today;
                    QuickAdapter<DateInfo> adapter= (QuickAdapter<DateInfo>)currentView.getAdapter();
                    adapter.setSelectedPosition(MonthOffset+currentDay.getDay()-1);
                    setDateText();
                    onDayChangedListener.onDayChange(currentDay.getYear(),currentDay.getMonth(),currentDay.getDay());
                }else {
                    currentDay=today;
                    addCalendarView(getMonth(currentDay.getYear(),currentDay.getMonth()),1);
                    flipper.setInAnimation(context,R.anim.right_in);
                    flipper.setOutAnimation(context,R.anim.right_out);
                    flipper.showNext();
                    flipper.removeViewAt(0);
                    onDayChangedListener.onDayChange(currentDay.getYear(),currentDay.getMonth(),currentDay.getDay());
                }

            }
        });
        dayButton.setText(""+currentDay.getDay());
        flipper= findViewById(R.id.flipper);
        myGestureDectector=new GestureDetector(context,new MyGestureListener());
        addCalendarView(getMonth(currentDay.getYear(),currentDay.getMonth()),0);
    }
    public void ToNextMonth(){
        List<Day> month=getNext(currentDay.getYear(),currentDay.getMonth());
        currentDay=month.get(0);
        addCalendarView(month,1);
        flipper.setInAnimation(context,R.anim.right_in);
        flipper.setOutAnimation(context,R.anim.right_out);
        flipper.showNext();
        flipper.removeViewAt(0);
        onDayChangedListener.onDayChange(currentDay.getYear(),currentDay.getMonth(),currentDay.getDay());
    }
    public void ToPreMonth(){
        List<Day> month=getPre(currentDay.getYear(),currentDay.getMonth());
        currentDay=month.get(0);
        addCalendarView(month,1);
        flipper.setInAnimation(context,R.anim.left_in);
        flipper.setOutAnimation(context,R.anim.left_out);
        flipper.showPrevious();
        flipper.removeViewAt(0);
        onDayChangedListener.onDayChange(currentDay.getYear(),currentDay.getMonth(),currentDay.getDay());
    }
    public void setDateText(){
        yeartext.setText( ""+currentDay.getYear()+"年\n"+MyCalendar.getTrunkBranch(currentDay.getLunarDay().getYear())+"年");
        monthtext.setText( ""+currentDay.getMonth()+"月\n"+LunarCalendar.numToChineseMonth1(currentDay.getLunarDay().getMonth(),currentDay.getLunarDay().getIsLeapMonth()));
        daytext.setText(""+currentDay.getDay()+"日\n"+ LunarCalendar.DAY_STR[currentDay.getLunarDay().getDay()-1]);
    }
    public void addCalendarView(List<Day> month,int index){
        currentView=new Calendarview(context);
        setDateText();
        List<DateInfo> DateInfos=DaysToDateInfos(month);
        MonthOffset=DateInfos.size()-month.size();
        currentView.init(DateInfos,MonthOffset,currentDay.getYear(),currentDay.getMonth());
        currentView.setOnDaySelectedListener(onDaySelectedListener);
        QuickAdapter<DateInfo> adapter= (QuickAdapter<DateInfo>)currentView.getAdapter();
        adapter.setSelectedPosition(MonthOffset+currentDay.getDay()-1);
        flipper.addView(currentView,index);
    }
    public void ToNextDay(){

        Day nextDay=MyCalendar.getNextDay(currentDay.getYear(),currentDay.getMonth(),currentDay.getDay());
        if(currentDay.getYear()==nextDay.getYear()&&currentDay.getMonth()==nextDay.getMonth())
        {
            currentDay=nextDay;
            QuickAdapter<DateInfo> adapter= (QuickAdapter<DateInfo>)currentView.getAdapter();
            adapter.setSelectedPosition(MonthOffset+currentDay.getDay()-1);
            setDateText();
        }else {
            currentDay=nextDay;
            addCalendarView(getMonth(currentDay.getYear(),currentDay.getMonth()),1);
            flipper.setInAnimation(context,R.anim.right_in);
            flipper.setOutAnimation(context,R.anim.right_out);
            flipper.showNext();
            flipper.removeViewAt(0);
        }
    }
    public void ToPreDay(){
        Day preDay=MyCalendar.getPreDay(currentDay.getYear(),currentDay.getMonth(),currentDay.getDay());
        if(currentDay.getYear()==preDay.getYear()&&currentDay.getMonth()==preDay.getMonth())
        {
            currentDay=preDay;
            QuickAdapter<DateInfo> adapter= (QuickAdapter<DateInfo>)currentView.getAdapter();
            adapter.setSelectedPosition(MonthOffset+currentDay.getDay()-1);
            setDateText();
        }else {
            currentDay=preDay;
            addCalendarView(getMonth(currentDay.getYear(),currentDay.getMonth()),1);
            flipper.setInAnimation(context,R.anim.left_in);
            flipper.setOutAnimation(context,R.anim.left_out);
            flipper.showPrevious();
            flipper.removeViewAt(0);
        }
    }
    public interface OnDayChangedListener{
        void onDayChange(int y,int m,int d);
    }
    public void setOnDayChangedListener(OnDayChangedListener onDayChangedListener){
            this.onDayChangedListener=onDayChangedListener;
    }

    public Day getCurrentDay() {
        return currentDay;
    }
}
