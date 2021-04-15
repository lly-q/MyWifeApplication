package com.android.wifestudy;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ToDoFragment extends Fragment {
    @Nullable
    private TextView title;
    private TextView lasttime;
    private TextView nexttime;
    private TextView dayleft;
    private View view;
    public List<Map<String, String>> mapList;
    private String TAG = "1";
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //设置
        view = inflater.inflate(R.layout.fragment_test, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Context context = getActivity();
        title = view.findViewById(R.id.title);
        String Pri_title = (String) readMessage(context).get(0);
        int interval = (Integer) readMessage(context).get(1);
        title.setText(Pri_title);
        lasttime = view.findViewById(R.id.lasttime);
        lasttime.setText("上次签到时间"+getLastTime(interval));
        nexttime = view.findViewById(R.id.nexttime);
        nexttime.setText("下次签到时间"+getNextTime(interval));
        try {
            String daystr = getDataStr(getNextTime(interval));
            dayleft = view.findViewById(R.id.dayleft);
            dayleft.setText(daystr+"天");
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public static ArrayList readMessage(Context context){
        ArrayList mapList = new ArrayList<>();
        SharedPreferences sp = context.getSharedPreferences("sp", Context.MODE_PRIVATE);
        String title = sp.getString("title",null);
        int interval = sp.getInt("interval",1);
        mapList.add(title);
        mapList.add(interval);
        return mapList;
    }
    private String getLastTime(int interval) {
        //DateFormat dateInstance = SimpleDateFormat.getDateTimeInstance();//系统预置
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        Calendar change = Calendar.getInstance();
        change.setTime(date);
        change.add(Calendar.DAY_OF_MONTH,-interval);
        Date update = change.getTime();
        String s = simpleDateFormat.format(update);
        return s;
    }
    private String getNextTime(int interval) {
        //DateFormat dateInstance = SimpleDateFormat.getDateTimeInstance();//系统预置
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        Calendar change = Calendar.getInstance();
        change.setTime(date);
        change.add(Calendar.DAY_OF_MONTH,interval);
        Date update = change.getTime();
        String s = simpleDateFormat.format(update);
        return s;
    }

    //获取差值
    public static String getDataStr(String string) throws ParseException {
        long diff;
        long nd = 1000 * 24 * 60 * 60;
        Date nowDate = new Date(System.currentTimeMillis());
        Date nowDate1 = new Date(stringToLong(string,"yyyy-MM-dd"));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateOk = simpleDateFormat.format(nowDate);
        String stringNextTime = simpleDateFormat.format(nowDate1);
        diff = simpleDateFormat.parse(stringNextTime).getTime()
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

}
