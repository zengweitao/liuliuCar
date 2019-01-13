package com.cheweibao.liuliu.common;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.adapter.MyAdapter;
import com.cheweibao.liuliu.data.CheckCarData;
import com.cheweibao.liuliu.main.MainActivity;


public class MyBaseDialog extends Dialog {
    Context mContext = null;

    Button btnOk = null;
    Button btnCancel = null;
    LinearLayout lobtnOk = null;
    LinearLayout lobtnCancel = null;
    String type = "question2";

    public int radioValue;
    public int defaultYear;

    private String title = "";
    private String content = "";
    private String okName = "";
    private String cancelName = "";

    public String[] arrayKind = null;


    MyAdapter myAdapter;

    private View.OnClickListener mOkListener = null;
    private View.OnClickListener mCancelListener = null;
    private AnimationDrawable anim;

    public MyBaseDialog(Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
    }

    public MyBaseDialog(Context context, int theme, String dlgType, String title) {

        super(context, theme);
        this.mContext = context;
        this.title = title;
        this.type = dlgType;
    }

    public MyBaseDialog(Context context, int theme, String dlgType, String content, View.OnClickListener okListner) {

        super(context, theme);
        this.mContext = context;
        this.content = content;
        this.type = dlgType;
        this.mOkListener = okListner;
    }

    public MyBaseDialog(Context context, int theme, String dlgType, String[] arrKind,
                        View.OnClickListener okListner, View.OnClickListener cancelListner) {

        super(context, theme);
        this.mOkListener = okListner;
        this.mCancelListener = cancelListner;
        this.mContext = context;
        this.arrayKind = arrKind;
        this.type = dlgType;
    }

    public MyBaseDialog(Context context, int theme, String dlgType, String dlgTitle, String dlgContent,
                        View.OnClickListener okListner, View.OnClickListener cancelListner) {

        super(context, theme);
        this.mContext = context;
        this.mOkListener = okListner;
        this.mCancelListener = cancelListner;
        this.type = dlgType;
        this.title = dlgTitle;
        this.content = dlgContent;
        this.okName = "确 定";
        this.cancelName = "取 消";
    }

    public MyBaseDialog(Context context, int theme, String dlgType, String dlgTitle, String dlgContent, String dlgButOk, String dlgButCancel,
                        View.OnClickListener okListner, View.OnClickListener cancelListner) {

        super(context, theme);
        this.mContext = context;
        this.mOkListener = okListner;
        this.mCancelListener = cancelListner;
        this.type = dlgType;
        this.title = dlgTitle;
        this.content = dlgContent;
        this.okName = dlgButOk;
        this.cancelName = dlgButCancel;
    }

    public MyBaseDialog(Context context, int theme, String dlgType, MyAdapter adapter,
                        View.OnClickListener okListner) {

        super(context, theme);
        this.mContext = context;
        this.mOkListener = okListner;
        this.type = dlgType;
        this.myAdapter = adapter;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.5f;
        getWindow().setAttributes(lpWindow);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


        if (type.equals("dlgAlert")) {
            setContentView(R.layout.dlg_alert);
            lobtnOk = (LinearLayout) findViewById(R.id.btn_confirm_ok);
            lobtnOk.setOnClickListener(mOkListener);

            TextView tvContent = (TextView) findViewById(R.id.tvContent);
            tvContent.setText(content);
        } else if (type.equals("dlgConfirm")) {
            setContentView(R.layout.dlg_confirm);
            lobtnOk = (LinearLayout) findViewById(R.id.btn_confirm_ok);
            lobtnOk.setOnClickListener(mOkListener);
            lobtnCancel = (LinearLayout) findViewById(R.id.btn_confirm_no);
            lobtnCancel.setOnClickListener(mCancelListener);

            TextView tvContent = (TextView) findViewById(R.id.tvContent);
            tvContent.setText(content);
            TextView tvConfirm_ok = (TextView) findViewById(R.id.tvConfirm_ok);
            tvConfirm_ok.setText(okName);
            TextView tvConfirm_cancel = (TextView) findViewById(R.id.tvConfirm_cancel);
            tvConfirm_cancel.setText(cancelName);
        } else if (type.equals("dlgSex")) {  //选择性别
            setContentView(R.layout.dlg_sex_item);
            RelativeLayout rloDlgBack = (RelativeLayout) findViewById(R.id.rloDlgBack);
            rloDlgBack.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View arg0, MotionEvent arg1) {
                    dismiss();
                    return false;
                }
            });

            LinearLayout loMale = (LinearLayout) findViewById(R.id.lobtn_Male);
            LinearLayout loFamale = (LinearLayout) findViewById(R.id.lobtn_Famale);
            loMale.setOnClickListener(mOkListener);
            loFamale.setOnClickListener(mCancelListener);

            if (content.equals("1")) {
                findViewById(R.id.ivCheckFamale).setSelected(false);
                findViewById(R.id.ivCheckMale).setSelected(true);
            } else {
                findViewById(R.id.ivCheckFamale).setSelected(true);
                findViewById(R.id.ivCheckMale).setSelected(false);
            }

        } else if (type.equals("dlgProgress")) {

            setContentView(R.layout.waiting_box);
            TextView tvWaiting = (TextView) findViewById(R.id.tvWaiting);
            ImageView ivGif = (ImageView) findViewById(R.id.ivGif);
            ivGif.setImageResource(R.drawable.loading);
            anim = (AnimationDrawable) ivGif.getDrawable();
            if (anim != null) {
                anim.start();
            }
//			SpinKitView spinKitView = (SpinKitView)findViewById(R.id.spin_kit);
//			Sprite drawable = SpriteFactory.create(Style.FADING_CIRCLE);
//			spinKitView.setIndeterminateDrawable(drawable);
//    	    tvWaiting.setText(title);
        } else if (type.equals("chkDlg")) {
            setOnKeyListener(new OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                    if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            dismiss();
                            return false;
                        }
                    }
                    return false;
                }
            });

            setContentView(R.layout.dialog_layout);
            ListView listView = (ListView) findViewById(R.id.list);
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

            if ("1".equals(CheckCarData.checkAll)) {
                findViewById(R.id.btn_save).setVisibility(View.GONE);
            }

            findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOkListener.onClick(view);
                }
            });
//
//			LinearLayout llcontent = (LinearLayout) findViewById(R.id.ll_content);
//			LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) llcontent.getLayoutParams();
//			params.height = MyGlobal.getInstance().SCR_HEIGHT;
//			llcontent.setLayoutParams(params);
            listView.setAdapter(myAdapter);
        }


    }

    private RadioGroup.OnCheckedChangeListener radioGroupChangeListener = new RadioGroup.OnCheckedChangeListener() {
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            // TODO Auto-generated method stub
            radioValue = checkedId;
        }
    };

    @Override
    public void dismiss() {
        super.dismiss();
        if (anim != null) {
            anim.stop();
        }
    }
}
