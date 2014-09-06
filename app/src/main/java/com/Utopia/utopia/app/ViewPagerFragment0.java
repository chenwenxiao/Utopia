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

public class ViewPagerFragment0 extends Fragment {
    private ListView lv0;
    private SQLiteNotePad snp;
    private Button button0;
    private SimpleAdapter sa;
    private ProgressBar m_ProgressBar;
    private ActivityManager am;
    EditText editText0;
    List<Map<String, Object>> listResource = new ArrayList<Map<String, Object>>();
    int count;

    public ViewPagerFragment0() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout0,
                container, false);
        button0 = (Button) getActivity().findViewById(R.id.page0Button0);
        editText0 = (EditText) getActivity().findViewById(R.id.page0EditText0);
        lv0 = (ListView) getActivity().findViewById(R.id.page0ListView0);
        snp = new SQLiteNotePad(getActivity().getApplicationContext());
        FromSQLToListView();
        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newContent = editText0.getText().toString();
                if (!newContent.equals("")) {
                    ++count;
                    ContentValues newValues = new ContentValues();
                    newValues.put("_Id", count);
                    newValues.put("_content", newContent);

                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("_id", count);
                    map.put("_content", newContent);
                    listResource.add(map);
                    lv0.invalidateViews();
                }
            }
        });

        return view;
    }

    public void FromSQLToListView() {
        SQLiteDatabase sdb = snp.getReadableDatabase();
        count = 0;
        Cursor cursor = sdb.query("NotePad", new String[]{"_id", "_content"}, null, null, null, null, "_id asc");

        while (cursor.moveToNext()) {
            ++count;
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("_id", cursor.getInt(cursor.getColumnIndex("_id")));
            map.put("_content", cursor.getString(cursor.getColumnIndex("_content")));
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

