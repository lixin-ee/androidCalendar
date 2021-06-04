package com.example.myapp.ui.home;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.CalendarLib.Day;
import com.example.myapp.CalendarLib.MyCalendar;
import com.example.myapp.R;

import java.util.List;

public class Calendarview extends RecyclerView {
    private List<DateInfo> infos;
    private int offset;
    private int Month;
    private int Year;
    public Calendarview(@NonNull Context context) {
        this(context,null);
    }

    public Calendarview(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public Calendarview(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public void init(List<DateInfo> dateInfos,int offset,int year,int month) {
        infos = dateInfos;
        this.offset=offset;
        Year=year;
        Month=month;
        setLayoutManager(new GridLayoutManager(getContext(), 7, RecyclerView.VERTICAL, false));
        setAdapter(new QuickAdapter<DateInfo>(dateInfos) {
            @Override
            public int getLayoutId(int viewType) {
                return R.layout.square;
            }
            {
                selectedPosition=offset;
            }
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void convert(VH holder, DateInfo data, int position) {
                TextView riqitext=holder.getView(R.id.riqi);
                if(position>=offset)
                {
                    setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void OnItemClick(View view, VH holder, int position) {
                           onDaySelectedListener.onDaySeleceted(year,month,position-offset+1);
                        }
                    });
                holder.itemView.setSelected(selectedPosition==position);
                holder.itemView.setBackgroundResource(R.drawable.calendar_item_select);
                    riqitext.setTextColor(getResources().getColor(R.color.black));
                Day today= MyCalendar.getToday();
                if(today.getMonth()==month&&today.getYear()==year&&position-offset+1==today.getDay())
                {
                    if(selectedPosition==position) {
                        holder.itemView.setBackgroundResource(R.drawable.calendar_today_select);
                        riqitext.setTextColor(getResources().getColor(R.color.white));
                    }
                    else
                    riqitext.setTextColor(getResources().getColor(R.color.todaytext));
                }
                holder.itemView.setOnClickListener(v -> {
                    onItemClickListener.OnItemClick(v,holder,position);
                    int tempPosition=selectedPosition;
                    selectedPosition=position;
                    notifyItemChanged(tempPosition);
                    notifyItemChanged(position);
                });
                }
                String bText = "";
                for (String x : data.bottomText) {
                    bText += x + "\n";
                }
                TextView jieri=holder.getView(R.id.jieri);
                TextView riqi=holder.getView(R.id.riqi);
                jieri.setText(bText);
                riqi.setText(data.day);
            }
        });
    }
    protected OnDaySelectedListener onDaySelectedListener;
    public interface OnDaySelectedListener{
        public void onDaySeleceted(int year,int month,int day);
    }
    public void setOnDaySelectedListener(OnDaySelectedListener onDaySelectedListener)
    {
        this.onDaySelectedListener=onDaySelectedListener;
    }

}
