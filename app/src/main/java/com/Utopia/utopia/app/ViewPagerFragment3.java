package com.Utopia.utopia.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2014/5/21 0021.
 * 使用Fragment显示ViewPager中的主要内容
 */
public class ViewPagerFragment3 extends Fragment {
    public ViewPagerFragment3() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout3,
                container, false);
        return view;
    }
}
