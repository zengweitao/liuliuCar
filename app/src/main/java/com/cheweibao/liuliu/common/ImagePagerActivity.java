package com.cheweibao.liuliu.common;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.ui.HackyViewPager;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 图片
 */
public class ImagePagerActivity extends BaseActivity {
    private static final String STATE_POSITION = "STATE_POSITION";
    public static final String EXTRA_IMAGE_INDEX = "image_index";
    public static final String EXTRA_IMAGE_URLS = "image_urls";
    public static final String EXTRA_THUMBNAIL_URLS = "thumbnail_urls";
    public static final String FILE_FLAG = "file_flag";
    @Bind(R.id.ivTopBack)
    ImageView ivTopBack;

    private HackyViewPager mPager;
    private TextView indicator;
    private boolean flag = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_pager);
        ButterKnife.bind(this);

        int pagerPosition = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
        flag = getIntent().getBooleanExtra(FILE_FLAG, false);
        ArrayList<String> urls = getIntent().getStringArrayListExtra(EXTRA_IMAGE_URLS);
        ArrayList<String> thumbnails = getIntent().getStringArrayListExtra(EXTRA_THUMBNAIL_URLS);

        mPager = (HackyViewPager) findViewById(R.id.pager);
        ImagePagerAdapter mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), urls, thumbnails);
        mPager.setAdapter(mAdapter);
        indicator = (TextView) findViewById(R.id.indicator);

        CharSequence text = getString(R.string.viewpager_indicator, 1, mPager.getAdapter().getCount());
        indicator.setText(text);
        // 更新下标
        mPager.addOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int arg0) {
                CharSequence text = getString(R.string.viewpager_indicator, arg0 + 1, mPager.getAdapter().getCount());
                indicator.setText(text);
            }

        });
        if (savedInstanceState != null) {
            pagerPosition = savedInstanceState.getInt(STATE_POSITION);
        }

        mPager.setCurrentItem(pagerPosition);
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_POSITION, mPager.getCurrentItem());
    }

    @OnClick(R.id.ivTopBack)
    public void onViewClicked() {
        finish();
    }


    private class ImagePagerAdapter extends FragmentStatePagerAdapter {

        public ArrayList<String> fileList;
        public ArrayList<String> thumbnails;

        public ImagePagerAdapter(FragmentManager fm, ArrayList<String> fileList, ArrayList<String> thumbnails) {
            super(fm);
            this.fileList = fileList;
            this.thumbnails = thumbnails;
        }

        @Override
        public int getCount() {
            return fileList == null ? 0 : fileList.size();
        }

        @Override
        public Fragment getItem(int position) {
            String url;
            String thumbnail = null;
            if (flag) {
                url = "file://" + fileList.get(position);
            } else {
                url = fileList.get(position);
                if (thumbnails != null) {
                    thumbnail = thumbnails.get(position);
                }
            }
            return ImageDetailsFragment.newInstance(url, thumbnail);
        }

    }




}
