package com.Utopia.utopia.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import java.net.ContentHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ViewPagerFragment1 extends Fragment {
    private ListView lv0;
    private SQLiteSchedule snp;
    private SimpleAdapter sa;
    private ProgressBar m_ProgressBar;
    private ActivityManager am;
    EditText editText0;
    List<Map<String, Object>> listResource = new ArrayList<Map<String, Object>>();
    int count;

    public ViewPagerFragment1() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout0,
                container, false);
        lv0 = (ListView) getActivity().findViewById(R.id.page1ListView0);
        snp = new SQLiteSchedule(getActivity().getApplicationContext());
        FromSQLToListView();

        return view;
    }

    public void FromSQLToListView() {
        SQLiteDatabase sdb = snp.getReadableDatabase();
        count = 0;
        Cursor cursor = sdb.query("Schedule", new String[]{"_id", "_content", "_beginHour", "_beginMinute",
                "_endHour", "_endMinute", "_finish"}, null, null, null, null, "_id asc");

        while (cursor.moveToNext()) {
            ++count;
            Map<String, Object> map = new HashMap<String, Object>();
            int _id, _beginHour, _beginMinute, _endHour, _endMinute, _finish;
            String _content;
            _id = cursor.getInt(cursor.getColumnIndex("_id"));
            _content = cursor.getString(cursor.getColumnIndex("_content"));
            _beginHour = cursor.getInt(cursor.getColumnIndex("_beginHour"));
            _beginMinute = cursor.getInt(cursor.getColumnIndex("_beginMinute"));
            _endHour = cursor.getInt(cursor.getColumnIndex("_endHour"));
            _endMinute = cursor.getInt(cursor.getColumnIndex("_endMinute"));
            _finish = cursor.getInt(cursor.getColumnIndex("_endMinute"));
/*
            _content = (_beginHour < 10 ? "0":"") + _beginHour + ":" +
                    (_beginMinute < 10 ? "0":"") + _beginMinute + "\n" +
                    _content + "\n" +
                    (_endHour < 10 ? "0":"") + _endHour + ":" +
                    (_endMinute < 10 ? "0":"") + _endMinute + "\n";
            格式转化放在日历创建模块
*/

            map.put("_id", _id);
            map.put("_content", _content);
            map.put("_beginHour", _beginHour);
            map.put("_beginMinute", _beginMinute);
            map.put("_endHour", _endHour);
            map.put("_endMinute", _endMinute);
            map.put("_finish", _finish);
            listResource.add(map);
        }
        cursor.close();
        sdb.close();
        if (count > 0) {
            sa = new SimpleAdapter(getActivity().getApplicationContext(), listResource, R.layout.notepad_listview,
                    new String[]{"_content"}, new int[]{R.id.EventEditText});
            lv0.setAdapter(sa);
        }
    }
}

