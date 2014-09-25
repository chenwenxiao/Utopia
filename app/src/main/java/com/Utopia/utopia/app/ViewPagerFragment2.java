package com.Utopia.utopia.app;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.ViewFlipper;

import com.Utopia.utopia.app.SQL.DataProviderMetaData;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Administrator on 2014/5/21 0021.
 * 使用Fragment显示ViewPager中的主要内容
 */
public class ViewPagerFragment2 extends Fragment implements GestureDetector.OnGestureListener {
    public static final int KIND_SCHEDULE = DataProviderMetaData.DataTableMetaData.KIND_SCHEDULE;
    public static final int KIND_TIP = DataProviderMetaData.DataTableMetaData.KIND_TIP;
    public static final int KIND_ADVERTISE = DataProviderMetaData.DataTableMetaData.KIND_ADVERTISE;

    public static int Next[] = new int[]{1, 2, 0};
    public static int Prev[] = new int[]{2, 0, 1};


    private ViewFlipper mViewFlipper;
    private ScrollView Scroll[];
    private LinearLayout BothLayout[], TipLayout[], ScheduleLayout[];
    private ImageView imageView[];
    ContentResolver cr;
    double secondLength;
    int current;
    long currentTime;
    TreeMap<String, LinearLayout> TipMap0, TipMap1, TipMap2, ScheduleMap0, ScheduleMap1, ScheduleMap2;

    public ViewPagerFragment2() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout2,
                container, false);
        mViewFlipper = (ViewFlipper) view.findViewById(R.id.page2ViewFlipper);
        Scroll = new ScrollView[]{
                (ScrollView) view.findViewById(R.id.page2Scroll0),
                (ScrollView) view.findViewById(R.id.page2Scroll1),
                (ScrollView) view.findViewById(R.id.page2Scroll2)};
        BothLayout = new LinearLayout[]{
                (LinearLayout) view.findViewById(R.id.page2Scroll0BothLayout),
                (LinearLayout) view.findViewById(R.id.page2Scroll1BothLayout),
                (LinearLayout) view.findViewById(R.id.page2Scroll2BothLayout)};
        TipLayout = new LinearLayout[]{
                (LinearLayout) view.findViewById(R.id.page2Scroll0TipLayout),
                (LinearLayout) view.findViewById(R.id.page2Scroll1TipLayout),
                (LinearLayout) view.findViewById(R.id.page2Scroll2TipLayout)};
        ScheduleLayout = new LinearLayout[]{
                (LinearLayout) view.findViewById(R.id.page2Scroll0ScheduleLayout),
                (LinearLayout) view.findViewById(R.id.page2Scroll1ScheduleLayout),
                (LinearLayout) view.findViewById(R.id.page2Scroll2ScheduleLayout)};

        imageView = new ImageView[]{null,null,null

        };
        ImageView TimeLine = (ImageView) view.findViewById(R.id.page2Scroll1TimeLine);
        secondLength = TimeLine.getHeight() / 86400.0;

        TipMap0 = new TreeMap<String, LinearLayout>();
        TipMap1 = new TreeMap<String, LinearLayout>();
        TipMap2 = new TreeMap<String, LinearLayout>();
        ScheduleMap0 = new TreeMap<String, LinearLayout>();
        ScheduleMap1 = new TreeMap<String, LinearLayout>();
        ScheduleMap2 = new TreeMap<String, LinearLayout>();

        cr = getActivity().getContentResolver();

        long todayTime, tomorrowTime, yesterdayTime;

        currentTime = TimeUtil.getCurrentTime();
        yesterdayTime = TimeUtil.getYesterday(currentTime);
        todayTime = TimeUtil.getToday(currentTime);
        tomorrowTime = TimeUtil.getTomorrow(currentTime);

        current = 1;
        currentTime = todayTime;
        FromSQLToListView(yesterdayTime, Scroll[0], BothLayout[0], TipLayout[0], ScheduleLayout[0], imageView[0], TipMap0, ScheduleMap0);
        FromSQLToListView(todayTime, Scroll[1], BothLayout[1], TipLayout[1], ScheduleLayout[1], imageView[1], TipMap1, ScheduleMap1);
        FromSQLToListView(tomorrowTime, Scroll[2], BothLayout[2], TipLayout[2], ScheduleLayout[2], imageView[2], TipMap2, ScheduleMap2);

        return view;
    }

    void addEvent(Map<String, Object> map) {
        long created, modified, begin, end, finish, kind;
        String title, value, hint;
        created = Long.valueOf(String.valueOf(map.get("created")));
        modified = Long.valueOf(String.valueOf(map.get("modified")));
        begin = Long.valueOf(String.valueOf(map.get("begin")));
        end = Long.valueOf(String.valueOf(map.get("end")));
        finish = Long.valueOf(String.valueOf(map.get("begin")));
        kind = Long.valueOf(String.valueOf(map.get("end")));
        title = String.valueOf(map.get("title"));
        value = String.valueOf(map.get("value"));
        hint = String.valueOf(map.get("hint"));

        ContentValues cv = new ContentValues();
        cv.put("created", created);
        cv.put("begin", begin);
        cv.put("end", end);
        cv.put("finish", finish);
        cv.put("kind", kind);
        cv.put("title", title);
        cv.put("value", value);
        cv.put("hint", hint);

        cr.insert(DataProviderMetaData.DataTableMetaData.CONTENT_URI, cv);

        if (kind == KIND_SCHEDULE) {
            if (current == 0) insertSchedule(map, ScheduleLayout[0], ScheduleMap0);
            else if (current == 1) insertSchedule(map, ScheduleLayout[1], ScheduleMap1);
            else insertSchedule(map, ScheduleLayout[2], ScheduleMap2);
        }
        else if (kind == KIND_TIP) {
            if (current == 0) insertTip(map, TipLayout[0], TipMap0);
            else if (current == 1) insertTip(map, TipLayout[1], TipMap1);
            else insertTip(map, TipLayout[2], TipMap2);
        }
    }

    void deleteEvent(LinearLayout Layout) {
        TreeMap<String, LinearLayout> TipMap;
        if (current == 0) TipMap = TipMap0;
        else if (current == 1) TipMap = TipMap1;
        else TipMap = TipMap2;

        Iterator it = TipMap.keySet().iterator();
        long currentCreated, id = 0;
        while (it.hasNext()) {
            it.next();
            ++id;
            currentCreated = Long.parseLong(it.toString().substring(14, 28));
            if (TipMap.get(it) == Layout) {
                cr.delete(DataProviderMetaData.DataTableMetaData.CONTENT_URI,
                        "created = " + currentCreated
                        + " AND " + "kind = " + KIND_TIP, null);
                TipLayout[current].removeViewAt((int) id);
                TipMap.remove(it);
                updateTip(TipLayout[current], TipMap);
            }
        }

        TreeMap<String, LinearLayout> ScheduleMap;
        if (current == 0) ScheduleMap = ScheduleMap0;
        else if (current == 1) ScheduleMap = ScheduleMap1;
        else ScheduleMap = ScheduleMap2;

        it = ScheduleMap.keySet().iterator();
        id = 0;
        while (it.hasNext()) {
            it.next();
            ++id;
            currentCreated = Long.parseLong(it.toString().substring(14, 28));
            if (ScheduleMap.get(it) == Layout) {
                cr.delete(DataProviderMetaData.DataTableMetaData.CONTENT_URI,
                        "created = " + currentCreated
                                + " AND " + "kind = " + KIND_SCHEDULE, null);
                ScheduleLayout[current].removeViewAt((int) id);
                ScheduleMap.remove(it);
                updateSchedule(ScheduleLayout[current], ScheduleMap);
            }
        }
    }

    void updateSchedule(LinearLayout ScheduleLayout, TreeMap<String, LinearLayout> ScheduleMap) {
        Iterator it = ScheduleMap.keySet().iterator();
        long current, currentCreated, reality = 0, wonder = 0, id = 0;
        while (it.hasNext()) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            it.next();
            current = Long.parseLong(it.toString().substring(0, 14));
            currentCreated = Long.parseLong(it.toString().substring(14, 28));
            reality = ScheduleMap.get(it).getTop();
            int topMargin = ((ViewGroup.MarginLayoutParams) ScheduleMap.get(it).getLayoutParams()).topMargin;
            lp.setMargins(0, (int) Math.max(topMargin - (reality - current), 0), 0, 0);
            ScheduleMap.get(it).setLayoutParams(lp);
        }
    }

    void updateTip(LinearLayout TipLayout, TreeMap<String, LinearLayout> TipMap) {
        Iterator it = TipMap.keySet().iterator();
        long current, currentCreated, reality = 0, wonder = 0, id = 0;
        while (it.hasNext()) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            it.next();
            current = Long.parseLong(it.toString().substring(0, 14));
            currentCreated = Long.parseLong(it.toString().substring(14, 28));
            reality = TipMap.get(it).getTop();
            int topMargin = ((ViewGroup.MarginLayoutParams) TipMap.get(it).getLayoutParams()).topMargin;
            lp.setMargins(0, (int) Math.max(topMargin - (reality - current), 0), 0, 0);
            TipMap.get(it).setLayoutParams(lp);
        }
    }


    void insertSchedule(Map<String, Object> map, LinearLayout ScheduleLayout, TreeMap<String, LinearLayout> ScheduleMap) {
        LayoutInflater flater = LayoutInflater.from(this.getActivity().getApplicationContext());
        LinearLayout newSchedule = (LinearLayout) flater.inflate(R.layout.notepad_listview, ScheduleLayout, false);
        long created, modified, begin, end, finish, kind;
        String title, value, hint;
        created = Long.valueOf(String.valueOf(map.get("created")));
        modified = Long.valueOf(String.valueOf(map.get("modified")));
        begin = Long.valueOf(String.valueOf(map.get("begin")));
        end = Long.valueOf(String.valueOf(map.get("end")));
        finish = Long.valueOf(String.valueOf(map.get("begin")));
        kind = Long.valueOf(String.valueOf(map.get("end")));
        title = String.valueOf(map.get("title"));
        value = String.valueOf(map.get("value"));
        hint = String.valueOf(map.get("hint"));

        EditText et = (EditText) newSchedule.findViewById(R.id.EventEditText);
        et.setText(value);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);


        //20140816105401
        Iterator it = ScheduleMap.keySet().iterator();
        long current, reality = 0, wonder = (int) (secondLength * TimeUtil.toSecond(begin)), id = 0;
        while (it.hasNext()) {
            it.next();
            current = Long.parseLong(it.toString().substring(0, 14));
            if (current > wonder) {
                break;
            }
            reality = ScheduleMap.get(it).getTop() + ScheduleMap.get(it).getHeight();
            ++id;
        }
        ScheduleMap.put(wonder + "" + created, newSchedule);
        lp.setMargins(0, (int) Math.max(wonder - reality, 0), 0, 0);
        newSchedule.setLayoutParams(lp);

        ScheduleLayout.addView(newSchedule, (int) id);
        updateSchedule(ScheduleLayout, ScheduleMap);
    }

    void insertTip(Map<String, Object> map, LinearLayout TipLayout, TreeMap<String, LinearLayout> TipMap) {
        LayoutInflater flater = LayoutInflater.from(this.getActivity().getApplicationContext());
        LinearLayout newTip = (LinearLayout) flater.inflate(R.layout.notepad_listview, TipLayout, false);
        long created, modified, begin, end, finish, kind;
        String title, value, hint;
        created = Long.valueOf(String.valueOf(map.get("created")));
        modified = Long.valueOf(String.valueOf(map.get("modified")));
        begin = Long.valueOf(String.valueOf(map.get("begin")));
        end = Long.valueOf(String.valueOf(map.get("end")));
        finish = Long.valueOf(String.valueOf(map.get("begin")));
        kind = Long.valueOf(String.valueOf(map.get("end")));
        title = String.valueOf(map.get("title"));
        value = String.valueOf(map.get("value"));
        hint = String.valueOf(map.get("hint"));

        EditText et = (EditText) newTip.findViewById(R.id.EventEditText);
        et.setText(value);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);


        //20140816105401
        Iterator it = TipMap.keySet().iterator();
        long current, reality = 0, wonder = (int) (secondLength * TimeUtil.toSecond(begin)), id = 0;
        while (it.hasNext()) {
            it.next();
            current = Long.parseLong(it.toString().substring(0, 14));
            if (current > wonder) {
                break;
            }
            reality = TipMap.get(it).getTop() + TipMap.get(it).getHeight();
            ++id;
        }
        TipMap.put(wonder + "" + created, newTip);
        lp.setMargins(0, (int) Math.max(wonder - reality, 0), 0, 0);
        newTip.setLayoutParams(lp);

        TipLayout.addView(newTip, (int) id);
        updateTip(TipLayout, TipMap);
    }

    void insertAdvertise(Map<String, Object> map, ImageView imageView) {

    }

    void FromSQLToListView(long todayTime, ScrollView Scroll, LinearLayout BothLayout, LinearLayout TipLayout, LinearLayout ScheduleLayout, ImageView imageView,
                           TreeMap<String, LinearLayout> TipMap, TreeMap<String, LinearLayout> ScheduleMap) {

        double yesterdayTime = TimeUtil.getYesterday(todayTime);
        todayTime = TimeUtil.getToday(todayTime);
        double tomorrowTime = TimeUtil.getTomorrow(todayTime);
        Cursor cursor = cr.query(DataProviderMetaData.DataTableMetaData.CONTENT_URI, new String[]{"created", "modified", "title", "value", "begin",
                "end", "finish", "kind"}, "kind = " + KIND_SCHEDULE +
                " AND " + "(begin < " + tomorrowTime +
                " AND " + "begin >= " + todayTime + ")", null, "begin asc");
        while (cursor.moveToNext()) {
            Map<String, Object> map = new HashMap<String, Object>();
            long created, modified, begin, end, finish, kind;
            String title, value, hint;

            created = cursor.getLong(cursor.getColumnIndex("created"));
            modified = cursor.getLong(cursor.getColumnIndex("modified"));
            title = cursor.getString(cursor.getColumnIndex("title"));
            value = cursor.getString(cursor.getColumnIndex("value"));
            begin = cursor.getLong(cursor.getColumnIndex("begin"));
            end = cursor.getLong(cursor.getColumnIndex("end"));
            finish = cursor.getLong(cursor.getColumnIndex("finish"));
            kind = cursor.getLong(cursor.getColumnIndex("kind"));
            hint = cursor.getString(cursor.getColumnIndex("hint"));

            map.put("create", created);
            map.put("modified", modified);
            map.put("title", title);
            map.put("value", value);
            map.put("begin", begin);
            map.put("end", end);
            map.put("finish", finish);
            map.put("kind", kind);
            map.put("hint", hint);

            insertSchedule(map, ScheduleLayout, ScheduleMap);
        }

        cursor = cr.query(DataProviderMetaData.DataTableMetaData.CONTENT_URI, new String[]{"created", "modified", "title", "value", "begin",
                "end", "finish", "kind"}, "kind = " + KIND_ADVERTISE +
                " AND " + "(begin < " + tomorrowTime +
                " AND " + "begin >= " + todayTime + ")", null, "begin asc");
        while (cursor.moveToNext()) {
            Map<String, Object> map = new HashMap<String, Object>();
            long created, modified, begin, end, finish, kind;
            String title, value, hint;

            created = cursor.getLong(cursor.getColumnIndex("created"));
            modified = cursor.getLong(cursor.getColumnIndex("modified"));
            title = cursor.getString(cursor.getColumnIndex("title"));
            value = cursor.getString(cursor.getColumnIndex("value"));
            begin = cursor.getLong(cursor.getColumnIndex("begin"));
            end = cursor.getLong(cursor.getColumnIndex("end"));
            finish = cursor.getLong(cursor.getColumnIndex("finish"));
            kind = cursor.getLong(cursor.getColumnIndex("kind"));
            hint = cursor.getString(cursor.getColumnIndex("hint"));

            map.put("created", created);
            map.put("modified", modified);
            map.put("title", title);
            map.put("value", value);
            map.put("begin", begin);
            map.put("end", end);
            map.put("finish", finish);
            map.put("kind", kind);
            map.put("hint", hint);

            insertAdvertise(map, imageView);
        }

        cursor = cr.query(DataProviderMetaData.DataTableMetaData.CONTENT_URI, new String[]{"created", "modified", "title", "value", "begin",
                "end", "finish", "kind"}, "kind = " + KIND_TIP +
                " AND " + "(begin < " + tomorrowTime +
                " AND " + "begin >= " + todayTime + ")", null, "begin asc");
        while (cursor.moveToNext()) {
            Map<String, Object> map = new HashMap<String, Object>();
            long created, modified, begin, end, finish, kind;
            String title, value, hint;

            created = cursor.getLong(cursor.getColumnIndex("created"));
            modified = cursor.getLong(cursor.getColumnIndex("modified"));
            title = cursor.getString(cursor.getColumnIndex("title"));
            value = cursor.getString(cursor.getColumnIndex("value"));
            begin = cursor.getLong(cursor.getColumnIndex("begin"));
            end = cursor.getLong(cursor.getColumnIndex("end"));
            finish = cursor.getLong(cursor.getColumnIndex("finish"));
            kind = cursor.getLong(cursor.getColumnIndex("kind"));
            hint = cursor.getString(cursor.getColumnIndex("hint"));

            map.put("created", created);
            map.put("modified", modified);
            map.put("title", title);
            map.put("value", value);
            map.put("begin", begin);
            map.put("end", end);
            map.put("finish", finish);
            map.put("kind", kind);
            map.put("hint", hint);

            insertTip(map, TipLayout, TipMap);
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

    private void log_int(float i) {
        Log.i("utopia_fling", String.valueOf(i));
    }



    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        //finger move to Up
        log_int(e1.getY());
        log_int(e2.getY());
        log_int(velocityX);
        log_int(velocityY);
        if (e1.getY() > e2.getY())
            if (Scroll[current].getChildAt(0).getMeasuredHeight() <= Scroll[current].getHeight() + Scroll[current].getScrollY()) {
                mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mViewFlipper.getContext(), R.anim.push_up_in));
                mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mViewFlipper.getContext(), R.anim.push_up_out));
                mViewFlipper.showNext();
                current = Next[current];
                currentTime = TimeUtil.getTomorrow(currentTime);
                int tomorrow = Next[current];
                long tomorrowTime = TimeUtil.getTomorrow(currentTime);
                if (tomorrow == 0)
                    FromSQLToListView(tomorrowTime, Scroll[0], BothLayout[0], TipLayout[0], ScheduleLayout[0], imageView[0], TipMap0, ScheduleMap0);
                else if (tomorrow == 1)
                    FromSQLToListView(tomorrowTime, Scroll[1], BothLayout[1], TipLayout[1], ScheduleLayout[1], imageView[1], TipMap1, ScheduleMap1);
                else
                    FromSQLToListView(tomorrowTime, Scroll[2], BothLayout[2], TipLayout[2], ScheduleLayout[2], imageView[2], TipMap2, ScheduleMap2);
            } else if (e1.getY() < e2.getY()) {
                if (Scroll[current].getScrollY() <= 0) {
                    mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mViewFlipper.getContext(), R.anim.push_down_in));
                    mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mViewFlipper.getContext(), R.anim.push_down_out));
                    mViewFlipper.showPrevious();
                    current = Prev[current];
                    currentTime = TimeUtil.getYesterday(currentTime);
                    int yesterday = Prev[current];
                    long yesterdayTime = TimeUtil.getYesterday(currentTime);
                    if (yesterday == 0)
                        FromSQLToListView(yesterdayTime, Scroll[0], BothLayout[0], TipLayout[0], ScheduleLayout[0], imageView[0], TipMap0, ScheduleMap0);
                    else if (yesterday == 1)
                        FromSQLToListView(yesterdayTime, Scroll[1], BothLayout[1], TipLayout[1], ScheduleLayout[1], imageView[1], TipMap1, ScheduleMap1);
                    else
                        FromSQLToListView(yesterdayTime, Scroll[2], BothLayout[2], TipLayout[2], ScheduleLayout[2], imageView[2], TipMap2, ScheduleMap2);
                }
            } else {
                return false;
            }
        return true;
    }
}
