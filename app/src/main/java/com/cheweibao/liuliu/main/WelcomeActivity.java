package com.cheweibao.liuliu.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.common.BaseActivity;
import com.cheweibao.liuliu.common.ToastUtil;
import com.cheweibao.liuliu.common.Utils;

import java.util.ArrayList;

/*
 * --------------功能：引导页----------------
 */

public class WelcomeActivity extends BaseActivity implements OnClickListener {

    private ViewPager viewPager;
    private ViewPagerAdapter vpAdapter;

    private ArrayList<View> views;

    private static final int[] pics = {R.drawable.guide1, R.drawable.guide2, R.drawable.guide3, R.drawable.guide4};

    public boolean isFinish = false;
    private int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        initView();
        initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toMain:
            case R.id.pass:
                next_page();
                break;
            default:
                break;
        }
    }

    private void next_page() {
        Utils.saveCurrentVersion(this);
        Utils.putBooleanPreferences(mContext, "isfir", false);
        Intent it = new Intent(this, MainActivity.class);
        startActivity(it);
        finish();
    }


    private void initView() {
        views = new ArrayList<View>();
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        vpAdapter = new ViewPagerAdapter(views);
    }

    private void initData() {
        LayoutInflater inflater = getLayoutInflater();

        for (int i = 0; i < pics.length; i++) {
            View v = inflater.inflate(R.layout.layout_welcome_item, null);
            ImageView image = (ImageView) v.findViewById(R.id.welcome_item_image);
            Button toMain = (Button) v.findViewById(R.id.toMain);
            TextView pass = (TextView) v.findViewById(R.id.pass);

            if (i == pics.length - 1) {
                toMain.setVisibility(View.VISIBLE);
                toMain.setOnClickListener(this);
                pass.setVisibility(View.GONE);
            } else {
                toMain.setVisibility(View.GONE);
                pass.setVisibility(View.VISIBLE);
                pass.setOnClickListener(this);
            }
            image.setImageResource(pics[i]);
            views.add(v);
        }

        viewPager.setAdapter(vpAdapter);
        viewPager.setOnPageChangeListener(new pageListener());

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (isFinish) {
                    finish();
                } else {
//                    Toast.makeText(mContext, "再按一次退出" + getResources().getString(R.string.app_name),
//                            Toast.LENGTH_SHORT).show();
                    ToastUtil.showToast("再按一次退出" + getResources().getString(R.string.app_name));
                    isFinish = true;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isFinish = false;
                        }
                    }, 3000);
                }
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    private class pageListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            if (position == 2) {
                //loadPageFormat();
            }
        }

    }

    private void setCurView(int position) {
        if (position < 0 || position >= pics.length) {
            return;
        }
        viewPager.setCurrentItem(position);
    }


    public class ViewPagerAdapter extends PagerAdapter {

        private ArrayList<View> views;

        public ViewPagerAdapter(ArrayList<View> views) {
            this.views = views;
        }

        @Override
        public int getCount() {
            if (views != null) {
                return views.size();
            }
            return 0;
        }

        @Override
        public Object instantiateItem(View view, int position) {

            ((ViewPager) view).addView(views.get(position), 0);

            return views.get(position);
        }

        @Override
        public boolean isViewFromObject(View view, Object arg1) {
            return (view == arg1);
        }

        @Override
        public void destroyItem(View view, int position, Object arg2) {
            ((ViewPager) view).removeView(views.get(position));
        }
    }

}
