package com.Utopia.utopia.app;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.Utopia.utopia.app.R;
import com.Utopia.utopia.app.SQL.DataProviderMetaData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EveryDayPushListActivity extends Activity {

    public static final int KIND_SCHEDULE = DataProviderMetaData.DataTableMetaData.KIND_SCHEDULE;
    public static final int KIND_TIP = DataProviderMetaData.DataTableMetaData.KIND_TIP;
    public static final int KIND_ADVERTISE = DataProviderMetaData.DataTableMetaData.KIND_ADVERTISE;

    private ListView lv0;
    private ContentResolver cr;
    private SimpleAdapter sa;
    List<Map<String, Object>> listResource = new ArrayList<Map<String, Object>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_every_day_push_list);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        //
        init();
    }

    void init() {
        lv0 = (ListView) findViewById(R.id.every_day_push_list);
        registerForContextMenu(lv0);

        cr = getContentResolver();
        Cursor cursor = cr.query(DataProviderMetaData.DataTableMetaData.CONTENT_URI, new String[]{"created", "modified", "title", "value", "begin",
                "end", "finish", "kind", "myhint", "bitmap"}, "kind = " + KIND_ADVERTISE,
                null, "begin DESC");
        listResource.clear();
        while (cursor.moveToNext()) {
            Map<String, Object> map = new HashMap<String, Object>();
            long created, modified, begin, end, finish, kind;
            String title, value, hint;
            byte[] bitmap;

            created = cursor.getLong(cursor.getColumnIndex("created"));
            modified = cursor.getLong(cursor.getColumnIndex("modified"));
            title = cursor.getString(cursor.getColumnIndex("title"));
            value = cursor.getString(cursor.getColumnIndex("value"));
            begin = cursor.getLong(cursor.getColumnIndex("begin"));
            end = cursor.getLong(cursor.getColumnIndex("end"));
            finish = cursor.getLong(cursor.getColumnIndex("finish"));
            kind = cursor.getLong(cursor.getColumnIndex("kind"));
            hint = cursor.getString(cursor.getColumnIndex("myhint"));
            bitmap = cursor.getBlob(cursor.getColumnIndex("bitmap"));

            map.put("created", created);
            map.put("modified", modified);
            map.put("title", title);
            map.put("value", value);
            map.put("begin", begin);
            map.put("end", end);
            map.put("finish", finish);
            map.put("kind", kind);
            map.put("myhint", hint);
            map.put("bitmap", ObjectAndByte.toObject(bitmap));

            listResource.add(map);
        }
        sa = new EveryDayPushListItemAdapter(getApplicationContext(), listResource, R.layout.notepad_listitem, null, null);
        lv0.setAdapter(sa);
        cursor.close();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.every_day_push_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
