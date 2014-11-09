package com.Utopia.utopia.app;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Map;

/**
 * Created by Joe on 14-11-9.
 */
public class EveryDayPushViewPagerAdapter extends PagerAdapter {
    private List<EveryDayPushView> ViewList;

    public EveryDayPushViewPagerAdapter(Context context, List<Map<String, Object>> mapList) {
        super();
        for (Map<String, Object> it : mapList) {
            ViewList.add(new EveryDayPushView(context, it));
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
        //TODO
    }

    @Override
    public int getCount() {
        return ViewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
