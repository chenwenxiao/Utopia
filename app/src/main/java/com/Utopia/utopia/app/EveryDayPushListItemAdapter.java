package com.Utopia.utopia.app;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.ListResourceBundle;
import java.util.Map;

/**
 * Created by chenwenxiao on 14-11-9.
 */

public class EveryDayPushListItemAdapter extends SimpleAdapter {
    int resource;
    Context context;
    List<List<Map<String, Object>>> listResource = new ArrayList<List<Map<String, Object>>>();

    public EveryDayPushListItemAdapter(Context context, List<Map<String, Object>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        this.context = context;
        this.resource = resource;
        long current, last;
        last = -1;
        for (Map<String, Object> it : data) {
            current = TimeUtil.getToday(Long.valueOf(it.get("begin").toString()));
            if (current != last) listResource.add(new ArrayList<Map<String, Object>>());
            (listResource.get(listResource.size() - 1)).add(it);
            last = current;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        if (convertView != null) {
            view = convertView;
        } else {
            view = new ViewPager(context);
        }
        List <Map<String, Object>> currentList = listResource.get(position);
        ((ViewPager) view).setAdapter(new EveryDayPushViewPagerAdapter(context, currentList));
        return view;
    }
}
