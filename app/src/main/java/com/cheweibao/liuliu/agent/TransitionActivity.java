package com.cheweibao.liuliu.agent;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.common.BaseActivity;
import com.cheweibao.liuliu.common.ButCommonUtils;
import com.cheweibao.liuliu.examine.ToExamineOneActivity;

import com.cheweibao.liuliu.wxapi.WXPayEntryActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TransitionActivity extends BaseActivity {

    Context context;
    @Bind(R.id.iv_transition)
    ImageView ivTransition;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_content)
    TextView tvContent;
    @Bind(R.id.tv_btn)
    TextView tvBtn;
    @Bind(R.id.tv_back)
    TextView tvBack;
    int style = 0;//0 无网络,1 审核中,2 审核通过,3 审核拒绝,4 审核退回,5 付款成功,6 付款失败,7无订单
    String message = "";
    String id = "";

    /****/

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition);
        style = getIntent().getIntExtra("style", 0);
        message = getIntent().getStringExtra("desc");
        id = getIntent().getStringExtra("id");
        ButterKnife.bind(this);
        context = this;
        Intent intent = new Intent();
        intent.putExtra("rePay", false);
        setResult(RESULT_REPAY, intent);
        initView();
    }

    private void initView() {
        switch (style) {
            //0 无网络,1 审核中,2 审核通过,3 审核拒绝,4 审核退回,5 付款成功,6 付款失败
            case 0:
                ivTransition.setImageResource(R.drawable.ic_no_net);
                tvTitle.setVisibility(View.GONE);
                tvContent.setText(getResources().getString(R.string.tv_network_link_failure));
                tvBtn.setText(getResources().getString(R.string.tv_refresh));
                break;
            case 1:
                ivTransition.setImageResource(R.drawable.ic_in_review);
                tvTitle.setVisibility(View.GONE);
                tvContent.setText(getResources().getString(R.string.tv_in_review));
                tvBtn.setText(getResources().getString(R.string.tv_got_it));
                break;
            case 2:
                ivTransition.setImageResource(R.drawable.ic_no_net);
                tvTitle.setText(Html.fromHtml(getResources().getString(R.string.tv_review_pass)));
                tvContent.setText(Html.fromHtml(getResources().getString(R.string.tv_review_pass_content)));
                tvBtn.setText(getResources().getString(R.string.tv_review_pass_btn));
                break;
            case 3:
                ivTransition.setImageResource(R.drawable.ic_audit_rejection);
                tvTitle.setText(Html.fromHtml(getResources().getString(R.string.tv_audit_rejection)));
                tvContent.setText(getResources().getString(R.string.tv_audit_rejection_content));
                tvBtn.setText(getResources().getString(R.string.tv_got_it));
                break;
            case 4:
                ivTransition.setImageResource(R.drawable.ic_send_back);
                tvTitle.setText(Html.fromHtml(getResources().getString(R.string.tv_send_back)));
                if (TextUtils.isEmpty(id)) {
                    String str = getResources().getString(R.string.tv_send_back_content) + "\n";
                    SpannableString spannableString = new SpannableString(str + message);
                    spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF44AF35")), str.length(), str.length() + message.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    tvContent.setText(spannableString);
                    tvBtn.setText(getResources().getString(R.string.tv_audit_rejection_btn));
                } else {
                    tvContent.setText(message);
                    tvBtn.setText(getResources().getString(R.string.tv_rechoose_car));
                }
                break;
            case 5:
                ivTransition.setImageResource(R.drawable.ic_payment_success);
                tvTitle.setText(getResources().getString(R.string.tv_payment_success));
                tvContent.setText(getResources().getString(R.string.tv_payment_success_content));
                tvBtn.setText(getResources().getString(R.string.tv_got_it));
                break;
            case 6:
                ivTransition.setImageResource(R.drawable.ic_failure_payment);
                tvTitle.setText(getResources().getString(R.string.tv_failure_payment));
                tvContent.setText(getResources().getString(R.string.tv_failure_payment_content));
                tvBtn.setText(getResources().getString(R.string.tv_failure_payment_btn));
                tvBack.setVisibility(View.VISIBLE);
                break;
            case 7:
                ivTransition.setImageResource(R.drawable.ic_list_none);
                tvContent.setText(getResources().getString(R.string.tv_no_order));
                tvContent.setTextSize(22);
                tvContent.setTextColor(getResources().getColor(R.color.tv_gray));
                tvTitle.setVisibility(View.GONE);
                tvBtn.setText(getResources().getString(R.string.tv_got_it));
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick({R.id.tv_btn, R.id.tv_back})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_btn:
                switch (style) {
                    //0 无网络,1 审核中,2 审核通过,3 审核拒绝,4 审核退回,5 付款成功,6 付款失败
                    case 0:
                    case 1:
                    case 3:
                    case 5:
                    case 7:
                        finish();
                        break;
                    case 2:
                        if (!ButCommonUtils.isFastDoubleClick()) {
                            intent = new Intent(mContext, WXPayEntryActivity.class);
                            intent.putExtra("style", 1);
                            startActivity(intent);
                            finish();
                        }
                        break;
                    case 6:
                        if (!ButCommonUtils.isFastDoubleClick()) {
                            intent = new Intent();
                            intent.putExtra("rePay", true);
                            setResult(RESULT_REPAY, intent);
                            finish();
                        }
                        break;
                    case 4:
                        if (!ButCommonUtils.isFastDoubleClick()) {
                            if (TextUtils.isEmpty(id)) {
                                intent = new Intent(mContext, ToExamineOneActivity.class);
                                intent.putExtra("style", 1);
                                startActivity(intent);
                            } else {
                                backtv("",0);
                            }
                            finish();
                        }
                        break;
                }
                break;
            case R.id.tv_back:
                if (!ButCommonUtils.isFastDoubleClick()) {
                    finish();
                }
                break;
        }
    }
}
