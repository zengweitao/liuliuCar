package com.cheweibao.liuliu.agent;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.adapter.ConstellationStringAdapter;
import com.cheweibao.liuliu.adapter.VCRecyclerViewAdapter;
import com.cheweibao.liuliu.common.BaseActivity;
import com.cheweibao.liuliu.data.ConfigInfo;
import com.cheweibao.liuliu.net.ServerUrl;
import com.cheweibao.liuliu.ui.BaseGridView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VeConListActivity extends BaseActivity {

    @Bind(R.id.tvTopTitle)
    TextView tvTopTitle;
    @Bind(R.id.iv_title)
    ImageView ivTitle;
    @Bind(R.id.rv_list_basics)
    ListView rvList;
    @Bind(R.id.gv_configure)
    BaseGridView gvConfigure;
    @Bind(R.id.view_car_configure)
    View viewCarConfigure;
    @Bind(R.id.view_configure)
    View viewConfigure;
    private VCRecyclerViewAdapter recyclerAdapter;
    private ConstellationStringAdapter configureAdapter;
    List<ConfigInfo> list = new ArrayList<>();
    private String sourcesId = "";
    private int top = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_configure_list);
        ButterKnife.bind(this);
        sourcesId = getIntent().getStringExtra("sourcesId");
        initViews();

        getModelDetail();
    }

    private void initViews() {
        tvTopTitle.setText("详细配置");
        viewConfigure.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    @OnClick({R.id.ivTopBack, R.id.ll_title})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivTopBack:
                finish();
                break;
            case R.id.ll_title:
                if (viewCarConfigure.getVisibility() == View.GONE) {
                    ivTitle.setImageResource(R.drawable.ic_up);
                    viewCarConfigure.setVisibility(View.VISIBLE);
                } else {
                    ivTitle.setImageResource(R.drawable.ic_down);
                    viewCarConfigure.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void getModelDetail() {
        if (ServerUrl.isNetworkConnected(mContext)) {
            try {
                showProgress();
                HashMap<String, String> fields = new HashMap<String, String>();
                fields.put("sourcesId", sourcesId);
                postMap(ServerUrl.getModelConfigList, fields, handler);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (mContext != null)
                shortToast("网络连接不可用");
        }
    }

    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 0:
                    @SuppressWarnings("unchecked")
                    HashMap<String, Object> result = (HashMap<String, Object>) msg.obj;
                    try {
                        String status = result.get("code") + "";
                        if ("0".equals(status)) {
                            Gson gson = new Gson();
                            JSONArray data = JSON.parseArray(gson.toJson(result.get("result")));
                            String strdata = data.toJSONString();
                            Type type = new TypeToken<List<ConfigInfo>>() {
                            }.getType();
                            list = gson.fromJson(strdata, type);
                            setView();
                        } else {
                            if (mContext != null) {
                                String message = result.get("desc") + "";
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
            hideProgress();
        }

    };

    private void setView() {
        recyclerAdapter = new VCRecyclerViewAdapter(mContext, list);
        rvList.setAdapter(recyclerAdapter);
        List<String> mlist = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            mlist.add(list.get(i).getName());
        }
        configureAdapter = new ConstellationStringAdapter(this, mlist);
        gvConfigure.setAdapter(configureAdapter);

        gvConfigure.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                configureAdapter.setCheckItem(position);
                ivTitle.setImageResource(R.drawable.ic_down);
                viewCarConfigure.setVisibility(View.GONE);
                //移动到某行
                rvList.setSelection(position);
            }
        });
    }
}
