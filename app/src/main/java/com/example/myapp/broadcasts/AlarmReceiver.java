package com.example.myapp.broadcasts;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.NotificationCompat;

import com.example.myapp.R;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "闹钟响了", Toast.LENGTH_SHORT).show();
        Resources res=context.getResources();
        Bitmap bmp=BitmapFactory.decodeResource(res,R.drawable.ic_home_black_24dp);
        if(bmp==null) Log.d("bmp","null");
        NotificationManager manager_notifica=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        //定义notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("AppTestNotificationId", "AppTestNotificationName", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("提醒");
            notificationChannel.setShowBadge(false);
            manager_notifica.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle("日程通知")
                .setSmallIcon(R.drawable.ic_dashboard_black_24dp)
                .setLargeIcon(bmp)
                .setContentText(intent.getStringExtra("content"))
                .setTicker(intent.getStringExtra("content"))
                // 单击 面板自动取消通知
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND);// 设置通知响应方式
               // .setContentIntent(pi);
               //.setVibrate(new long[0,500,100,500]);
        Notification notification=builder.build();
        manager_notifica.notify(1, notification);// 通过管理器发送通知（通知的id，Notification对象）
    }

}
