package com.example.myapp.ui.add;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.myapp.R;

import java.text.DecimalFormat;
import java.util.GregorianCalendar;


public class MyDatePicker extends LinearLayout {
    private Context context;
    private NumberPicker ypicker;
    private NumberPicker mopicker;
    private NumberPicker dpicker;
    private NumberPicker hpicker;
    private NumberPicker mpicker;
    private TextView ytext;
    private TextView motext;
    private TextView dtext;
    private TextView htext;
    private TextView mtext;
    public static final int YP=1;
    public static final int MOP=2;
    public static final int DP=3;
    public static final int HP=4;
    public static final int MP=5;
    private static int [] MonthNumber={31,28,31,30,31,30,31,31,30,31,30,31};
    GregorianCalendar calendar;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyDatePicker(Context context) {
        this(context,null);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyDatePicker(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyDatePicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyDatePicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
       super(context, attrs, defStyleAttr, defStyleRes);
       this.context=context;
        LayoutInflater.from(context).inflate(R.layout.my_date_picker,this,true);
        ypicker=findViewById(R.id.time_y);
        mopicker=findViewById(R.id.time_mo);
        dpicker=findViewById(R.id.time_d);
        hpicker=findViewById(R.id.time_h);
        mpicker=findViewById(R.id.time_m);
        ytext=findViewById(R.id.ytext);
        motext=findViewById(R.id.motext);
        dtext=findViewById(R.id.dtext);
        htext=findViewById(R.id.htext);
        mtext=findViewById(R.id.mtext);
        calendar=new GregorianCalendar();
        NumberPicker.OnValueChangeListener onValueChangeListener= new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                onTimeChangedListener.onTimeChanged(ypicker.getValue(),mopicker.getValue(),dpicker.getValue(),hpicker.getValue(),mpicker.getValue());
            }
        };
        ypicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                onTimeChangedListener.onTimeChanged(ypicker.getValue(),mopicker.getValue(),dpicker.getValue(),hpicker.getValue(),mpicker.getValue());
               if(mopicker.getValue()==2)
               {
                   if(calendar.isLeapYear(newVal)){
                       dpicker.setMaxValue(29);
                   }
                   else
                       dpicker.setMaxValue(28);
               }

            }
        });
        mopicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                onTimeChangedListener.onTimeChanged(ypicker.getValue(),mopicker.getValue(),dpicker.getValue(),hpicker.getValue(),mpicker.getValue());
                if(calendar.isLeapYear(ypicker.getValue())){
                    if(newVal==2) dpicker.setMaxValue(29);
                }
                else
                dpicker.setMaxValue(MonthNumber[newVal-1]);
            }
        });
        dpicker.setOnValueChangedListener(onValueChangeListener);
        hpicker.setOnValueChangedListener(onValueChangeListener);
        mpicker.setOnValueChangedListener(onValueChangeListener);
     setMinValue(1900,1,1,0,0);
     setMaxValue(2099,12,31,23,59);
    }
    public String getTimeString(){
        DecimalFormat df=new DecimalFormat("00");
        String TimeString=""+ypicker.getValue()+"年"+df.format(mopicker.getValue())+"月"+
                df.format(dpicker.getValue())+"日 "+df.format(hpicker.getValue())+":"+df.format(mpicker.getValue());
        return  TimeString;
    }
    public void setMinValue(int y,int mo,int d,int h,int m){
      ypicker.setMinValue(y);
      mopicker.setMinValue(mo);
      dpicker.setMinValue(d);
      hpicker.setMinValue(h);
      mpicker.setMinValue(m);
    }
    public void setMaxValue(int y,int mo,int d,int h,int m)
    {
        ypicker.setMaxValue(y);
        mopicker.setMaxValue(mo);
        dpicker.setMaxValue(d);
        hpicker.setMaxValue(h);
        mpicker.setMaxValue(m);
    }
    public interface OnTimeChangedListener{
        void onTimeChanged(int y,int mo,int d,int h,int m);
    }
    private OnTimeChangedListener onTimeChangedListener;

    public void setOnTimeChangedListener(OnTimeChangedListener onTimeChangedListener) {
        this.onTimeChangedListener = onTimeChangedListener;
    }
    public void setVisibility(int v) {
        ypicker.setVisibility(v);
        mopicker.setVisibility(v);
        dpicker.setVisibility(v);
        hpicker.setVisibility(v);
        mpicker.setVisibility(v);
        ytext.setVisibility(v);
        motext.setVisibility(v);
        dtext.setVisibility(v);
        htext.setVisibility(v);
        mtext.setVisibility(v);
    }
    public void setValue(int y,int mo,int d,int h,int m ){
        ypicker.setValue(y);
        mopicker.setValue(mo);
        dpicker.setMaxValue(MonthNumber[mo-1]);
        dpicker.setValue(d);
        hpicker.setValue(h);
        mpicker.setValue(m);
    }
    public void setValue(int pickertype,int value){
        switch (pickertype){
            case YP:{
                ypicker.setValue(value);
                break;
            }
            case MOP:{
                mopicker.setValue(value);
                break;
            }
            case DP:{
                dpicker.setValue(value);
                break;
            }
            case HP:{
                hpicker.setValue(value);
                break;
            }
            case MP:{
                mpicker.setValue(value);
                break;
            }
        }
    }
    public void setMinValue(int pickertype,int value){
        switch (pickertype){
            case YP:{
                ypicker.setMinValue(value);
                break;
            }
            case MOP:{
                mopicker.setMinValue(value);
                break;
            }
            case DP:{
                dpicker.setMinValue(value);
                break;
            }
            case HP:{
                hpicker.setMinValue(value);
                break;
            }
            case MP:{
                mpicker.setMinValue(value);
                break;
            }
        }
    }
    public void setMaxValue(int pickertype,int value){
        switch (pickertype){
            case YP:{
                ypicker.setMaxValue(value);
                break;
            }
            case MOP:{
                mopicker.setMaxValue(value);
                break;
            }
            case DP:{
                dpicker.setMaxValue(value);
                break;
            }
            case HP:{
                hpicker.setMaxValue(value);
                break;
            }
            case MP:{
                mpicker.setMaxValue(value);
                break;
            }
        }
    }

}
