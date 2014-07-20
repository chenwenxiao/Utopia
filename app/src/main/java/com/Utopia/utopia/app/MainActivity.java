package com.Utopia.utopia.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.activity_main);

        PagerTabStrip pagerTabStrip = (PagerTabStrip) findViewById(R.id.pagertab);
        pagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.gold));
        pagerTabStrip.setDrawFullUnderline(true);
        pagerTabStrip.setBackgroundColor(getResources().getColor(R.color.azure));
        pagerTabStrip.setTextSpacing(50);

        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewpager);
        ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();//ViewPager中显示的数据
        ArrayList<String> titleList = new ArrayList<String>();// 标题数据
        //添加数据
        fragmentList.add(new ViewPagerFragment0());
        fragmentList.add(new ViewPagerFragment1());
        fragmentList.add(new ViewPagerFragment2());
        fragmentList.add(new ViewPagerFragment3());
        fragmentList.add(new ViewPagerFragment4());

        titleList = new ArrayList<String>();// 每个页面的Title数据
        titleList.add("页面0");
        titleList.add("页面1");
        titleList.add("页面2");
        titleList.add("页面3");
        titleList.add("页面4");

        mViewPager.setOffscreenPageLimit(2);

        mViewPager.setAdapter(new MyPagerFragmentAdapter(
                getSupportFragmentManager(), fragmentList, titleList));

    }

    //适配器
    private class MyPagerFragmentAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragmentList;
        private List<String> titleList;

        public MyPagerFragmentAdapter(FragmentManager fragmentManager,
                                      List<Fragment> fragmentList, List<String> titleList) {
            super(fragmentManager);
            this.fragmentList = fragmentList;
            this.titleList = titleList;
        }

        // ViewPage中显示的内容
        @Override
        public Fragment getItem(int arg0) {
            // TODO Auto-generated method stub
            return fragmentList.get(arg0);
        }

        // Title中显示的内容
        @Override
        public CharSequence getPageTitle(int position) {
            // TODO Auto-generated method stub
            return titleList.get(position);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return fragmentList.size();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}