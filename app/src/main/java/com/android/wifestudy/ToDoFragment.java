package com.android.wifestudy;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.wifestudy.Adapter.MyAdapter;
import com.android.wifestudy.Data.DatabaseHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ToDoFragment extends Fragment {
    @Nullable
    private TextView title;
    private TextView lasttime;
    private TextView nexttime;
    private TextView dayleft;
    private int Interval = 0;
    private String intervaltext;
    ArrayList<String> mtitle;
    private String[] titleGroup;
    private Integer[] intervalGroup;
    private String[] dayLeftGroup;
    private View view;
    private int itemNum;
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private ArrayList titlelists = new ArrayList();
    private ArrayList intervallists = new ArrayList();
    private Context mContext;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //设置
        view = inflater.inflate(R.layout.fragment_test, container, false);
        initList();
        return view;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        HandletMessage();
        initrecycleview();
    }

    private void HandletMessage() {
        //获取上下文
        Context context = getActivity();
        DatabaseHelper dbhelper = new DatabaseHelper(context, "ToDo_db", null, 1);
        SQLiteDatabase db = dbhelper.getWritableDatabase();

        //非首次运行咱们就是说坚决不对数据库初始写入了,家人们.
        if (CheckStr()) {
            //导入原始数据
            String SQLI = "INSERT INTO content VALUES ('整理数学笔记','2'),('背英语','1')";
            db.execSQL(SQLI);
        } else {
            //Toast.makeText(context, "非第一次运行，不对数据库进行操作", Toast.LENGTH_SHORT).show();
        }
        //使用游标
        Cursor cursor = db.query("content", new String[]{"title_data,interval"}, null, null, null, null, null);
        //游标历遍数据库使用String[]存放sql数据
        int sum = 0;
        //cursor.getCount()；获取总行数
        this.itemNum = cursor.getCount();
        this.titleGroup = new String[this.itemNum];
        this.intervalGroup = new Integer[this.itemNum];
        while (cursor.moveToNext()) {
            String T = cursor.getString(cursor.getColumnIndex("title_data"));
            int I = Integer.parseInt(cursor.getString(cursor.getColumnIndex("interval")));
            //Interval = Integer.parseInt(I);
            this.titleGroup[sum]=T;
            this.intervalGroup[sum]=I;
            sum++;
        }
        //方法末端得到两个String字符串集合

        //获取经过计算的剩余时间

    }

    private void initrecycleview() {
        this.mContext = requireContext();
        this.recyclerView= view.findViewById(R.id.recycleview);
        this.dayLeftGroup =new String[this.itemNum];
        this.recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));//线性显示

        for (int i = 0; i < this.itemNum; i++) {

            titlelists.add(this.titleGroup[i]);
            try {
                this.dayLeftGroup[i] = getDataStr(getNextTime(intervalGroup[i]).toString());
                intervallists.add(this.dayLeftGroup[i]);
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }
        this.myAdapter = new MyAdapter(this.titlelists,this.intervallists);
        this.recyclerView.setAdapter(this.myAdapter);
    }

    //检查是否为空
    private boolean CheckStr() {
        //获取上下文
        Context context = getActivity();
        DatabaseHelper dbhelper = new DatabaseHelper(context, "ToDo_db", null, 1);
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Cursor cursor = db.query("content", new String[]{"title_data"}, null, null, null, null, null);
        String titlestr = null;
        if (cursor.moveToFirst()) {
            titlestr = cursor.getString(cursor.getColumnIndex("title_data"));
        }
        if (TextUtils.isEmpty(titlestr)) {
            return true;
        } else {
            return false;
        }

    }



    private void initView() {
        //获取上下文
        Context context = getActivity();
        this.dayleft = view.findViewById(R.id.dayleft);
        //未解决
        title = view.findViewById(R.id.title);
        lasttime = view.findViewById(R.id.lasttime);
        //lasttime.setText("上次签到时间"+getLastTime(interval));
        nexttime = view.findViewById(R.id.nexttime);
        //nexttime.setText("下次签到时间"+getNextTime(interval));

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
        String day = String.valueOf(diff / nd + "天");
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
    //初始化List接口
    private void initList() {
        mtitle = new ArrayList<String>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean contains(@Nullable Object o) {
                return false;
            }

            @NonNull
            @Override
            public Iterator<String> iterator() {
                return null;
            }

            @NonNull
            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @NonNull
            @Override
            public <T> T[] toArray(@NonNull T[] a) {
                return null;
            }

            @Override
            public boolean add(String s) {
                return false;
            }

            @Override
            public boolean remove(@Nullable Object o) {
                return false;
            }

            @Override
            public boolean containsAll(@NonNull Collection<?> c) {
                return false;
            }

            @Override
            public boolean addAll(@NonNull Collection<? extends String> c) {
                return false;
            }

            @Override
            public boolean addAll(int index, @NonNull Collection<? extends String> c) {
                return false;
            }

            @Override
            public boolean removeAll(@NonNull Collection<?> c) {
                return false;
            }

            @Override
            public boolean retainAll(@NonNull Collection<?> c) {
                return false;
            }

            @Override
            public void clear() {

            }

            @Override
            public String get(int index) {
                return null;
            }

            @Override
            public String set(int index, String element) {
                return null;
            }

            @Override
            public void add(int index, String element) {

            }

            @Override
            public String remove(int index) {
                return null;
            }

            @Override
            public int indexOf(@Nullable Object o) {
                return 0;
            }

            @Override
            public int lastIndexOf(@Nullable Object o) {
                return 0;
            }

            @NonNull
            @Override
            public ListIterator<String> listIterator() {
                return null;
            }

            @NonNull
            @Override
            public ListIterator<String> listIterator(int index) {
                return null;
            }

            @NonNull
            @Override
            public List<String> subList(int fromIndex, int toIndex) {
                return null;
            }
        };
    }

}
