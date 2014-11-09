package com.Utopia.utopia.app;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Joe on 14-10-4.
 */
public class NotePadListItemAdapter extends SimpleAdapter{
    int resource;
    Context context;

    public NotePadListItemAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        if(convertView != null) {
            view = convertView;
        }
        else {
            view = inflater.inflate(resource,null);
        }
        Map<String,Object> map = (Map<String,Object>)getItem(position);
        int marginLeft = (int)Long.parseLong(map.get("created").toString());
        int marginRight = (int)Long.parseLong(map.get("end").toString());
        String value = map.get("value").toString();
        if(position % 2 == 0) {
            view.findViewById(R.id.EventTextView0).setLayoutParams(new LinearLayout.LayoutParams(marginLeft, ViewGroup.LayoutParams.WRAP_CONTENT));
            view.findViewById(R.id.EventTextView1).setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        else {
            view.findViewById(R.id.EventTextView1).setLayoutParams(new LinearLayout.LayoutParams(marginRight, ViewGroup.LayoutParams.WRAP_CONTENT));
            view.findViewById(R.id.EventTextView0).setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        TextView textView = (TextView)view.findViewById(R.id.EventTextViewM);
        textView.setText(value);
        return view;
    }
}
