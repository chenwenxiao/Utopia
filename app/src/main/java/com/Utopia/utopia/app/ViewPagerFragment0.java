package com.Utopia.utopia.app;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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

    public ViewPagerFragment0() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout0,
                container, false);
        button0 = (Button) view.findViewById(R.id.page0Button0);
        editText0 = (EditText) view.findViewById(R.id.page0EditText0);
        lv0 = (ListView) view.findViewById(R.id.page0ListView0);
        cr = getActivity().getContentResolver();
        FromSQLToListView();

        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = editText0.getText().toString();
                if (!value.equals("")) {
                    ContentValues cv = new ContentValues();
                    cv.put("value", value);
                    cv.put("kind", KIND_NOTE);
                    cr.insert(DataProviderMetaData.DataTableMetaData.CONTENT_URI, cv);

                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("value", value);
                    map.put("kind", KIND_NOTE);
                    listResource.add(map);
                    lv0.invalidateViews();
                }
            }
        });

        return view;
    }

    public void FromSQLToListView() {
        Uri uri = DataProviderMetaData.DataTableMetaData.CONTENT_URI;

        cr.insert(uri, null);

        Cursor cursor = cr.query(DataProviderMetaData.DataTableMetaData.CONTENT_URI, new String[]{"created", "value", "kind"}, "kind = " + KIND_NOTE, null, "created asc");

        Log.i("utopia", String.valueOf(cursor == null));

        while (cursor.moveToNext()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("created", cursor.getLong(cursor.getColumnIndex("created")));
            map.put("value", cursor.getString(cursor.getColumnIndex("value")));
            listResource.add(map);
        }
        cursor.close();
        sa = new SimpleAdapter(getActivity().getApplicationContext(), listResource, R.layout.notepad_listview,
                new String[]{"value"}, new int[]{R.id.EventEditText});
        lv0.setAdapter(sa);
    }
}

