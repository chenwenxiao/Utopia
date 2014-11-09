package com.Utopia.utopia.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Administrator on 2014/5/21 0021.
 * 使用Fragment显示ViewPager中的主要内容
 */
public class ViewPagerFragment3 extends Fragment {
    public ViewPagerFragment3() {
        super();
    }

    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout3,
                container, false);
        imageView1 = (ImageView) view.findViewById(R.id.image_view_1);
        imageView2 = (ImageView) view.findViewById(R.id.image_view_2);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),EveryDayPushListActivity.class);
                startActivity(intent);
            }
        });
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),HealthTipListActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
