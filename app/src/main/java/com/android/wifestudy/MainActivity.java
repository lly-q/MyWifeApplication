package com.android.wifestudy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.WindowManager;
import android.widget.Editor;
import android.widget.TextView;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import me.zhouzhuo.zzhorizontalprogressbar.ZzHorizontalProgressBar;

import static android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

public class MainActivity extends AppCompatActivity {
    private NotificationManager manager;
    private Notification notification;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //去除状态遮罩
        lightStatusBar();
        //设置进度条&设置提醒时间
        setGaoKaoProgressDayLeft();
        //创建高考倒计时通知
        notificationGaoKao();
        //添加待办
        addToDoFragment();

    }
    private void setGaoKaoProgressDayLeft() {
        final ZzHorizontalProgressBar pb = (ZzHorizontalProgressBar) findViewById(R.id.zzHorizontalProgressBar);
        pb.setMax(100);
        try {
            pb.setProgress(100-Integer.parseInt(getDataStr().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        textView = findViewById(R.id.gaokaotime);
        try {
            textView.setText("距离高考还有"+Integer.parseInt(getDataStr().toString())+"天");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void addToDoFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ToDoFragment fragment =new ToDoFragment();
        fragmentTransaction.add(R.id.ToDofragment,fragment).commit();

        //添加数据
        saveMessage(this,"整理数学错题",2);
    }

    private void notificationGaoKao() {
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel = new NotificationChannel("gaokao","高考时间",
                NotificationManager.IMPORTANCE_HIGH);
        manager.createNotificationChannel(notificationChannel);
        try {
            notification = new NotificationCompat.Builder(this,"gaokao")
                    .setContentTitle("高考时间倒计时")
                    .setSmallIcon(R.drawable.ic_android_black_24dp)
                    .setContentText("距离高考还有"+getDataStr()+"天")
                    .build();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        manager.notify(1,notification);
    }

    private void lightStatusBar() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN| SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    //获取差值
    public static String getDataStr() throws ParseException {
        long diff;
        long nd = 1000 * 24 * 60 * 60;
        Date nowDate = new Date(System.currentTimeMillis());
        Date nowDate1 = new Date(stringToLong("2021-06-07","yyyy-MM-dd"));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateOk = simpleDateFormat.format(nowDate);
        String dategaokao = simpleDateFormat.format(nowDate1);
        diff = simpleDateFormat.parse(dategaokao).getTime()
                - simpleDateFormat.parse(dateOk).getTime();
        String day = String.valueOf(diff / nd);
        return day;

    }
    //转换数值
    public static long stringToLong(String strTime, String formatType)
            throws ParseException {
        Date date = stringToDate(strTime, formatType); // String类型转成date类型
        if (date == null) {
            return 0;
        } else {
            long currentTime = dateToLong(date); // date类型转成long类型
            return currentTime;
        }
    }
    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }
    public static long dateToLong(Date date) {
        return date.getTime();
    }

    public static void saveMessage(Context context, String title, int interval){
        SharedPreferences sp = context.getSharedPreferences("sp",context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("title",title);
        editor.putInt("interval",interval);
        editor.commit();
    }
}

