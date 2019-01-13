package com.cheweibao.liuliu.checker;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.common.BaseActivity;
import com.cheweibao.liuliu.common.BaseFragment;
import com.cheweibao.liuliu.common.MyConstants;
import com.cheweibao.liuliu.data.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CheckerOrderActivity extends BaseActivity {
    @Bind(R.id.tv_waiting)
    TextView tvWaiting;
    @Bind(R.id.tv_finished)
    TextView tvFinished;
    @Bind(R.id.tv_order_count)
    TextView tvOrderCount;

    @Bind(R.id.fragment_container)
    FrameLayout fragmentContainer;

    private FragmentTransaction ft;
    BaseFragment waitingFragment = null;
    BaseFragment finishedFragment = null;

    int mSelType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_order);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
    }

    private void initView() {
        tvWaiting.setText("检测订单");
        switchContainer(0);
    }

    private void switchContainer(int type) {
        mSelType = type;
        if (type == 0) {
            tvWaiting.setSelected(true);
            tvFinished.setSelected(false);
        } else {
            tvWaiting.setSelected(false);
            tvFinished.setSelected(true);
        }
        setSelect(type);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        ButterKnife.unbind(this);
    }

    @SuppressLint("CommitTransaction")
    public void setSelect(int i) {

        FragmentManager fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        hideFragments();
        switch (i) {
            case 0:
                if (waitingFragment == null) {
                    waitingFragment = CheckerOrderFragment.newInstance(i);
                    ft.add(R.id.fragment_container, waitingFragment);
                }
                ft.show(waitingFragment);
                break;
            case 1:
                if (finishedFragment == null) {
                    finishedFragment = CheckerOrderFragment.newInstance(i);
                    ft.add(R.id.fragment_container, finishedFragment);
                }
                ft.show(finishedFragment);
                break;
        }
        ft.commit();
    }

    private void hideFragments() {
        if (waitingFragment != null) {
            ft.hide(waitingFragment);
        }
        if (finishedFragment != null) {
            ft.hide(finishedFragment);
        }
    }

    @OnClick({R.id.tv_waiting, R.id.tv_finished})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_waiting:
                switchContainer(0);
                break;
            case R.id.tv_finished:
                switchContainer(1);
                break;
        }
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if(event.isMsgOf(MyConstants.ORDER_COUNT)){
            String count = event.getStringExtra("orderCount");
            tvOrderCount.setText(count);
            if("0".equals(count)){
                tvOrderCount.setVisibility(View.GONE);
            }else{
                tvOrderCount.setVisibility(View.VISIBLE);
            }

        }
    }
}
