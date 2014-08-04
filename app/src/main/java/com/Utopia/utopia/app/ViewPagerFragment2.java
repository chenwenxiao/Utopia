package com.Utopia.utopia.app;

import android.content.ContentResolver;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.ViewFlipper;

import com.Utopia.utopia.app.SQL.DataProviderMetaData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2014/5/21 0021.
 * 使用Fragment显示ViewPager中的主要内容
 */
public class ViewPagerFragment2 extends Fragment implements GestureDetector.OnGestureListener {
    public static final int KIND_SCHEDULE = DataProviderMetaData.DataTableMetaData.KIND_SCHEDULE;
    public static final int KIND_TIP = DataProviderMetaData.DataTableMetaData.KIND_TIP;
    public static final int KIND_ADVERTISE = DataProviderMetaData.DataTableMetaData.KIND_ADVERTISE;

    public static int Next[] = new int[] {1, 2, 0};
    public static int Prev[] = new int[] {2, 0, 1};


    private ViewFlipper mViewFlipper;
    private ScrollView Scroll[];
    private LinearLayout BothLayout[], TipLayout[], ScheduleLayout[];
    private ImageView imageView[];
    ContentResolver cr;
    List<Map<String, Object>> lSchedule, lTip, lAdvertise;
    double secondLength;
    int current;
    long currentTime;

    public ViewPagerFragment2() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout2,
                container, false);
        mViewFlipper = (ViewFlipper) getActivity().findViewById(R.id.page2ViewFlipper);
        Scroll = new ScrollView[]{
                (ScrollView) getActivity().findViewById(R.id.page2Scroll0),
                (ScrollView) getActivity().findViewById(R.id.page2Scroll1),
                (ScrollView) getActivity().findViewById(R.id.page2Scroll2)};
        BothLayout = new LinearLayout[]{
                (LinearLayout) getActivity().findViewById(R.id.page2Scroll0BothLayout),
                (LinearLayout) getActivity().findViewById(R.id.page2Scroll1BothLayout),
                (LinearLayout) getActivity().findViewById(R.id.page2Scroll2BothLayout)};
        TipLayout = new LinearLayout[]{
                (LinearLayout) getActivity().findViewById(R.id.page2Scroll0TipLayout),
                (LinearLayout) getActivity().findViewById(R.id.page2Scroll1TipLayout),
                (LinearLayout) getActivity().findViewById(R.id.page2Scroll2TipLayout)};
        ScheduleLayout = new LinearLayout[]{
                (LinearLayout) getActivity().findViewById(R.id.page2Scroll0ScheduleLayout),
                (LinearLayout) getActivity().findViewById(R.id.page2Scroll1ScheduleLayout),
                (LinearLayout) getActivity().findViewById(R.id.page2Scroll2ScheduleLayout)};
        imageView = new ImageView[]{
                (ImageView) getActivity().findViewById(R.id.page2Scroll0imageView),
                (ImageView) getActivity().findViewById(R.id.page2Scroll1imageView),
                (ImageView) getActivity().findViewById(R.id.page2Scroll2imageView),
        };
        ImageView TimeLine = ((ImageView) getActivity().findViewById(R.id.page2Scroll1TimeLine));
        secondLength = TimeLine.getHeight() / 86400.0;

        cr = getActivity().getContentResolver();

        long todayTime, tomorrowTime, yesterdayTime;

        currentTime = TimeUtil.getCurrentTime();
        yesterdayTime = TimeUtil.getYesterday(currentTime);
        todayTime = TimeUtil.getToday(currentTime);
        tomorrowTime = TimeUtil.getTomorrow(currentTime);

        current = 1;
        currentTime = todayTime;
        FromSQLToListView(yesterdayTime, Scroll[0], BothLayout[0], TipLayout[0], ScheduleLayout[0], imageView[0]);
        FromSQLToListView(todayTime, Scroll[1], BothLayout[1], TipLayout[1], ScheduleLayout[1], imageView[1]);
        FromSQLToListView(tomorrowTime, Scroll[2], BothLayout[2], TipLayout[2], ScheduleLayout[2], imageView[2]);

        return view;
    }

    void insertSchedule(Map<String, Object> map, LinearLayout ScheduleLayout) {

    }

    void insertAdvertise(Map<String, Object> map, ImageView imageView) {

    }

    void insertTip(Map<String, Object> map, LinearLayout TipLayout) {

    }


    void FromSQLToListView(long todayTime, ScrollView Scroll, LinearLayout BothLayout, LinearLayout TipLayout, LinearLayout ScheduleLayout, ImageView imageView) {

        double yesterdayTime = TimeUtil.getYesterday(todayTime);
        todayTime = TimeUtil.getToday(todayTime);
        double tomorrowTime = TimeUtil.getTomorrow(todayTime);
        Cursor cursor = cr.query(Uri.parse(""), new String[]{"create", "modified", "title", "value", "begin",
                "end", "finish", "kind"}, "kind = " + KIND_SCHEDULE +
                " AND " + "(begin < " + tomorrowTime +
                " AND " + "begin >= " + todayTime + ")", null, "begin asc");
        while (cursor.moveToNext()) {
            Map<String, Object> map = new HashMap<String, Object>();
            long create, modified, begin, end, finish, kind;
            String title, value;

            create = cursor.getLong(cursor.getColumnIndex("create"));
            modified = cursor.getLong(cursor.getColumnIndex("modified"));
            title = cursor.getString(cursor.getColumnIndex("title"));
            value = cursor.getString(cursor.getColumnIndex("value"));
            begin = cursor.getLong(cursor.getColumnIndex("begin"));
            end = cursor.getLong(cursor.getColumnIndex("end"));
            finish = cursor.getLong(cursor.getColumnIndex("finish"));
            kind = cursor.getLong(cursor.getColumnIndex("kind"));


            map.put("create", create);
            map.put("modified", modified);
            map.put("title", title);
            map.put("value", value);
            map.put("begin", begin);
            map.put("end", end);
            map.put("finish", finish);
            map.put("kind", kind);

            lSchedule.add(map);
            insertSchedule(map, ScheduleLayout);
        }

        cursor = cr.query(Uri.parse(""), new String[]{"create", "modified", "title", "value", "begin",
                "end", "finish", "kind"}, "kind = " + KIND_ADVERTISE +
                " AND " + "(begin < " + tomorrowTime +
                " AND " + "begin >= " + todayTime + ")", null, "begin asc");
        while (cursor.moveToNext()) {
            Map<String, Object> map = new HashMap<String, Object>();
            long create, modified, begin, end, finish, kind;
            String title, value;

            create = cursor.getLong(cursor.getColumnIndex("create"));
            modified = cursor.getLong(cursor.getColumnIndex("modified"));
            title = cursor.getString(cursor.getColumnIndex("title"));
            value = cursor.getString(cursor.getColumnIndex("value"));
            begin = cursor.getLong(cursor.getColumnIndex("begin"));
            end = cursor.getLong(cursor.getColumnIndex("end"));
            finish = cursor.getLong(cursor.getColumnIndex("finish"));
            kind = cursor.getLong(cursor.getColumnIndex("kind"));

            map.put("create", create);
            map.put("modified", modified);
            map.put("title", title);
            map.put("value", value);
            map.put("begin", begin);
            map.put("end", end);
            map.put("finish", finish);
            map.put("kind", kind);

            lAdvertise.add(map);
            insertAdvertise(map, imageView);
        }

        cursor = cr.query(Uri.parse(""), new String[]{"create", "modified", "title", "value", "begin",
                "end", "finish", "kind"}, "kind = " + KIND_TIP +
                " AND " + "(begin < " + tomorrowTime +
                " AND " + "begin >= " + todayTime + ")", null, "begin asc");
        while (cursor.moveToNext()) {
            Map<String, Object> map = new HashMap<String, Object>();
            long create, modified, begin, end, finish, kind;
            String title, value;

            create = cursor.getLong(cursor.getColumnIndex("create"));
            modified = cursor.getLong(cursor.getColumnIndex("modified"));
            title = cursor.getString(cursor.getColumnIndex("title"));
            value = cursor.getString(cursor.getColumnIndex("value"));
            begin = cursor.getLong(cursor.getColumnIndex("begin"));
            end = cursor.getLong(cursor.getColumnIndex("end"));
            finish = cursor.getLong(cursor.getColumnIndex("finish"));
            kind = cursor.getLong(cursor.getColumnIndex("kind"));

            map.put("create", create);
            map.put("modified", modified);
            map.put("title", title);
            map.put("value", value);
            map.put("begin", begin);
            map.put("end", end);
            map.put("finish", finish);
            map.put("kind", kind);

            lTip.add(map);
            insertTip(map, TipLayout);
        }




    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        //finger move to Up
        if (e1.getY() > e2.getY())
            if (Scroll[current].getChildAt(0).getMeasuredHeight() <= Scroll[current].getHeight() + Scroll[current].getScrollY()) {
                mViewFlipper.showNext();
                current = Next[current];
                currentTime = TimeUtil.getTomorrow(currentTime);
                int tomorrow = Next[current];
                long tomorrowTime = TimeUtil.getTomorrow(currentTime);
                FromSQLToListView(tomorrowTime, Scroll[tomorrow], BothLayout[tomorrow], TipLayout[tomorrow], ScheduleLayout[tomorrow], imageView[tomorrow]);
            }
        else if (e1.getY() < e2.getY()) {
            if (Scroll[current].getScrollY() <= 0)
            {
                mViewFlipper.showPrevious();
                current = Prev[current];
                currentTime = TimeUtil.getYesterday(currentTime);
                int yesterday = Prev[current];
                long yesterdayTime = TimeUtil.getYesterday(currentTime);
                FromSQLToListView(yesterdayTime, Scroll[yesterday], BothLayout[yesterday], TipLayout[yesterday], ScheduleLayout[yesterday], imageView[yesterday]);
            }
        } else {
            return false;
        }
        return true;
    }
}
