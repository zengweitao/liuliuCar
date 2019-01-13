package com.cheweibao.liuliu.appraiser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.agent.AgentPjOrderDetailActivity;
import com.cheweibao.liuliu.common.BaseActivity;
import com.cheweibao.liuliu.net.ServerUrl;
import com.cheweibao.liuliu.popup.SelectAreaPopWindow;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 123456 on 2018/3/11.
 */

public class JingZhenGuResultActivity extends BaseActivity {

    @Bind((R.id.web_view))
    WebView webView;
    private SelectAreaPopWindow selectAreaPopWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jingzhengu_result);
        ButterKnife.bind(this);

        WebSettings s = webView.getSettings();
        s.setBuiltInZoomControls(true);
        s.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        s.setUseWideViewPort(true);
        s.setLoadWithOverviewMode(true);
        s.setSavePassword(true);
        s.setSaveFormData(true);
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setDefaultTextEncodingName("utf-8");
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                // Activities and WebViews measure progress with different scales.
                // The progress meter will automatically disappear when we reach 100%
                JingZhenGuResultActivity.this.setProgress(progress * 1000);
            }
        });
        //Map<String,String> carInfo = (Map<String, String>) savedInstanceState.getSerializable("carInfo");
        //步骤3. 复写shouldOverrideUrlLoading()方法，使得打开网页时不调用系统浏览器， 而是在本WebView中显示
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        String url = ServerUrl.appraisalCarHtml;
        Intent intent = getIntent();
        Map<String,String> carInfo = (Map<String, String>) intent.getSerializableExtra("carInfo");

        if(carInfo != null){
            Set<String> keys = carInfo.keySet();
            List<String> k = new ArrayList<>();
            for (String key:keys){
                try {
                    k.add(key + "=" + URLEncoder.encode(new String(carInfo.get(key)), "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            String params =StringUtils.join(k.toArray(),"&");
            url += "?" + params;
        }

        webView.loadUrl(url);
    }

    @OnClick({R.id.btn_next,R.id.ivTopBack})
    public void onClick(View view) {
        Intent it;
        switch (view.getId()) {
            case R.id.ivTopBack:
                if (selectAreaPopWindow != null && selectAreaPopWindow.isShowing()) {
                    selectAreaPopWindow.dismiss();
                }
                finish();
                break;
            case R.id.btn_next:
                it = new Intent(mContext, AgentPjOrderDetailActivity.class);
                startActivity(it);
                break;
        }
    }


}
