package com.kuoruan.carouselfigure;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private ViewPager vp_main;
    private TextView tv_title;
    private LinearLayout ll_point_container;
    private int[] mImageResIds;
    private String[] mContentTitles;
    private List<ImageView> mImageList;
    private int previousSelectedPosition = 0;
    private Timer mTimer;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initAdapter();
        initTimer();
    }

    private void initTimer() {
        mTimer = new Timer(true);
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        vp_main.setCurrentItem(vp_main.getCurrentItem() + 1);
                    }
                });
            }
        }, 0, 2000);

    }

    private void initAdapter() {
        ll_point_container.getChildAt(0).setEnabled(true);
        tv_title.setText(mContentTitles[0]);
        previousSelectedPosition = 0;

        // 设置适配器
        vp_main.setAdapter(new MyViewPagerAdapter());

        // 默认设置到中间的某个位置
        vp_main.setCurrentItem(5000000);
    }

    private void initData() {
        mImageResIds = new int[]{R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e};
        mContentTitles = new String[]{
                "巩俐不低俗，我就不能低俗",
                "扑树又回来啦！再唱经典老歌引万人大合唱",
                "揭秘北京电影如何升级",
                "乐视网TV版大派送",
                "热血屌丝的反杀"
        };

        mImageList = new ArrayList<>();
        ImageView imageView;
        View pointView;
        LinearLayout.LayoutParams layoutParams;

        for (int i = 0; i < mImageResIds.length; i++) {
            imageView = new ImageView(this);
            imageView.setImageResource(mImageResIds[i]);
            mImageList.add(imageView);

            // 加小白点指示器
            pointView = new View(this);
            pointView.setBackgroundResource(R.drawable.selector_bg_point);
            layoutParams = new LinearLayout.LayoutParams(5, 5);
            if (i != 0) { // 设置左边距
                layoutParams.leftMargin = 10;
            }

            // 设置默认所有都不可用
            pointView.setEnabled(false);
            ll_point_container.addView(pointView, layoutParams);
        }
    }

    private void initView() {
        vp_main = (ViewPager) findViewById(R.id.vp_main);
        vp_main.addOnPageChangeListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        ll_point_container = (LinearLayout) findViewById(R.id.ll_point_container);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        // 计算位置
        int newPosition = position % mImageList.size();

        //设置文本
        tv_title.setText(mContentTitles[newPosition]);

        // 把之前的禁用, 把最新的启用, 更新指示器
        //for (int i = 0; i < ll_point_container.getChildCount(); i++) {
        //  View childAt = ll_point_container.getChildAt(position);
        //  childAt.setEnabled(position == i);
        //}
        ll_point_container.getChildAt(previousSelectedPosition).setEnabled(false);
        ll_point_container.getChildAt(newPosition).setEnabled(true);

        // 记录之前的位置
        previousSelectedPosition = newPosition;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class MyViewPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            int newPosition = position % mImageList.size();

            ImageView imageView = mImageList.get(newPosition);
            // 把View对象添加到container中
            container.addView(imageView);
            // 把View对象返回给框架, 适配器
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
    }
}
