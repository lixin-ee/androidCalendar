package com.example.myapp.ui.home;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.CalendarLib.Day;
import com.example.myapp.R;
import com.example.myapp.entity.Task;

import java.text.ParseException;
import java.util.List;

import static com.example.myapp.entity.TSitem.SCHEDULE;
import static com.example.myapp.entity.TSitem.TASK;

public class Taskview extends RecyclerView {
    private Day day;
    private List<TaskInfo> Infos;
    QuickAdapter<TaskInfo> adapter;
    public Taskview(@NonNull Context context) {
        this(context,null);
    }

    public Taskview(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public Taskview(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public void init(List<TaskInfo> infos,int year,int month,int day){
        Infos=infos;
        setLayoutManager(new LinearLayoutManager(getContext(), VERTICAL,false));
        adapter=new QuickAdapter<TaskInfo>(infos) {

            @Override
            public int getLayoutId(int viewType) {
                return R.layout.taskitem;
            }
            @Override
            public void convert(VH holder, TaskInfo data, int position) {
                TextView typeText=holder.getView(R.id.type);
                TextView time1Text=holder.getView(R.id.time1);
                TextView time2Text=holder.getView(R.id.time2);
                Button deleteButton=holder.getView(R.id.delete_item);
                TextView descriptionText=holder.getView(R.id.description);
                LinearLayout typeBackGround=holder.getView(R.id.typeBackGround);
                typeText.setText(data.type);
                switch ( data.TStype)
                {
                    case TASK:{
                        time1Text.setText("截至时间："+data.time1);
                        time2Text.setText("完成时间："+data.time2);
                        typeBackGround.setBackgroundResource(R.color.light_blue_400);
                        break;
                    }
                    case SCHEDULE:{
                        time1Text.setText("结束时间："+data.time1);
                        time2Text.setText("开始时间："+data.time2);
                        typeBackGround.setBackgroundResource(R.color.todaytext);
                        break;
                    }
                }
                descriptionText.setText(data.description);
                deleteButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adapter.mDatas.remove(position);
                        onItemDeleteListener.onItemDelete(data.id,data.TStype);
                        adapter.notifyDataSetChanged();
                    }
                });
                holder.itemView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            onItemUpdateListener.onItemUpdate(data);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
                    }
                });

            }
        };
        setAdapter(adapter);
    }
   public interface OnItemDeleteListener{
       void  onItemDelete(int id,int type);
   }
   public interface OnItemUpdateListener{
        void onItemUpdate(TaskInfo info) throws ParseException;
   }
   private  OnItemDeleteListener onItemDeleteListener;
   private OnItemUpdateListener onItemUpdateListener;

    public void setOnItemDeleteListener(OnItemDeleteListener onItemDeleteListener) {
        this.onItemDeleteListener = onItemDeleteListener;
    }

    public void setOnItemUpdateListener(OnItemUpdateListener onItemUpdateListener) {
        this.onItemUpdateListener = onItemUpdateListener;
    }
}
