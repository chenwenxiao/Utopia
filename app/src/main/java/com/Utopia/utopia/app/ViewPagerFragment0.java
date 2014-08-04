package com.Utopia.utopia.app;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.Utopia.utopia.app.SQL.DataProviderMetaData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewPagerFragment0 extends Fragment {
    public static final int KIND_NOTE = DataProviderMetaData.DataTableMetaData.KIND_NOTE;

    private ListView lv0;
    private ContentResolver cr;
    private Button button0;
    private SimpleAdapter sa;
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
        cr = getActivity().getContentResolver();
        FromSQLToListView();

        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newValue = editText0.getText().toString();
                if (!newValue.equals("")) {
                    Long now = System.currentTimeMillis();
                    ++count;

                    ContentValues cv = new ContentValues();
                    cv.put("value", newValue);
                    cv.put("kind", KIND_NOTE);
                    cr.insert(Uri.parse("") ,cv);

                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("value", newValue);
                    map.put("kind", KIND_NOTE);
                    listResource.add(map);
                    lv0.invalidateViews();
                }
            }
        });

        return view;
    }

    public void FromSQLToListView() {
        count = 0;
        Cursor cursor = cr.query(Uri.parse(""), new String[]{"create", "value", "kind"}, "kind = " + KIND_NOTE, null, "create asc");

        while (cursor.moveToNext()) {
            ++count;
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("create", cursor.getLong(cursor.getColumnIndex("create")));
            map.put("value", cursor.getString(cursor.getColumnIndex("value")));
            listResource.add(map);
        }
        cursor.close();
        if (count > 0) {
            sa = new SimpleAdapter(getActivity().getApplicationContext(), listResource, R.layout.notepad_listview,
                    new String[]{"create"}, new int[]{R.id.EventEditText});
            lv0.setAdapter(sa);
        }
    }
}

