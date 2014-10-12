package com.Utopia.utopia.app;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.Utopia.utopia.app.SQL.DataProviderMetaData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewPagerFragment1 extends Fragment {
    final static int KIND_SCHEDULE = DataProviderMetaData.DataTableMetaData.KIND_SCHEDULE;

    private ListView lv0;
    private ContentResolver cr;
    private SimpleAdapter sa;
    List<Map<String, Object>> listResource = new ArrayList<Map<String, Object>>();
    int count;

    public ViewPagerFragment1() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout1,
                container, false);
        lv0 = (ListView) view.findViewById(R.id.page1ListView0);
        cr = getActivity().getContentResolver();
        FromSQLToListView();

        return view;
    }

    public void FromSQLToListView() {
        count = 0;
        Cursor cursor = cr.query(DataProviderMetaData.DataTableMetaData.CONTENT_URI, new String[]{"created", "modified", "title", "value", "begin",
                "end", "finish", "kind", "myhint"}, "kind = " + KIND_SCHEDULE, null, "begin asc");
        listResource.clear();

        while (cursor.moveToNext()) {
            ++count;
            Map<String, Object> map = new HashMap<String, Object>();
            long create, modified, begin, end, finish, kind;
            String title, value, hint;

            create = cursor.getLong(cursor.getColumnIndex("created"));
            modified = cursor.getLong(cursor.getColumnIndex("modified"));
            title = cursor.getString(cursor.getColumnIndex("title"));
            value = cursor.getString(cursor.getColumnIndex("value"));
            begin = cursor.getLong(cursor.getColumnIndex("begin"));
            end = cursor.getLong(cursor.getColumnIndex("end"));
            finish = cursor.getLong(cursor.getColumnIndex("finish"));
            kind = cursor.getLong(cursor.getColumnIndex("kind"));
            hint = cursor.getString(cursor.getColumnIndex("myhint"));

            map.put("created", create);
            map.put("modified", modified);
            map.put("title", title);
            map.put("value", value);
            map.put("begin", begin);
            map.put("end", end);
            map.put("finish", finish);
            map.put("kind", kind);
            map.put("myhint", hint);

            listResource.add(map);
        }
        cursor.close();
        if (count > 0) {
            sa = new SimpleAdapter(getActivity().getApplicationContext(), listResource, R.layout.notepad_listview,
                    new String[]{"value"}, new int[]{R.id.EventTextViewM});
            lv0.setAdapter(sa);
        }
    }
}

