package com.cheweibao.liuliu.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CommonArrayAdapter extends BaseAdapter {

    private final static String TAG = "CommonArrayAdapter";

    private Activity mActivity;
    private String[] mStr;

    public CommonArrayAdapter(Activity activity, String[] str) {
        mActivity = activity;
        mStr = str;
    }

    public int getCount() {
        return mStr.length;
    }

    public Object getItem(int position) {
        return mStr[position];
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        return new MyAdapterView(mActivity, mStr[position]);
    }

    class MyAdapterView extends LinearLayout {
        public static final String LOG_TAG = "MyAdapterView";

        public MyAdapterView(Context context, String str) {
            super(context);
            this.setOrientation(HORIZONTAL);

            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 3, 10, 10);
            params.gravity = Gravity.CENTER_HORIZONTAL;

            TextView name = new TextView(context);
            name.setText(str);
            name.setTextSize(17);
            addView(name, params);
        }
    }
}
