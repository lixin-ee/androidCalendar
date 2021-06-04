package com.example.myapp;


import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.util.GregorianCalendar;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.DragAndDropPermissions;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.Toast;

import com.example.myapp.broadcasts.AlarmReceiver;
import com.example.myapp.entity.Schedule;
import com.example.myapp.entity.TSitem;
import com.example.myapp.entity.Task;
import com.example.myapp.ui.add.AddFragment;
import com.example.myapp.ui.dashboard.DashboardFragment;
import com.example.myapp.ui.home.HomeFragment;
import com.example.myapp.ui.notifications.NotificationsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavGraphNavigator;
import androidx.navigation.Navigation;
import androidx.navigation.NavigatorProvider;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static com.example.myapp.entity.TSitem.*;


public class MainActivity extends AppCompatActivity {
    private AddFragment addFragment;
    private FragmentManager fragmentManager;
    private Bundle args;
    private Fragment navfra;
    private FloatingActionButton addButton;
    public int year;
    public int month;
    public int day;
    public static final int TXNUM=4;
    @Override
    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addButton=findViewById(R.id.add);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        args=new Bundle();
        fragmentManager=getSupportFragmentManager();
        navfra=fragmentManager.findFragmentById(R.id.nav_host_fragment);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigatorProvider provider=navController.getNavigatorProvider();
        FixFragmentNavigator fixFragmentNavigator=new FixFragmentNavigator(this,navfra.getChildFragmentManager(),navfra.getId());
        provider.addNavigator(fixFragmentNavigator);
        NavGraph navDestinations = initNavGraph(provider, fixFragmentNavigator);
        navController.setGraph(navDestinations);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications,R.id.settingsFragment)
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        addFragment=AddFragment.getInstance(args);
        addFragment.setOnSavelistener(new AddFragment.OnSavelistener() {
            @Override
            public void onSave(TSitem tSitem,int optype) {
                Log.d("tsitem",tSitem.getTime1());
                DataOP dataOP=new DataOPByTSitem(optype,tSitem);
                DataQueryAsyncTask dataQueryAsyncTask=new DataQueryAsyncTask();
                dataQueryAsyncTask.execute(dataOP);
                onSaveListener.onSave();
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                args.putInt("timey",year);
                args.putInt("timemo",month);
                args.putInt("timed",day);
                GregorianCalendar calendar=new GregorianCalendar();
                int hour=calendar.get(Calendar.HOUR_OF_DAY);
                int minute=calendar.get(Calendar.MINUTE);
                args.putInt("timeh",hour);
                args.putInt("timem",minute);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                if(!addFragment.isAdded()) {
                    Log.d("add","false");
                    transaction.add(R.id.container,addFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                transaction.show(addFragment);
                getSupportActionBar().hide();
            }
        });
    }

    @Override
    public void onBackPressed() {
        getSupportActionBar().show();
        fragmentManager.beginTransaction().remove(addFragment).commit();
    }

    private NavGraph initNavGraph(NavigatorProvider provider, FixFragmentNavigator fragmentNavigator) {
        NavGraph navGraph = new NavGraph(new NavGraphNavigator(provider));

        //用自定义的导航器来创建目的地
        FragmentNavigator.Destination destination1 = fragmentNavigator.createDestination();
        destination1.setId(R.id.navigation_home);
        destination1.setLabel(getResources().getString(R.string.title_home));
        destination1.setClassName(HomeFragment.class.getCanonicalName());
        navGraph.addDestination(destination1);


        FragmentNavigator.Destination destination2 = fragmentNavigator.createDestination();
        destination2.setId(R.id.navigation_dashboard);
        destination2.setLabel(getResources().getString(R.string.title_dashboard));
        destination2.setClassName(DashboardFragment.class.getCanonicalName());
        navGraph.addDestination(destination2);

        FragmentNavigator.Destination destination3 = fragmentNavigator.createDestination();
        destination3.setId(R.id.navigation_notifications);
        destination3.setLabel(getResources().getString(R.string.title_notifications));
        destination3.setClassName(NotificationsFragment.class.getCanonicalName());
        navGraph.addDestination(destination3);

        FragmentNavigator.Destination destination4 = fragmentNavigator.createDestination();
        destination4.setId(R.id.settingsFragment);
        destination4.setLabel(getResources().getString(R.string.title_setting));
        destination4.setClassName(SettingsFragment.class.getCanonicalName());
        navGraph.addDestination(destination4);

        navGraph.setStartDestination(destination1.getId());

        return navGraph;
    }
    public void updateitem(int syear,int smonth,int sday,int shour,int smin,String sdes,int tixing,int id)
    {
         args.putInt("type", TASK);
         args.putInt("syear",syear);
         args.putInt("smonth",smonth);
         args.putInt("sday",sday);
         args.putInt("shour",shour);
         args.putInt("smin",smin);
         args.putString("tsdes",sdes);
         args.putInt("sttx",tixing);
         args.putInt("stid",id);
         addButton.performClick();
    }
    public void updateitem(int syear,int smonth,int sday,int shour,int smin,int syear2,int smonth2,int sday2,int shour2,int smin2,String sdes,int tixing,int id)
    {
        args.putInt("type",TSitem.SCHEDULE);
        args.putInt("syear1",syear);
        args.putInt("smonth1",smonth);
        args.putInt("sday1",sday);
        args.putInt("shour1",shour);
        args.putInt("smin1",smin);
        args.putInt("syear2",syear2);
        args.putInt("smonth2",smonth2);
        args.putInt("sday2",sday2);
        args.putInt("shour2",shour2);
        args.putInt("smin2",smin2);
        args.putString("ssdes",sdes);
        args.putInt("sstx",tixing);
        args.putInt("ssid",id);
        addButton.performClick();
    }
    public interface OnSaveListener{
        void onSave();
    }
    private OnSaveListener onSaveListener;

    public void setOnSaveListener(OnSaveListener onSaveListener) {
        this.onSaveListener = onSaveListener;
    }

    public static class DataQueryAsyncTask extends AsyncTask<DataOP,Void,DataOP> {
        @Override
        protected DataOP doInBackground(DataOP... dataOPS) {
               DataOP dataOP=dataOPS[0];
                MyDatabase myDatabase=MyDatabase.getInstance(dataOP.mainActivity);
               switch (dataOP.op)
               {
                   case DataOP.INSERT:{
                       DataOPByTSitem dataOPByTSitem=(DataOPByTSitem)dataOP;
                       switch (dataOPByTSitem.tSitem.getTStype())
                       {
                           case TASK:{
                               long id=myDatabase.taskDao().insertTaskre((Task)dataOPByTSitem.tSitem);

                               try {
                                   int numtixing=dataOPByTSitem.tSitem.getTxing();
                                   for(int i=1;i<=TXNUM;i++)
                                   {
                                      int numi= (int) ((int)numtixing/(Math.pow(10,(TXNUM-i))));
                                      numtixing = (int) ((int)numtixing%(Math.pow(10,(TXNUM-i))));
                                      if(numi==1){
                                          Intent intent=new Intent(dataOP.mainActivity, AlarmReceiver.class);
                                          intent.putExtra("content",dataOPByTSitem.tSitem.getDescription());
                                          PendingIntent pi=PendingIntent.getBroadcast
                                                  (dataOP.mainActivity,productID(i,(int)id,
                                                         dataOPByTSitem.tSitem.getTStype()),intent,PendingIntent.FLAG_UPDATE_CURRENT);
                                          Calendar calendar=TimeStrToCalendar(dataOPByTSitem.tSitem.getTime1());
                                          calendar.add(Calendar.MINUTE,0-AddFragment.TXS[i-1]);
                                          setAlarm(pi,calendar,dataOP.mainActivity);
                                      }
                                   }
                               } catch (ParseException e) {
                                   e.printStackTrace();
                               }

                               break;
                           }
                           case SCHEDULE:{
                               long id= myDatabase.scheduleDao().insertSchedulere((Schedule)dataOPByTSitem.tSitem);
                               Log.d("id",""+id);
                               try {
                                   int numtixing=dataOPByTSitem.tSitem.getTxing();
                                   for(int i=1;i<=TXNUM;i++)
                                   {
                                       int numi= (int) ((int)numtixing/(Math.pow(10,(TXNUM-i))));
                                       numtixing = (int) ((int)numtixing%(Math.pow(10,(TXNUM-i))));
                                       if(numi==1){
                                           Intent intent=new Intent(dataOP.mainActivity, AlarmReceiver.class);
                                           intent.putExtra("content",dataOPByTSitem.tSitem.getDescription());
                                           PendingIntent pi=PendingIntent.getBroadcast
                                                   (dataOP.mainActivity,productID(i,(int)id,
                                                           dataOPByTSitem.tSitem.getTStype()),intent,PendingIntent.FLAG_UPDATE_CURRENT);
                                           Calendar calendar=TimeStrToCalendar(dataOPByTSitem.tSitem.getTime2());
                                           calendar.add(Calendar.MINUTE,0-AddFragment.TXS[i-1]);
                                           Log.d("time",""+calendar.get(Calendar.HOUR_OF_DAY)+calendar.get(Calendar.MINUTE));
                                           setAlarm(pi,calendar,dataOP.mainActivity);
                                       }
                                   }
                               } catch (ParseException e) {
                                   e.printStackTrace();
                               }
                               dataOP.mainActivity.onSaveListener.onSave();
                               break;
                           }
                       }
                       break;
                   }
                   case DataOP.UPDATE:{
                       DataOPByTSitem dataOPByTSitem=(DataOPByTSitem)dataOP;
                       switch (dataOPByTSitem.tSitem.getTStype())
                       {
                           case TASK:{
                                  /*Task oldinsert=myDatabase.taskDao().findTaskById(dataOPByTSitem.tSitem.getId());
                                   int numtixing=oldinsert.getTxing();
                                   for(int i=1;i<=TXNUM;i++)
                                   {
                                       int numi= (int) ((int)numtixing/(Math.pow(10,(TXNUM-i))));
                                       numtixing = (int) ((int)numtixing%(Math.pow(10,(TXNUM-i))));
                                       if(numi==1){
                                           Intent intent=new Intent(dataOP.mainActivity, AlarmReceiver.class);
                                           PendingIntent pi=PendingIntent.getBroadcast
                                                   (dataOP.mainActivity,productID(i,oldinsert.getId(),
                                                           oldinsert.getTStype()),intent,PendingIntent.FLAG_UPDATE_CURRENT);
                                           cancleAlarm(pi,dataOP.mainActivity);
                                       }
                                   }*/
                               try {
                                   int newnumtixing=dataOPByTSitem.tSitem.getTxing();
                                   for(int i=1;i<=TXNUM;i++)
                                   {
                                       int numi= (int) ((int)newnumtixing/(Math.pow(10,(TXNUM-i))));
                                       newnumtixing = (int) ((int)newnumtixing%(Math.pow(10,(TXNUM-i))));
                                       if(numi==1){
                                           Intent intent=new Intent(dataOP.mainActivity, AlarmReceiver.class);
                                           intent.putExtra("content",dataOPByTSitem.tSitem.getDescription());
                                           PendingIntent pi=PendingIntent.getBroadcast
                                                   (dataOP.mainActivity,productID(i,dataOPByTSitem.tSitem.getId(),
                                                          dataOPByTSitem.tSitem.getTStype()),intent,PendingIntent.FLAG_CANCEL_CURRENT);
                                           Calendar calendar=TimeStrToCalendar(dataOPByTSitem.tSitem.getTime1());
                                           calendar.add(Calendar.MINUTE,0-AddFragment.TXS[i-1]);
                                           setAlarm(pi,calendar,dataOP.mainActivity);
                                       }
                                   }
                               } catch (ParseException e) {
                                   e.printStackTrace();
                               }
                               myDatabase.taskDao().updateTask((Task)dataOPByTSitem.tSitem);
                               break;
                           }
                           case SCHEDULE:{
                              /* Schedule oldinsert=myDatabase.scheduleDao().findScheById(dataOPByTSitem.tSitem.getId());
                               int numtixing=oldinsert.getTxing();
                               for(int i=1;i<=TXNUM;i++)
                               {
                                   int numi= (int) ((int)numtixing/(Math.pow(10,(TXNUM-i))));
                                   numtixing = (int) ((int)numtixing%(Math.pow(10,(TXNUM-i))));
                                   if(numi==1){
                                       Intent intent=new Intent(dataOP.mainActivity, AlarmReceiver.class);
                                       PendingIntent pi=PendingIntent.getBroadcast
                                               (dataOP.mainActivity,productID(i,oldinsert.getId(),
                                                       oldinsert.getTStype()),intent,PendingIntent.FLAG_UPDATE_CURRENT);
                                       cancleAlarm(pi,dataOP.mainActivity);
                                   }
                               }*/
                               try {
                                   int newnumtixing=dataOPByTSitem.tSitem.getTxing();
                                   for(int i=1;i<=TXNUM;i++)
                                   {
                                       int numi= (int) ((int)newnumtixing/(Math.pow(10,(TXNUM-i))));
                                       newnumtixing = (int) ((int)newnumtixing%(Math.pow(10,(TXNUM-i))));
                                       if(numi==1){
                                           Intent intent=new Intent(dataOP.mainActivity, AlarmReceiver.class);
                                           intent.putExtra("content",dataOPByTSitem.tSitem.getDescription());
                                           PendingIntent pi=PendingIntent.getBroadcast
                                                   (dataOP.mainActivity,productID(i,dataOPByTSitem.tSitem.getId(),
                                                           dataOPByTSitem.tSitem.getTStype()),intent,PendingIntent.FLAG_CANCEL_CURRENT);
                                           Calendar calendar=TimeStrToCalendar(dataOPByTSitem.tSitem.getTime2());
                                           calendar.add(Calendar.MINUTE,0-AddFragment.TXS[i-1]);
                                           setAlarm(pi,calendar,dataOP.mainActivity);
                                       }
                                   }
                               } catch (ParseException e) {
                                   e.printStackTrace();
                               }
                               myDatabase.scheduleDao().updateSchedule((Schedule)dataOPByTSitem.tSitem);
                               break;
                           }
                       }
                       break;
                   }
                   case DataOP.DELETE: {
                       DataOPByID dataOPByID=(DataOPByID)dataOP;
                       switch (dataOPByID.TStype)
                       {
                           case TASK:{
                               Task deleteitem=myDatabase.taskDao().findTaskById(dataOPByID.Id);
                               int numtixing=deleteitem.getTxing();
                               for(int i=1;i<=TXNUM;i++)
                               {
                                   int numi= (int) ((int)numtixing/(Math.pow(10,(TXNUM-i))));
                                   numtixing = (int) ((int)numtixing%(Math.pow(10,(TXNUM-i))));
                                   if(numi==1){
                                       Intent intent=new Intent(dataOP.mainActivity, AlarmReceiver.class);
                                       PendingIntent pi=PendingIntent.getBroadcast
                                               (dataOP.mainActivity,productID(i,deleteitem.getId(),
                                                       deleteitem.getTStype()),intent,PendingIntent.FLAG_UPDATE_CURRENT);
                                       cancleAlarm(pi,dataOP.mainActivity);
                                   }
                               }
                               myDatabase.taskDao().deleteTask(deleteitem);

                               break;
                           }
                           case SCHEDULE:{
                               Schedule deleteitem=myDatabase.scheduleDao().findScheById(dataOPByID.Id);
                               int numtixing=deleteitem.getTxing();
                               for(int i=1;i<=TXNUM;i++)
                               {
                                   int numi= (int) ((int)numtixing/(Math.pow(10,(TXNUM-i))));
                                   numtixing = (int) ((int)numtixing%(Math.pow(10,(TXNUM-i))));
                                   if(numi==1){
                                       Intent intent=new Intent(dataOP.mainActivity, AlarmReceiver.class);
                                       PendingIntent pi=PendingIntent.getBroadcast
                                               (dataOP.mainActivity,productID(i,deleteitem.getId(),
                                                       deleteitem.getTStype()),intent,PendingIntent.FLAG_UPDATE_CURRENT);
                                       cancleAlarm(pi,dataOP.mainActivity);
                                   }
                               }
                               myDatabase.scheduleDao().deleteSchedule(deleteitem);
                               break;
                           }
                       }
                       break;
                   }
                   case DataOP.QUERYBYDATE:{
                       DataOPByDateString dataOPByDateString=(DataOPByDateString) dataOP;
                       String DateStr=dataOPByDateString.DateString;
                      List<Task> Tasks=myDatabase.taskDao().findTaskbyDate(DateStr);
                       List<Schedule> schedules=myDatabase.scheduleDao().findScheByDate(DateStr);
                        dataOPByDateString.tSitems.addAll(Tasks);
                        dataOPByDateString.tSitems.addAll(schedules);
                        break;
                   }
               }
            return dataOPS[0];
        }

        @Override
        protected void onPreExecute() {
         super.onPreExecute();
        }

        @Override
        protected void onPostExecute(DataOP dataOP) {
            switch (dataOP.op)
            {
                case DataOP.DELETE:{
                    Toast.makeText(dataOP.mainActivity,"删除成功",Toast.LENGTH_LONG).show();
                    break;
                }
                case DataOP.INSERT:{
                    Toast.makeText(dataOP.mainActivity,"添加成功",Toast.LENGTH_LONG).show();
                    break;
                }
                case DataOP.QUERYBYDATE:{
                    DataOPByDateString dataOPByDateString=(DataOPByDateString) dataOP;
                   dataOP.mainActivity.onQueryByDateFinishListener.onQueryByDateFinish(dataOPByDateString.tSitems);
                   break;
                }
                case DataOP.UPDATE:{
                    Toast.makeText(dataOP.mainActivity,"修改成功",Toast.LENGTH_LONG).show();
                    dataOP.mainActivity.onSaveListener.onSave();
                    break;
                }
            }

        }
    }
    public abstract class DataOP{
        public static final int DELETE=2;
        public static final int QUERYBYID=3;
        public static final int QUERYBYDATE=4;
        public static final int UPDATE=0;
        public static final int INSERT=1;
        int op;
        public MainActivity mainActivity=MainActivity.this;
        public DataOP(int OP) {
        op=OP;
        }

    }
    public class DataOPByID extends DataOP{
        int Id;
        int TStype;
        public DataOPByID(int op,int id,int tstype){
            super(op);
            Id=id;
            TStype=tstype;
        }
    }
    public class DataOPByDateString extends DataOP{
        String DateString;
        List<TSitem> tSitems;
        public DataOPByDateString(int op,String string) {
            super(op);
            DateString=string;
            tSitems=new ArrayList<>();
        }
    }
    public class DataOPByTSitem extends DataOP{
        TSitem tSitem;
        public DataOPByTSitem(int op,TSitem tsitem)
        {
            super(op);
            tSitem=tsitem;
        }
    }
    public interface OnQueryByDateFinishListener{
        void onQueryByDateFinish(List<TSitem> tSitems);
    }
    private  OnQueryByDateFinishListener onQueryByDateFinishListener;

    public void setOnQueryByDateFinishListener(OnQueryByDateFinishListener onQueryByDateFinishListener) {
            this.onQueryByDateFinishListener = onQueryByDateFinishListener;
    }
    public static Calendar TimeStrToCalendar(String timestr) throws ParseException {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        java.util.GregorianCalendar calendar=new java.util.GregorianCalendar();
        calendar.setTime(sdf.parse(timestr));
        return calendar;
    }
    public static void setAlarm(PendingIntent pi, Calendar calendar, Context context)
    {
        AlarmManager am=(AlarmManager)context.getSystemService(ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
    }
    public static void cancleAlarm(PendingIntent pi,Context context)
    {
        AlarmManager am=(AlarmManager)context.getSystemService(ALARM_SERVICE);
        am.cancel(pi);
    }
    public static int productID(int i,int id,int type)
    {
        String str=""+i+id+type;
       return Integer.parseInt(str);
    }
}