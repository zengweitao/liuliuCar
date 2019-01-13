package com.cheweibao.liuliu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.common.MyBaseDialog;
import com.cheweibao.liuliu.common.MyConstants;
import com.cheweibao.liuliu.common.MyGlobal;
import com.cheweibao.liuliu.common.ToastUtil;
import com.cheweibao.liuliu.data.BrandInfo;
import com.cheweibao.liuliu.data.MessageEvent;
import com.cheweibao.liuliu.net.ServerUrl;
import com.wangli.FinalHttp;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by user on 2018/4/3.
 */

public class CarDialog extends Dialog {
    // 18606713255   123456aA
    private ArrayList<BrandInfo> brandCarList = new ArrayList<>();
    private String id;
    private String name;
    private String headName;
    private FinalHttp finalHttp;
    private MyAdapter mAdapter;
    private Context context;
    private ListView carListView;
    private LinearLayout linCarEmpty;
    private MyBaseDialog progress = null;
    private TextView tvPersonBiaoTi;
    private LinearLayout linBiaoTi;
    private View view;

    public CarDialog(@NonNull Context context, int style, String id, String name) {
        super(context, style);
        setContentView(R.layout.dialog_car);
        this.id = id;
        this.headName = name;
        this.context = context;
        finalHttp = FinalHttp.getInstance();
        initView();
    }

    private void initView() {
        view = findViewById(R.id.person_view);
        linBiaoTi = (LinearLayout) findViewById(R.id.lin_person_type_biaoti);
        tvPersonBiaoTi = (TextView) findViewById(R.id.tv_person_type_biaoti);
        carListView = (ListView) findViewById(R.id.car_listview);
        linCarEmpty = (LinearLayout) findViewById(R.id.ll_car_empty);
        findViewById(R.id.img_person_type_biaoti).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        if (ServerUrl.isNetworkConnected(context)) {
            try {

                HashMap<String, String> fields = new HashMap<String, String>();
                fields.put("parentid", id);
                finalHttp.postMap(ServerUrl.getCarList, fields, handler);
                showProgress();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ToastUtil.showToast("网络连接不可用");
        }

    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    HashMap<String, Object> result = (HashMap<String, Object>) msg.obj;
                    try {
                        String status = result.get("result_code") + "";
                        if ("200".equals(status)) {
                            Gson gson = new Gson();
                            JSONObject data = JSON.parseObject(gson.toJson(result.get("data")));
                            JSONArray list = data.getJSONArray("list");
                            name = data.getString("fullname");
                            if (list != null) {
                                for (int i = 0; i < list.size(); i++) {
                                    brandCarList.add(new BrandInfo((JSONObject) list.get(i)));
                                }
                            }
                            setAdapter();
                        } else {
                            String message = result.get("message") + "";
                            ToastUtil.showToast(message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtil.showToast("抱歉数据异常");
                    }
                    break;
                default:
                    ToastUtil.showToast("网络不给力!");
                    break;
            }
        }
    };

    private void setAdapter() {
        if (brandCarList.size() < 1) {
            linCarEmpty.setVisibility(View.VISIBLE);
        } else {
            linBiaoTi.setVisibility(View.VISIBLE);
            view.setVisibility(View.VISIBLE);
            tvPersonBiaoTi.setText(headName);
            linCarEmpty.setVisibility(View.GONE);
            mAdapter = new MyAdapter();
            carListView.setAdapter(mAdapter);
        }
        hideProgress();
    }


    private class MyAdapter extends ArrayAdapter<BrandInfo> {

        public MyAdapter() {
            super(context, R.layout.layout_car_item, brandCarList);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(MyGlobal.context, R.layout.layout_car_item, null);
            }
            holder = getHolder(convertView);
            BrandInfo item = getItem(position);
            holder.nameTextView.setText(item.name);

            holder.llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MessageEvent event = new MessageEvent(MyConstants.SEL_CAR_NAME);
                    event.putExtra("carName", brandCarList.get(position).name);
                    event.putExtra("carId", brandCarList.get(position).id);
                    event.putExtra("fullName", name);
                    event.putExtra("carYear", brandCarList.get(position).yeartype);
                    EventBus.getDefault().post(event);
                    dismiss();

                }
            });
            return convertView;
        }

        private ViewHolder getHolder(View convertView) {
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            if (viewHolder == null) {
                viewHolder = new ViewHolder();
                viewHolder.nameTextView = (TextView) convertView.findViewById(R.id.tv_car_item);
                viewHolder.llItem = (LinearLayout) convertView.findViewById(R.id.lin_car_item);
                convertView.setTag(viewHolder);
            }


            return viewHolder;
        }

        private ViewHolder holder;

        class ViewHolder {
            TextView nameTextView;
            LinearLayout llItem;

        }
    }


    public void showProgress() {
        if (context == null) return;

        try {
            progress = new MyBaseDialog(context, R.style.Theme_Transparent, "dlgProgress", "请稍等!");
            progress.show();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public void hideProgress() {
        try {
            if (progress != null) {
                progress.dismiss();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
