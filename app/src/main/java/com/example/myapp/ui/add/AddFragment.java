package com.example.myapp.ui.add;

import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import androidx.gridlayout.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapp.Dao.ScheduleDao;
import com.example.myapp.MainActivity;
import com.example.myapp.R;
import com.example.myapp.entity.*;


import static com.example.myapp.MainActivity.DataOP.*;
import static com.example.myapp.entity.TSitem.SCHEDULE;
import static com.example.myapp.entity.TSitem.TASK;

public class AddFragment extends Fragment {
    private static AddFragment addFragment;
    private MyDatePicker myDatePicker1;
    private MyDatePicker myDatePicker2;
    private ImageButton ok;
    private ImageButton cancle;
    private RadioGroup typeRadioGroup;
    private TextView timetext1;
    private TextView timetext2;
    private EditText destext;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private GridLayout tixing;
    private  int flag;
    public static final int[] TXS={5,10,20,30};

    @Override
    public void onDestroy() {
        Log.d("des","destroy");
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        Log.d("ondesvie","ondesview");
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        Log.d("onres","onres");
        super.onResume();
        init();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("oncv","oncreateview");
        View root=inflater.inflate(R.layout.add_fragment,container,false);
        myDatePicker1=root.findViewById(R.id.datepick1);
        myDatePicker2=root.findViewById(R.id.datepick2);
        myDatePicker1.setOnTimeChangedListener(new MyDatePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(int y, int mo, int d, int h, int m) {

            }
        });
        myDatePicker2.setOnTimeChangedListener(new MyDatePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(int y, int mo, int d, int h, int m) {

            }
        });
        tixing=root.findViewById(R.id.gridLayout);
        ok=root.findViewById(R.id.OK);
        cancle=root.findViewById(R.id.cancle);
        timetext1=root.findViewById(R.id.timetext1);
        timetext2=root.findViewById(R.id.timetext2);
        destext=root.findViewById(R.id.title_edit);
        radioButton1=root.findViewById(R.id.typeRadio1);
        radioButton2=root.findViewById(R.id.typeRadio2);
        typeRadioGroup=root.findViewById(R.id.typeRadioGroup);
        typeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)
                {
                    case R.id.typeRadio1:{
                        timetext2.setVisibility(View.VISIBLE);
                        myDatePicker2.setVisibility(View.VISIBLE);
                        timetext1.setText(getResources().getString(R.string.starttime));
                        timetext2.setText(getResources().getString(R.string.endtime));
                        break;
                    }
                    case R.id.typeRadio2:{
                        timetext1.setText(getResources().getString(R.string.stoptime));
                        timetext2.setVisibility(View.INVISIBLE);
                        myDatePicker2.setVisibility(View.INVISIBLE);
                        break;
                    }
                }
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args=getArguments();
                int tixingnum=0;
                for(int i=1;i<=tixing.getChildCount();i++)
                {
                  CheckBox checkBox=(CheckBox) tixing.getChildAt(i-1);
                  if(checkBox.isChecked()) tixingnum+=Math.pow(10,tixing.getChildCount()-i);
                }
                Log.d("tixing",tixingnum+"");
                String des= destext.getText().toString();
                switch(args.getInt("type"))
                {
                    case 0:
                        { flag= INSERT;
                            switch(typeRadioGroup.getCheckedRadioButtonId())
                          {
                        case R.id.typeRadio1:{
                            String time1=myDatePicker1.getTimeString();
                            String time2=myDatePicker2.getTimeString();
                            Schedule schedule=new Schedule(time1,des,time2,tixingnum);
                            onSavelistener.onSave(schedule,flag);
                            break;
                           }
                        case R.id.typeRadio2:{
                             String time1=myDatePicker1.getTimeString();
                             Task task=new Task(time1,des,tixingnum);
                             onSavelistener.onSave(task,flag);
                            break;
                           }
                         }
                         break;
                      }
                    case TASK:
                    {
                        flag=UPDATE;
                        String time1=myDatePicker1.getTimeString();
                        Task task=new Task(time1,des,tixingnum,args.getInt("stid"));
                        onSavelistener.onSave(task,flag);
                        break;
                    }
                    case SCHEDULE:{
                        flag=UPDATE;
                        String time1=myDatePicker1.getTimeString();
                        String time2=myDatePicker2.getTimeString();
                        Schedule schedule=new Schedule(time1,des,time2,tixingnum,args.getInt("ssid"));
                        onSavelistener.onSave(schedule,flag);
                        break;
                    }
                }

                //调用返回键
               getArguments().putInt("type",0);
               MainActivity mainActivity=(MainActivity) getContext();
               mainActivity.onBackPressed();
               mainActivity.getSupportActionBar().show();
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //调用返回键
                getArguments().putInt("type",0);
                MainActivity mainActivity=(MainActivity) getContext();
                mainActivity.onBackPressed();
                mainActivity.getSupportActionBar().show();
            }
        });
        return root;
    }
    public static AddFragment getInstance(Bundle args)
    {
        if(addFragment==null) {
          addFragment = new AddFragment();
        }
        addFragment.setArguments(args);
       return addFragment;
    }
    public void init(){
        Bundle args=getArguments();
        int type=args.getInt("type");
        switch (type){
            case TASK:{
                radioButton2.setVisibility(View.VISIBLE);
                radioButton2.setChecked(true);
                radioButton1.setVisibility(View.INVISIBLE);
                myDatePicker1.setValue(args.getInt("syear"),args.getInt("smonth"),args.getInt("sday"),args.getInt("shour"),args.getInt("smin"));
                destext.setText(args.getString("tsdes"));
                int numtixing=args.getInt("sttx");
                for(int i=1;i<=tixing.getChildCount();i++)
                {
                   CheckBox checkBox= (CheckBox)tixing.getChildAt(i-1);
                   int numi= (int) ((int)numtixing/(Math.pow(10,(tixing.getChildCount()-i))));
                    numtixing = (int) ((int)numtixing%(Math.pow(10,(tixing.getChildCount()-i))));
                   checkBox.setChecked(numi==1);
                }
                break;
            }
            case SCHEDULE:{
                radioButton1.setVisibility(View.VISIBLE);
                radioButton1.setChecked(true);
                radioButton2.setVisibility(View.INVISIBLE);
                myDatePicker1.setValue(args.getInt("syear1"),args.getInt("smonth1"),args.getInt("sday1"),args.getInt("shour1"),args.getInt("smin1"));
                myDatePicker2.setValue(args.getInt("syear2"),args.getInt("smonth2"),args.getInt("sday2"),args.getInt("shour2"),args.getInt("smin2"));
                destext.setText(args.getString("ssdes"));
                int numtixing=args.getInt("sstx");
                for(int i=1;i<=tixing.getChildCount();i++)
                {
                    CheckBox checkBox= (CheckBox)tixing.getChildAt(i-1);
                    int numi= (int) ((int)numtixing/(Math.pow(10,(tixing.getChildCount()-i))));
                    numtixing = (int) ((int)numtixing%(Math.pow(10,(tixing.getChildCount()-i))));
                    checkBox.setChecked(numi==1);
                }
                break;
            }
            default:
            {
                Log.d("default","de");
                radioButton1.setChecked(true);
                destext.setText(getResources().getString(R.string.default_title));
                myDatePicker1.setValue(args.getInt("timey"),args.getInt("timemo"),args.getInt("timed"),args.getInt("timeh"),args.getInt("timem"));
                myDatePicker2.setValue(args.getInt("timey"),args.getInt("timemo"),args.getInt("timed"),args.getInt("timeh")+1,args.getInt("timem"));
                for(int i=1;i<=tixing.getChildCount();i++)
                {
                    CheckBox checkBox= (CheckBox)tixing.getChildAt(i-1);
                    checkBox.setChecked(i==1);
                }
                break;
            }
        }
    }
    public interface OnSavelistener{
        void onSave(TSitem tSitem,int optype);
    }
    private OnSavelistener onSavelistener;

    public void setOnSavelistener(OnSavelistener onSavelistener) {
        this.onSavelistener = onSavelistener;
    }

}
