package com.android.wifestudy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import me.zhouzhuo.zzhorizontalprogressbar.ZzHorizontalProgressBar;

public class MainActivity extends AppCompatActivity {
    private NotificationManager manager;
    private Notification notification;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_main);
        final ZzHorizontalProgressBar pb = (ZzHorizontalProgressBar) findViewById(R.id.zzHorizontalProgressBar);
        try {
            pb.setProgress(100-Integer.parseInt(getDataStr().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        textView = findViewById(R.id.gaokaotime);
        pb.setMax(100);
        try {
            textView.setText("距离高考还有"+Integer.parseInt(getDataStr().toString())+"天");
        } catch (ParseException e) {
            e.printStackTrace();
        }
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

}
