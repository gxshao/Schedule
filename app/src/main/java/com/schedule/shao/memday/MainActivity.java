package com.schedule.shao.memday;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.TextView;

import com.schedule.shao.memday.Fragments.FragmentAdd;
import com.schedule.shao.memday.Fragments.FragmentMonth;
import com.schedule.shao.memday.Fragments.FragmentSettings;
import com.schedule.shao.memday.Fragments.FragmentToday;
import com.schedule.shao.memday.Utils.ScheduleSQLOperation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    /**
     * 一堆控件
     */
    ViewPager viewPager;
    String mToday = "";
    String mYear, mMonth, mDay;
    String mSelectedMonth = "";
    FragmentPagerAdapter mAdapter;
    List<Fragment> mFragments = new ArrayList<Fragment>();
    TextView ActionBarTitle;
    ActionBar mActionBar;
    int isMenuShow = 0;
    RadioButton btn_today;
    int[] MenuItemGroup1 = new int[]{R.id.hideCompleted, R.id.SortByTime, R.id.SortByTitle,
            R.id.SortByPri, R.id.SearchByDate, R.id.SearchByTitle, R.id.thisMonth};
    int[] MenuItemGroup2 = new int[]{R.id.SaveAndQuit, R.id.SaveAndNew, R.id.btn_SendMsg, R.id.btn_Cancel};
    myBroadcastReciver brd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initTab();
        viewPager.setOnPageChangeListener(PageChanged);
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

    }

    private void initData() {
        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR) + "";
        mMonth = cal.get(Calendar.MONTH) + 1 + "";
        mDay = cal.get(Calendar.DATE) + "";
        mToday = mYear + "年" + mMonth + "月" + mDay + "日";
        mSelectedMonth = mToday.substring(0, 7);
        ScheduleSQLOperation.CreateDatebase(this);
    }

    private ViewPager.OnPageChangeListener PageChanged = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //加载该页数据代码
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void initTab() {
        mActionBar=this.getSupportActionBar();
        mActionBar.setCustomView(R.layout.actionbar);
        mActionBar.setDisplayOptions(getSupportActionBar().DISPLAY_SHOW_CUSTOM);
        ActionBarTitle = (TextView) findViewById(R.id.bar_title);
        ActionBarTitle.setText("纪念日");
        /**
         * 底部控件
         */
         btn_today= (RadioButton) findViewById(R.id.btn_today);
        btn_today.setOnClickListener(TabEventListener);
        btn_today.setChecked(true);
        RadioButton btn_month= (RadioButton) findViewById(R.id.btn_month);
        btn_month.setOnClickListener(TabEventListener);
        RadioButton btn_add= (RadioButton) findViewById(R.id.btn_add);
        btn_add.setOnClickListener(TabEventListener);
        RadioButton btn_settings= (RadioButton) findViewById(R.id.btn_settings);
        btn_settings.setOnClickListener(TabEventListener);
        FragmentToday ft = new FragmentToday();
        FragmentMonth fm = new FragmentMonth();
        FragmentAdd fa = new FragmentAdd();
        FragmentSettings fs = new FragmentSettings();
        mFragments.add(ft);
        mFragments.add(fm);
        mFragments.add(fa);
        mFragments.add(fs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        mAdapter = new MyAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(0);
        invalidateOptionsMenu();
        IntentFilter inte=new IntentFilter();
        inte.addAction("add");
        brd=new myBroadcastReciver();
        registerReceiver(brd,inte);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        setOverflowIconVisible(featureId, menu);
        return super.onMenuOpened(featureId, menu);
    }

    //显示菜单栏图标
    private void setOverflowIconVisible(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    Log.d("OverflowIconVisible", e.getMessage());
                }
            }
        }
    }

    private View.OnClickListener TabEventListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            v.requestFocus();
            switch (Integer.parseInt(v.getTag().toString())) {
                case 1:
                    isMenuShow = 0;
                    viewPager.setCurrentItem(0);
                    ActionBarTitle.setText("纪念日  " + mToday);
                    break;
                case 2:
                    mFragments.get(1).onResume();
                    isMenuShow = 1;

                    MainActivity.this.getWindow().invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL);
                    viewPager.setCurrentItem(1);
                    ActionBarTitle.setText("日历信息");
                    break;
                case 3:
                    isMenuShow = 2;
                    MainActivity.this.getWindow().invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL);
                    viewPager.setCurrentItem(2);
                    ActionBarTitle.setText("添加纪念日");
                    break;
                case 4:
                    isMenuShow = 3;
                    viewPager.setCurrentItem(3);
                    ActionBarTitle.setText("个人设置");

                    break;
                default:
                    break;
            }

        }
    };

    class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isMenuShow == 1 || isMenuShow == 2)
            getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //响应菜单信息
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
            try{
                Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                m.setAccessible(true);
                m.invoke(menu, true);
            } catch (Exception e) {
                Log.e(getClass().getSimpleName(), "onMenuOpened...unable to set icons for overflow menu", e);
            }
        }
        switch (isMenuShow) {
            case 1:
                for (int i = 0; i < MenuItemGroup1.length; i++) {
                    menu.findItem(MenuItemGroup1[i]).setVisible(true);
                }
                for (int i = 0; i < MenuItemGroup2.length; i++) {
                    menu.findItem(MenuItemGroup2[i]).setVisible(false);
                }

                break;
            case 2:
                for (int i = 0; i < MenuItemGroup1.length; i++) {
                    menu.findItem(MenuItemGroup1[i]).setVisible(false);
                }
                for (int i = 0; i < MenuItemGroup2.length; i++) {
                    menu.findItem(MenuItemGroup2[i]).setVisible(true);
                }
                break;
            default:
                break;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(brd);
        super.onDestroy();

    }
    class  myBroadcastReciver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            btn_today.setChecked(true);
            btn_today.performClick();
        }
    }
}
