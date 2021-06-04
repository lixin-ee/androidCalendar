package com.example.myapp.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.IBinder;

import com.example.myapp.MainActivity;
import com.example.myapp.MyDatabase;
import com.example.myapp.entity.Schedule;
import com.example.myapp.entity.TSitem;
import com.example.myapp.entity.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MyTXService extends Service {
    private Long time;
   /* private StartBroadcastReceiver startBroadcastReceiver;
    private StopBroadcastReceiver stopBroadcastReceiver;
*/
    @Override
    public void onCreate() {
        super.onCreate();
/*        startBroadcastReceiver = new StartBroadcastReceiver();
        stopBroadcastReceiver = new StopBroadcastReceiver();
        IntentFilter startIntentfilter = new IntentFilter("com.example.myapp.MyTXService.Start");
        IntentFilter stopIntentfilter = new IntentFilter("com.example.myapp.MyTXService.Stop");
        registerReceiver(startBroadcastReceiver, startIntentfilter);
        registerReceiver(stopBroadcastReceiver, stopIntentfilter);*/

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    public void onStop() {

    }

   /* class StartBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            NetworkAsyncTask nk=new NetworkAsyncTask();
            NetOP netOP=new NetOP(intent.getIntExtra("type",0),context);
            nk.execute(netOP);
        }
    }*/

    /*class StopBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }*/

 /*   public static class NetworkAsyncTask extends AsyncTask<NetOP,Void,NetOP> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected NetOP doInBackground(NetOP... netOPS) {
            NetOP netOP=netOPS[0];
            MyDatabase myDatabase=MyDatabase.getInstance(netOP.context);
            switch (netOP.op)
            {
                case NetOP.GET:{
                    List<Schedule> schedules =new ArrayList<>();
                    List<Task> tasks =new ArrayList<>();
                    String schesjson="";
                    String tasksjson="";
                    //执行网络读取
                    try {
                        Socket socket=new Socket("127.0.0.1", 25545);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Gson gson=new Gson();
                    schedules=gson.fromJson(schesjson,new TypeToken<List<Schedule>>(){}.getType());
                    tasks=gson.fromJson(tasksjson,new TypeToken<List<Task>>(){}.getType());
                    myDatabase.scheduleDao().insertSchedules(schedules);
                    myDatabase.taskDao().insertTaskS(tasks);
                    break;
                }
                case NetOP.POST:{
                    List<Schedule> schedules=new ArrayList<>();
                    List<Task> tasks=new ArrayList<>();
                    schedules.addAll(myDatabase.scheduleDao().loadAll());
                    tasks.addAll(myDatabase.taskDao().loadAll());
                    Gson gson=new Gson();
                    String schesjson=gson.toJson(schedules,new TypeToken<List<Schedule>>(){}.getType());
                    String tasksjson=gson.toJson(tasks,new TypeToken<List<Schedule>>(){}.getType());
                    //执行网络传送

                    break;
                }
            }
            return netOP;
        }

        @Override
        protected void onPostExecute(NetOP netOP) {
            super.onPostExecute(netOP);
        }
    }
    public class NetOP
    {
      public static final int POST=1;
      public static final int GET=2;
      public int op;
      public  Context context;
     public NetOP(int op,Context con)
     {
         this.op=op;
         context=con;
     }
    }*/
}