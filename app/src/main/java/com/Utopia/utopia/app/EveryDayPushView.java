package com.Utopia.utopia.app;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joe on 14-9-7.
 */
public class EveryDayPushView extends RelativeLayout {

    ViewPager viewPager;
    TextView textView;
    LinearLayout linearLayout;


    public EveryDayPushView(Context context) {
        super(context);
        init();
    }

    public EveryDayPushView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EveryDayPushView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.every_day_push_view, this);

        ImageView imageView1 = new ImageView(getContext());
        ImageView imageView2 = new ImageView(getContext());
        imageView1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        imageView2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        imageView1.setImageResource(R.drawable.pic1);
        imageView2.setImageResource(R.drawable.pic2);
        imageView1.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView2.setScaleType(ImageView.ScaleType.CENTER_CROP);

        List<ImageView> list = new ArrayList<ImageView>();
        list.add(imageView1);
        list.add(imageView2);
        viewPager = (ViewPager)view.findViewById(R.id.test_view_pager);
        viewPager.setAdapter(new EveryDayPushViewAdapter(list));

    }

    class EveryDayPushViewAdapter extends PagerAdapter{

        private List<ImageView> imageViewList;
        EveryDayPushViewAdapter(List<ImageView> imageViewList) {
            this.imageViewList = imageViewList;
        }


        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ((ViewPager)container).addView(imageViewList.get(position));
            return imageViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager)container).removeView(imageViewList.get(position));
        }

        @Override
        public int getCount() {
            return imageViewList.size();
        }
    }
}
