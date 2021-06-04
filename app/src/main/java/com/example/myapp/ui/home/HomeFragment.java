package com.example.myapp.ui.home;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.CalendarLib.Day;
import com.example.myapp.CalendarLib.MyCalendar;
import com.example.myapp.MainActivity;
import com.example.myapp.R;

import java.time.Month;

import static com.example.myapp.ui.home.MyTaskView.FLUSH;
import static com.example.myapp.ui.home.MyTaskView.JUMP;

@RequiresApi(api = Build.VERSION_CODES.N)
public class HomeFragment extends Fragment {
    private HomeViewModel homeViewModel;
    private MyCalendarView calendarView;
    private MyTaskView taskView;
    MainActivity mainActivity;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        calendarView=root.findViewById(R.id.myCalendarView);
        taskView=root.findViewById(R.id.MyTaskView);
        Day today=MyCalendar.getToday();
        init(today.getYear(),today.getMonth(),today.getDay());
        mainActivity.year=today.getYear();
        mainActivity.month=today.getMonth();
        mainActivity.day=today.getDay();
        return root;
    }
    public void init(int y,int m,int d)
    {
        mainActivity=(MainActivity)(getContext());
        calendarView.init(y,m,d);
        taskView.init(y,m,d);
        calendarView.setOnDayChangedListener(new MyCalendarView.OnDayChangedListener() {
            @Override
            public void onDayChange(int y, int m, int d) {
                taskView.setOPFLAG(JUMP);
                taskView.setCurrentDay(y,m,d);
                taskView.getTaskInfos();
                mainActivity.year=y;
                mainActivity.month=m;
                mainActivity.day=d;
            }
        });
        taskView.setOnNextDayListener(new MyTaskView.OnNextDayListener() {
            @Override
            public void OnNextDay() {
                calendarView.ToNextDay();
                Day day=calendarView.getCurrentDay();
                mainActivity.year=day.getYear();
                mainActivity.month=day.getMonth();
                mainActivity.day=day.getDay();
            }
        });
        taskView.setOnPreDayListener(new MyTaskView.OnPreDayListener() {
            @Override
            public void OnPreDay() {
                calendarView.ToPreDay();
                Day day=calendarView.getCurrentDay();
                mainActivity.year=day.getYear();
                mainActivity.month=day.getMonth();
                mainActivity.day=day.getDay();
            }
        });
        mainActivity.setOnSaveListener(new MainActivity.OnSaveListener() {
            @Override
            public void onSave() {
                taskView.setOPFLAG(FLUSH);
                taskView.getTaskInfos();
            }
        });
    }

}