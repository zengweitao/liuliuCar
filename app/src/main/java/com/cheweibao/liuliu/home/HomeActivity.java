package com.cheweibao.liuliu.home;

import android.os.Bundle;

import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.common.BaseActivity;

import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
    }


}
