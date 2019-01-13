package com.cheweibao.liuliu.wxapi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cheweibao.liuliu.R;

public class DemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //在服务端签名
        //假装请求了服务器 获取到了所有的数据
//        WXPayUtils.WXPayBuilder builder = new WXPayUtils.WXPayBuilder();
//        builder.setAppId("123")
//                .setPartnerId("56465")
//                .setPrepayId("41515")
//                .setPackageValue("5153")
//                .setNonceStr("5645")
//                .setTimeStamp("56512")
//                .setSign("54615")
//                .build().toWXPayNotSign(DemoActivity.this, "123");
//        //在客户端签名
//        //假装请求了服务端信息，并获取了appid、partnerId、prepayId
//        WXPayUtils.WXPayBuilder mbuilder = new WXPayUtils.WXPayBuilder();
//        mbuilder.setAppId("123")
//                .setPartnerId("213")
//                .setPrepayId("3213")
//                .setPackageValue("Sign=WXPay")
//                .build()
//                .toWXPayAndSign(DemoActivity.this, "123", "key");
    }
}
