package com.example.myapp.ui.home;

import android.content.Context;
import android.nfc.cardemulation.CardEmulation;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.RequiresApi;

import com.example.myapp.CalendarLib.Day;
import com.example.myapp.CalendarLib.MyCalendar;
import com.example.myapp.MainActivity.DataOPByDateString;
import com.example.myapp.MainActivity;
import com.example.myapp.R;
import com.example.myapp.entity.TSitem;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static android.view.Gravity.CENTER;
import static com.example.myapp.entity.TSitem.SCHEDULE;
import static com.example.myapp.entity.TSitem.TASK;
import static com.example.myapp.ui.home.TaskInfo.ListItemToListInfo;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MyTaskView extends RelativeLayout {
    private Day currentDay;
    private ViewFlipper flipper;
    private GestureDetector myGestureDectector;
    private Context context;
    private Taskview currentView;
    private final List<TaskInfo>  currentDayInfos=new ArrayList<>();
    private TextView emptyView;
    private TextView emptyView1;
    private OnNextDayListener onNextDayListener;
    private OnPreDayListener onPreDayListener;
    public static final int LEFT=1;
    public static final int RIGHT=2;
    public static final int JUMP=3;
    public static final int FLUSH=4;
    public static final int INIT=5;
    private int OPFLAG;
    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() - e2.getX() > 120) {
                // 向左滑动
                OPFLAG=LEFT;
                currentDay=MyCalendar.getNextDay(currentDay.getYear(),currentDay.getMonth(),currentDay.getDay());
                getTaskInfos();
                return true;
            } else if (e1.getX() - e2.getX() < -120) {
                // 向右滑动
               OPFLAG=RIGHT;
               currentDay=MyCalendar.getPreDay(currentDay.getYear(),currentDay.getMonth(),currentDay.getDay());
               getTaskInfos();
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
    public MyTaskView(Context context) {
        this(context,null);
    }
    public MyTaskView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyTaskView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    public MyTaskView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context=context;
    }
    public void init(int year,int month,int day){
        LayoutInflater.from(context).inflate(R.layout.mytask_view,this,true);
        flipper=findViewById(R.id.taskview_flipper);
        emptyView=new TextView(context);
        emptyView.setText("今日暂无事件，祝你玩的开心");
        emptyView.setGravity(CENTER);
        emptyView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //啥都不用做，只是响应触碰。
            }
        });
        emptyView1=new TextView(context);
        emptyView1.setText("今日暂无事件，祝你玩的开心");
        emptyView1.setGravity(CENTER);
        emptyView1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //啥都不用做，只是响应触碰。
            }
        });
        myGestureDectector=new GestureDetector(context,new MyTaskView.MyGestureListener());
        currentDay= MyCalendar.getDay(year,month,day);
        OPFLAG=INIT;
        getTaskInfos();
    }
    public void ToNextDay() {
        addTaskView(1);
        flipper.setInAnimation(context,R.anim.right_in);
        flipper.setOutAnimation(context,R.anim.right_out);
        flipper.showNext();
        flipper.removeViewAt(0);
        onNextDayListener.OnNextDay();
    }
    public void ToPreDay(){
        addTaskView(1);
        flipper.setInAnimation(context,R.anim.left_in);
        flipper.setOutAnimation(context,R.anim.left_out);
        flipper.showPrevious();
        flipper.removeViewAt(0);
        onPreDayListener.OnPreDay();
    }
    public void Jump(){
        addTaskView(1);
        flipper.setInAnimation(context,R.anim.right_in);
        flipper.setOutAnimation(context,R.anim.right_out);
        flipper.showNext();
        flipper.removeViewAt(0);
    }
    public void addTaskView (int index)
    {
        if(currentDayInfos!=null&&currentDayInfos.size()!=0) {
            currentView=new Taskview(context);
            currentView.init(currentDayInfos,currentDay.getYear(),currentDay.getMonth(),currentDay.getDay());
            currentView.setOnItemDeleteListener(onItemDeleteListener);
            currentView.setOnItemUpdateListener(onItemUpdateListener);
            flipper.addView(currentView,index);
        }
        else{
            if(emptyView==flipper.getChildAt(0))
            flipper.addView(emptyView1,index);
            else flipper.addView(emptyView,index);
        }

    }

    public void  getTaskInfos() {
        MainActivity mainActivity=  (MainActivity)getContext();
        mainActivity.setOnQueryByDateFinishListener(new MainActivity.OnQueryByDateFinishListener() {
            @Override
            public void onQueryByDateFinish(List<TSitem> tSitems) {
                currentDayInfos.clear();
                currentDayInfos.addAll(ListItemToListInfo(tSitems));
                switch (OPFLAG)
                {
                    case INIT:
                    {
                        addTaskView(0);
                        break;
                    }
                    case LEFT:
                    {
                        ToNextDay();
                        break;
                    }
                    case RIGHT:
                    {
                        ToPreDay();
                        break;
                    }
                    case FLUSH:
                    {
                        flush();
                        break;
                    }
                    case JUMP:
                    {
                        Jump();
                        break;
                    }
                }

               }
        });
        DecimalFormat df=new DecimalFormat("00");
        String datestr=currentDay.getYear()+"年"+df.format(currentDay.getMonth())+"月"+
                df.format(currentDay.getDay())+"日";
        Log.d("day",datestr);
        MainActivity.DataOP dataOP= mainActivity.new DataOPByDateString(MainActivity.DataOP.QUERYBYDATE,datestr);
      MainActivity.DataQueryAsyncTask dA=new MainActivity.DataQueryAsyncTask();
      dA.execute(dataOP);
    }
    public interface OnNextDayListener{
        void OnNextDay();
    }
    public interface OnPreDayListener{
        void OnPreDay();
    }

    public void setOnNextDayListener(OnNextDayListener onNextDayListener) {
        this.onNextDayListener = onNextDayListener;
    }

    public void setOnPreDayListener(OnPreDayListener onPreDayListener) {
        this.onPreDayListener = onPreDayListener;
    }

    private Taskview.OnItemUpdateListener onItemUpdateListener=new Taskview.OnItemUpdateListener() {
        @Override
        public void onItemUpdate(TaskInfo info) throws ParseException {
            MainActivity mainActivity=(MainActivity) getContext();
            switch (info.TStype)
            {
                case TASK:{
                    String stoptime=info.time1;
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
                    GregorianCalendar calendar=new GregorianCalendar();
                    calendar.setTime(sdf.parse(stoptime));
                    int year=calendar.get(Calendar.YEAR);
                    int month=calendar.get(Calendar.MONTH)+1;
                    int day=calendar.get(Calendar.DATE);
                    int hour=calendar.get(Calendar.HOUR_OF_DAY);
                    int min=calendar.get(Calendar.MINUTE);
                    String des=info.description;
                    mainActivity.updateitem(year,month,day,hour,min,des,info.tixing,info.id);
                    break;
                }
                case SCHEDULE:{
                    String endtime=info.time1;
                    String starttime=info.time2;
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
                    GregorianCalendar calendar=new GregorianCalendar();
                    calendar.setTime(sdf.parse(starttime));
                    int year=calendar.get(Calendar.YEAR);
                    int month=calendar.get(Calendar.MONTH)+1;
                    int day=calendar.get(Calendar.DATE);
                    int hour=calendar.get(Calendar.HOUR_OF_DAY);
                    int min=calendar.get(Calendar.MINUTE);
                    calendar.setTime(sdf.parse(endtime));
                    int year2=calendar.get(Calendar.YEAR);
                    int month2=calendar.get(Calendar.MONTH)+1;
                    int day2=calendar.get(Calendar.DATE);
                    int hour2=calendar.get(Calendar.HOUR_OF_DAY);
                    int min2=calendar.get(Calendar.MINUTE);
                    String des=info.description;
                    mainActivity.updateitem(year,month,day,hour,min,year2,month2,day2,hour2,min2,des,info.tixing,info.id);
                    break;
                }
            }
        }
    };
    private Taskview.OnItemDeleteListener onItemDeleteListener=new Taskview.OnItemDeleteListener() {
        @Override
        public void onItemDelete(int id, int type) {
            MainActivity mainActivity=(MainActivity) getContext();
            MainActivity.DataOP dataOP=mainActivity.new DataOPByID(MainActivity.DataOP.DELETE,id,type);
            MainActivity.DataQueryAsyncTask dA=new MainActivity.DataQueryAsyncTask();
            dA.execute(dataOP);
            QuickAdapter<TaskInfo> adapter=(QuickAdapter<TaskInfo>)currentView.getAdapter();
          if(adapter.mDatas.size()==0){
              flipper.removeViewAt(0);
              flipper.addView(emptyView);
          }
        }
    };
    public void flush()
    {
        flipper.removeViewAt(0);
        addTaskView(0);
    }
    public void setOPFLAG(int OPFLAG) {
        this.OPFLAG = OPFLAG;
    }

    public void setCurrentDay(int year,int month,int day) {
        this.currentDay =MyCalendar.getDay(year,month,day);
    }
}
