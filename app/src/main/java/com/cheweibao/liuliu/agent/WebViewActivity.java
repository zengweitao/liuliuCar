package com.cheweibao.liuliu.agent;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.common.BaseActivity;
import com.cheweibao.liuliu.common.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 123456 on 2018/3/11.
 */

public class WebViewActivity extends BaseActivity {

    @Bind((R.id.web_view))
    WebView webView;
    String url = "";
    @Bind(R.id.tvTopTitle)
    TextView tvTopTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);
        tvTopTitle.setText(getResources().getString(R.string.app_name));
        int state = getIntent().getIntExtra("state", 0);
        switch (state) {
            case 0:
                initWebView();
                break;
            default:
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
                        WebViewActivity.this.setProgress(progress * 1000);
                    }
                });
                //Map<String,String> carInfo = (Map<String, String>) savedInstanceState.getSerializable("carInfo");
                //步骤3. 复写shouldOverrideUrlLoading()方法，使得打开网页时不调用系统浏览器， 而是在本WebView中显示
                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;
                    }
                });
                url = getIntent().getStringExtra("url");
//        Intent intent = getIntent();
//        Map<String, String> carInfo = (Map<String, String>) intent.getSerializableExtra("carInfo");
//
//        if (carInfo != null) {
//            Set<String> keys = carInfo.keySet();
//            List<String> k = new ArrayList<>();
//            for (String key : keys) {
//                try {
//                    k.add(key + "=" + URLEncoder.encode(new String(carInfo.get(key)), "utf-8"));
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//            }
//            String params = StringUtils.join(k.toArray(), "&");
//            url += "?" + params;
//        }
                webView.loadUrl(url);
                break;

        }
    }

    private void initWebView() {
        tvTopTitle.setText("溜溜好车平台用户服务协议");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY); //取消滚动条白边效果
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                // Activities and WebViews measure progress with different scales.
                // The progress meter will automatically disappear when we reach 100%
                WebViewActivity.this.setProgress(progress * 1000);
            }
        });
        //Map<String,String> carInfo = (Map<String, String>) savedInstanceState.getSerializable("carInfo");
        //步骤3. 复写shouldOverrideUrlLoading()方法，使得打开网页时不调用系统浏览器， 而是在本WebView中显示
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.getSettings().setDefaultTextEncodingName("UTF-8");
        webView.getSettings().setBlockNetworkImage(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(webView.getSettings().MIXED_CONTENT_ALWAYS_ALLOW);  //注意安卓5.0以上的权限
        }
        url = Utils.readAssetsTxt(mContext, "registration_agreement");
        webView.loadDataWithBaseURL(null, url, "text/html", "UTF-8", null);
    }

    @OnClick({R.id.ivTopBack})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivTopBack:
                finish();
                break;
        }
    }


}
