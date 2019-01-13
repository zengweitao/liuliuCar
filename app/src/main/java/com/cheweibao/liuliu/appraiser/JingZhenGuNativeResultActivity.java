package com.cheweibao.liuliu.appraiser;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.common.BaseActivity;
import com.cheweibao.liuliu.common.MyGlobal;
import com.cheweibao.liuliu.common.PrefUtils;
import com.cheweibao.liuliu.event.ResultBackEvent;
import com.cheweibao.liuliu.ui.ClickEffectImageView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by user on 2018/4/11.
 */
public class JingZhenGuNativeResultActivity extends BaseActivity {
    @Bind(R.id.tvTopTitle)
    TextView tvTopTitle;
    @Bind(R.id.ivTopBack)
    ClickEffectImageView ivTopBack;
    @Bind(R.id.tablayout)
    TabLayout tablayout;
    @Bind(R.id.viewpager)
    ViewPager viewpager;
    @Bind(R.id.tv_native_result_des_top)
    TextView tvNativeResultDesTop;
    @Bind(R.id.tv_native_result_des_up)
    TextView tvNativeResultDesUp;
    @Bind(R.id.img_native_result)
    ImageView imgNativeResult;
    @Bind(R.id.tv_result_continue)
    TextView tvResultContinue;

    private List<String> mTitleList = new ArrayList<>();//页卡标题集合
    private Intent intent;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jingzhengu_nativeresult);
        ButterKnife.bind(this);
        intent = getIntent();
        initView();

    }

    private void initView() {
        String logo = PrefUtils.getString(MyGlobal.context, "logo", "");
        if (!TextUtils.isEmpty(logo)) {
            Glide.with(JingZhenGuNativeResultActivity.this).load(logo).into(imgNativeResult);
        }

        bundle = intent.getExtras();
        String trimId = bundle.getString("trimId");
        String buyCarDate = bundle.getString("buyCarDate");
        String mileage = bundle.getString("mileage");
        String cityId = bundle.getString("cityId");
        String city = bundle.getString("city");
        String type = bundle.getString("type");

        String[] split = buyCarDate.split("-");
        tvNativeResultDesTop.setText(type);
        tvNativeResultDesUp.setText(split[0] + "  " + mileage + "万公里 " + city);


        tvTopTitle.setText("评估结果");
        //添加页卡标题
        mTitleList.add("车商报价");
        mTitleList.add("个人报价");

        ViewpagerAdapter mAdapter = new ViewpagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(mAdapter);
        tablayout.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        tablayout.addTab(tablayout.newTab().setText(mTitleList.get(0)));//添加tab选项卡
        tablayout.addTab(tablayout.newTab().setText(mTitleList.get(1)));

        tablayout.setupWithViewPager(viewpager);//将TabLayout和ViewPager关联起来。
        tablayout.setTabsFromPagerAdapter(mAdapter);//给Tabs设置适配器

        tvResultContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new ResultBackEvent());
                finish();

            }
        });
    }

    @OnClick(R.id.ivTopBack)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivTopBack:
                finish();
                break;

        }
    }



    class ViewpagerAdapter extends FragmentPagerAdapter {

        public ViewpagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment ft = null;
            switch (position) {
                case 0:
                    ft = CarDealerOfferFragment.newInstance();
                    ft.setArguments(bundle);
                    break;
                case 1:
                    ft = PersonalOfferFragment.newInstance();
                    ft.setArguments(bundle);
                    break;

            }
            return ft;
        }

        @Override
        public int getCount() {
            return 2;
        }

        //此方法用来显示tab上的名字
        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position);
        }
    }
}
