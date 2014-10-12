package com.Utopia.utopia.app;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
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
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.Utopia.utopia.app.SQL.DataProviderMetaData;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.Format;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Administrator on 2014/5/21 0021.
 * 使用Fragment显示ViewPager中的主要内容
 */
public class ViewPagerFragment2 extends Fragment {
    public static final int KIND_SCHEDULE = DataProviderMetaData.DataTableMetaData.KIND_SCHEDULE;
    public static final int KIND_TIP = DataProviderMetaData.DataTableMetaData.KIND_TIP;
    public static final int KIND_ADVERTISE = DataProviderMetaData.DataTableMetaData.KIND_ADVERTISE;

    public static int Next[] = new int[]{1, 2, 0};
    public static int Prev[] = new int[]{2, 0, 1};


    private ViewFlipper mViewFlipper;
    private ScrollView Scroll[];
    private LinearLayout BothLayout[], TipLayout[], ScheduleLayout[],TimeLineLayout[];
    private ImageView imageView[];
    ContentResolver cr;
    double secondLength;
    int current;
    long currentTime;
    TreeMap<String, LinearLayout> TipMap0, TipMap1, TipMap2, ScheduleMap0, ScheduleMap1, ScheduleMap2;
    QuickEntry qe;
    int imageLength;
    int startTime = 6;

    public ViewPagerFragment2() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout2,
                container, false);

        mViewFlipper = (ViewFlipper) view.findViewById(R.id.page2ViewFlipper);
        mViewFlipper.setDisplayedChild(1);
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
        TimeLineLayout = new LinearLayout[]{
                (LinearLayout) view.findViewById(R.id.page2Scroll0TimeLine),
                (LinearLayout) view.findViewById(R.id.page2Scroll1TimeLine),
                (LinearLayout) view.findViewById(R.id.page2Scroll2TimeLine),};

        buildTimeLine();

        for (int i = 0; i < 3; ++i) {
            Scroll[i].setLongClickable(true);
            Scroll[i].setFocusable(true);
            Scroll[i].setOnTouchListener(new View.OnTouchListener() {
                int beginY;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_MOVE:
                            break;
                        case MotionEvent.ACTION_DOWN:
                            beginY = (int) (event.getY() + Scroll[current].getScrollY());
                            break;
                        case MotionEvent.ACTION_UP:
                            if (beginY > event.getY()) {
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
                                }
                            } else if (beginY < event.getY()) {
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
                            }
                            break;
                    }
                    return false;
                }
            });

            Scroll[i].setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    qe = new QuickEntry(getActivity());
                    qe.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            addEvent(qe.getContent());
                        }
                    });
                    return true;
                }
            });
        }

        imageView = new ImageView[]{
                null, null, null
        };
        secondLength = imageLength / 86400.0;
        Log.v("DEBUG", String.valueOf(secondLength));

        TipMap0 = new TreeMap<String, LinearLayout>();
        TipMap1 = new TreeMap<String, LinearLayout>();
        TipMap2 = new TreeMap<String, LinearLayout>();
        ScheduleMap0 = new TreeMap<String, LinearLayout>();
        ScheduleMap1 = new TreeMap<String, LinearLayout>();
        ScheduleMap2 = new TreeMap<String, LinearLayout>();

        cr = getActivity().getContentResolver();

        long todayTime,
                tomorrowTime,
                yesterdayTime;

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
        finish = Long.valueOf(String.valueOf(map.get("finish")));
        kind = Long.valueOf(String.valueOf(map.get("kind")));
        title = String.valueOf(map.get("title"));
        value = String.valueOf(map.get("value"));
        hint = String.valueOf(map.get("myhint"));

        if (begin % 1000000 < startTime * 1000000)
            begin = begin % 1000000 + TimeUtil.getTomorrow(currentTime);
        else
            begin = begin % 1000000 + currentTime;

        if (end % 1000000 < startTime * 1000000)
            end = end % 1000000 + TimeUtil.getTomorrow(currentTime);
        else
            end = end % 1000000 + currentTime;

        map.put("begin", begin);
        map.put("end", end);

        ContentValues cv = new ContentValues();
        cv.put("created", created);
        cv.put("begin", begin);
        cv.put("end", end);
        cv.put("finish", finish);
        cv.put("kind", kind);
        cv.put("title", title);
        cv.put("value", value);
        cv.put("myhint", hint);

        cr.insert(DataProviderMetaData.DataTableMetaData.CONTENT_URI, cv);

        if (kind == KIND_SCHEDULE) {
            if (current == 0) insertSchedule(map, ScheduleLayout[0], ScheduleMap0);
            else if (current == 1) insertSchedule(map, ScheduleLayout[1], ScheduleMap1);
            else insertSchedule(map, ScheduleLayout[2], ScheduleMap2);
        } else if (kind == KIND_TIP) {
            if (current == 0) insertTip(map, TipLayout[0], TipMap0);
            else if (current == 1) insertTip(map, TipLayout[1], TipMap1);
            else insertTip(map, TipLayout[2], TipMap2);
        }
        ((ViewPagerFragment1)((MainActivity) getActivity()).fragmentList.get(1)).FromSQLToListView();
    }

    void deleteEvent(LinearLayout Layout) {
        TreeMap<String, LinearLayout> TipMap;
        if (current == 0) TipMap = TipMap0;
        else if (current == 1) TipMap = TipMap1;
        else TipMap = TipMap2;

        Iterator it = TipMap.keySet().iterator();
        long currentCreated, id;
        id = 0;
        while (it.hasNext()) {
            String key = it.next().toString();
            ++id;
            currentCreated = Long.parseLong(key.substring(14, 28).trim());
            if (TipMap.get(key) == Layout) {
                cr.delete(DataProviderMetaData.DataTableMetaData.CONTENT_URI,
                        "created = " + currentCreated
                                + " AND " + "kind = " + KIND_TIP, null);
                TipLayout[current].removeViewAt((int) id);
                TipMap.remove(key);
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
            String key = it.next().toString();
            ++id;
            currentCreated = Long.parseLong(key.substring(14, 28).trim());
            if (ScheduleMap.get(key) == Layout) {
                cr.delete(DataProviderMetaData.DataTableMetaData.CONTENT_URI,
                        "created = " + currentCreated
                                + " AND " + "kind = " + KIND_SCHEDULE, null);
                ScheduleLayout[current].removeViewAt((int) id);
                ScheduleMap.remove(key);
                updateSchedule(ScheduleLayout[current], ScheduleMap);
            }
        }
    }

    void updateSchedule(LinearLayout
                                ScheduleLayout, TreeMap<String, LinearLayout> ScheduleMap) {
        Iterator it = ScheduleMap.keySet().iterator();
        long current, currentCreated, reality = 0, wonder = 0, id = 0;
        while (it.hasNext()) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            String key = it.next().toString();
            current = Long.parseLong(key.substring(0, 14).trim());
            currentCreated = Long.parseLong(key.substring(14, 28).trim());
            reality = ScheduleMap.get(key).getTop();
            int topMargin = ((ViewGroup.MarginLayoutParams) ScheduleMap.get(key).getLayoutParams()).topMargin;
            lp.setMargins(0, (int) Math.max(topMargin - (reality - current), 0), 0, 0);
            ScheduleMap.get(key).setLayoutParams(lp);
        }
    }

    void updateTip(LinearLayout TipLayout, TreeMap<String, LinearLayout> TipMap) {
        Iterator it = TipMap.keySet().iterator();
        long current, currentCreated, reality = 0, wonder = 0, id = 0;
        while (it.hasNext()) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            String key = it.next().toString();
            current = Long.parseLong(key.substring(0, 14).trim());
            currentCreated = Long.parseLong(key.substring(14, 28).trim());
            reality = TipMap.get(key).getTop();
            int topMargin = ((ViewGroup.MarginLayoutParams) TipMap.get(key).getLayoutParams()).topMargin;
            lp.setMargins(0, (int) Math.max(topMargin - (reality - current), 0), 0, 0);
            TipMap.get(key).setLayoutParams(lp);
        }
    }


    void insertSchedule(Map<String, Object> map, LinearLayout
            ScheduleLayout, TreeMap<String, LinearLayout> ScheduleMap) {
        LayoutInflater flater = LayoutInflater.from(this.getActivity().getApplicationContext());
        LinearLayout newSchedule = (LinearLayout) flater.inflate(R.layout.notepad_listview, ScheduleLayout, false);
        long created, modified, begin, end, finish, kind;
        String title, value, hint;
        created = Long.valueOf(String.valueOf(map.get("created")));
        modified = Long.valueOf(String.valueOf(map.get("modified")));
        begin = Long.valueOf(String.valueOf(map.get("begin")));
        end = Long.valueOf(String.valueOf(map.get("end")));
        finish = Long.valueOf(String.valueOf(map.get("finish")));
        kind = Long.valueOf(String.valueOf(map.get("kind")));
        title = String.valueOf(map.get("title"));
        value = String.valueOf(map.get("value"));
        hint = String.valueOf(map.get("myhint"));

        TextView tv = (TextView) newSchedule.findViewById(R.id.EventTextViewM);
        tv.setText(value);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);


        //20140816105401
        Iterator it = ScheduleMap.keySet().iterator();
        long current, reality = 0, wonder = (int) (secondLength * ((TimeUtil.toSecond(begin) - startTime * 3600 + 86400) % 86400)), id = 0;
        while (it.hasNext()) {
            String key = it.next().toString();
            current = Long.parseLong(key.substring(0, 14).trim());
            if (current > wonder) {
                break;
            }
            reality = ScheduleMap.get(key).getTop() + ScheduleMap.get(key).getHeight();
            ++id;
        }
        String key = String.format("%14d%14d", wonder, created);
        ScheduleMap.put(key, newSchedule);
        lp.setMargins(0, (int) Math.max(wonder - reality, 0), 0, 0);
        newSchedule.setLayoutParams(lp);

        ScheduleLayout.addView(newSchedule, (int) id);
        updateSchedule(ScheduleLayout, ScheduleMap);
    }

    void insertTip(Map<String, Object> map, LinearLayout
            TipLayout, TreeMap<String, LinearLayout> TipMap) {
        LayoutInflater flater = LayoutInflater.from(this.getActivity().getApplicationContext());
        LinearLayout newTip = (LinearLayout) flater.inflate(R.layout.notepad_listview, TipLayout, false);
        long created, modified, begin, end, finish, kind;
        String title, value, hint;
        created = Long.valueOf(String.valueOf(map.get("created")));
        modified = Long.valueOf(String.valueOf(map.get("modified")));
        begin = Long.valueOf(String.valueOf(map.get("begin")));
        end = Long.valueOf(String.valueOf(map.get("end")));
        finish = Long.valueOf(String.valueOf(map.get("finish")));
        kind = Long.valueOf(String.valueOf(map.get("kind")));
        title = String.valueOf(map.get("title"));
        value = String.valueOf(map.get("value"));
        hint = String.valueOf(map.get("myhint"));

        TextView tv = (TextView) newTip.findViewById(R.id.EventTextViewM);
        tv.setText(value);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);


        //20140816105401
        Iterator it = TipMap.keySet().iterator();
        long current, reality = 0, wonder = (int) (secondLength * ((TimeUtil.toSecond(begin) - startTime * 3600 + 86400) % 86400)), id = 0;
        while (it.hasNext()) {
            String key = it.next().toString();
            current = Long.parseLong(key.substring(0, 14).trim());
            if (current > wonder) {
                break;
            }
            reality = TipMap.get(key).getTop() + TipMap.get(key).getHeight();
            ++id;
        }
        String key = String.format("%14d%14d", wonder, created);
        TipMap.put(key, newTip);
        lp.setMargins(0, (int) Math.max(wonder - reality, 0), 0, 0);
        newTip.setLayoutParams(lp);

        TipLayout.addView(newTip, (int) id);
        updateTip(TipLayout, TipMap);
    }

    void insertAdvertise(Map<String, Object> map, ImageView imageView) {

    }

    void FromSQLToListView(long todayTime, ScrollView Scroll, LinearLayout
            BothLayout, LinearLayout TipLayout, LinearLayout ScheduleLayout, ImageView imageView,
                           TreeMap<String, LinearLayout> TipMap, TreeMap<String, LinearLayout> ScheduleMap) {
        TipLayout.removeAllViews();
        ScheduleLayout.removeAllViews();
        TipMap.clear();
        ScheduleMap.clear();

        double yesterdayTime = TimeUtil.getYesterday(todayTime) + startTime * 10000;
        todayTime = TimeUtil.getToday(todayTime) + startTime * 10000;
        double tomorrowTime = TimeUtil.getTomorrow(todayTime) + startTime * 10000;
        Cursor cursor = cr.query(DataProviderMetaData.DataTableMetaData.CONTENT_URI, new String[]{"created", "modified", "title", "value", "begin",
                "end", "finish", "kind", "myhint"}, "kind = " + KIND_SCHEDULE +
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
            hint = cursor.getString(cursor.getColumnIndex("myhint"));

            map.put("created", created);
            map.put("modified", modified);
            map.put("title", title);
            map.put("value", value);
            map.put("begin", begin);
            map.put("end", end);
            map.put("finish", finish);
            map.put("kind", kind);
            map.put("myhint", hint);

            insertSchedule(map, ScheduleLayout, ScheduleMap);
        }

        cursor = cr.query(DataProviderMetaData.DataTableMetaData.CONTENT_URI, new String[]{"created", "modified", "title", "value", "begin",
                "end", "finish", "kind", "myhint"}, "kind = " + KIND_ADVERTISE +
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
            hint = cursor.getString(cursor.getColumnIndex("myhint"));

            map.put("created", created);
            map.put("modified", modified);
            map.put("title", title);
            map.put("value", value);
            map.put("begin", begin);
            map.put("end", end);
            map.put("finish", finish);
            map.put("kind", kind);
            map.put("myhint", hint);

            insertAdvertise(map, imageView);
        }

        cursor = cr.query(DataProviderMetaData.DataTableMetaData.CONTENT_URI, new String[]{"created", "modified", "title", "value", "begin",
                "end", "finish", "kind", "myhint"}, "kind = " + KIND_TIP +
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
            hint = cursor.getString(cursor.getColumnIndex("myhint"));

            map.put("created", created);
            map.put("modified", modified);
            map.put("title", title);
            map.put("value", value);
            map.put("begin", begin);
            map.put("end", end);
            map.put("finish", finish);
            map.put("kind", kind);
            map.put("myhint", hint);

            insertTip(map, TipLayout, TipMap);
        }
    }
    private void buildTimeLine()
    {
        Bitmap source = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.time_line_6_am);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        source.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
        try {
            BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()),true);
            imageLength = decoder.getHeight();
            int regionCount = 3;
            int height = decoder.getHeight() / regionCount;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            for(int i = 0;i < 3;i++) {
                TimeLineLayout[i].removeAllViews();
            }
            for(int i = 0;i < regionCount;i++) {
                Bitmap bitmap;
                if(i == regionCount-1) {
                    bitmap = decoder.decodeRegion(new Rect(0, i * height, decoder.getWidth(),decoder.getHeight()), null);
                } else {
                    bitmap = decoder.decodeRegion(new Rect(0, i * height, decoder.getWidth(), (i + 1) * height), null);
                }
                for(int j = 0;j < 3;j++) {
                    ImageView imageView= new ImageView(getActivity().getApplicationContext());
                    imageView.setImageBitmap(bitmap);
                    imageView.setLayoutParams(params);
                    TimeLineLayout[j].addView(imageView);
                }
            }
        } catch (IOException e) {
        }
    }
}