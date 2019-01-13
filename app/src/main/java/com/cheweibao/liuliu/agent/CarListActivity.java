package com.cheweibao.liuliu.agent;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.common.BaseActivity;
import com.cheweibao.liuliu.common.MyConstants;
import com.cheweibao.liuliu.data.BrandInfo;
import com.cheweibao.liuliu.event.SelectCarTypeEvent;
import com.cheweibao.liuliu.net.ServerUrl;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CarListActivity extends BaseActivity {

    @Bind(R.id.listview)
    ListView listview;

    ArrayList<BrandInfo> brandList;
    @Bind(R.id.tvTopTitle)
    TextView tvTopTitle;
    @Bind(R.id.ll_empty)
    LinearLayout llEmpty;
    private MyAdapter mAdapter;

    String parentid;
    String fullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_type_list);
        ButterKnife.bind(this);

        parentid = getIntent().getStringExtra("parentid");
        initViews();
    }

    private void initViews() {
        tvTopTitle.setText("选择车型");
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent it = new Intent(MyConstants.SEL_CAR_NAME);
//                it.putExtra("carName", brandList.get(position).name);
//                LocalBroadcastManager.getInstance(mContext).sendBroadcast(it);

                SelectCarTypeEvent event = new SelectCarTypeEvent(MyConstants.SEL_CAR_NAME);
                event.putExtra("carName", brandList.get(position).name);
                event.putExtra("carId",brandList.get(position).id);
                event.putExtra("fullName", fullName);
                event.putExtra("carYear", brandList.get(position).yeartype);
                EventBus.getDefault().post(event);

                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (brandList == null || brandList.size() < 1) {
            brandList = new ArrayList<>();
            getInfo();
        }
    }


    private void getInfo() {
        if (ServerUrl.isNetworkConnected(mContext)) {
            try {
                HashMap<String, String> fields = new HashMap<String, String>();
                fields.put("parentid", parentid);
                postMap(ServerUrl.getCarList, fields, handler);
                showProgress();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            shortToast("网络连接不可用");
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            hideProgress();
            switch (msg.what) {
                case 0:
                    HashMap<String, Object> result = (HashMap<String, Object>) msg.obj;
                    try {
                        String status = result.get("result_code") + "";
                        if ("200".equals(status)) {
                            Gson gson = new Gson();
                            JSONObject data = JSON.parseObject(gson.toJson(result.get("data")));
                            JSONArray list = data.getJSONArray("list");
                            fullName  = data.getString("fullname");
                            if(list != null) {
                                for (int i = 0; i < list.size(); i++) {
                                    brandList.add(new BrandInfo((JSONObject) list.get(i)));
                                }
                            }

                            setAdapter();
                        } else {
                            String message = result.get("desc") + "";
                            shortToast(message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        shortToast("抱歉数据异常");
                    }
                    break;
                default:
                    shortToast("网络不给力!");
                    break;
            }

        }

        ;
    };

    private void setAdapter() {
        if(brandList.size() < 1){
            llEmpty.setVisibility(View.VISIBLE);
        }else{
            llEmpty.setVisibility(View.GONE);
            mAdapter = new MyAdapter();
            listview.setAdapter(mAdapter);
        }
    }


    @OnClick({R.id.ivTopBack})
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.ivTopBack:
                finish();
                break;
        }
    }


    private class MyAdapter extends ArrayAdapter<BrandInfo> {

        public MyAdapter() {
            super(mContext, R.layout.layout_index_list_item, brandList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.layout_index_list_item, null);
            }
            holder = getHolder(convertView);
            BrandInfo item = getItem(position);
            holder.nameTextView.setText(item.name);
            holder.indexTextView.setVisibility(View.GONE);

            return convertView;
        }

        private ViewHolder getHolder(View convertView) {
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            if (viewHolder == null) {
                viewHolder = new ViewHolder();
                viewHolder.indexTextView = (TextView) convertView.findViewById(R.id.index);
                viewHolder.nameTextView = (TextView) convertView.findViewById(R.id.name);
                viewHolder.llItem = (LinearLayout) convertView.findViewById(R.id.ll_item);
                viewHolder.ivLogo = (ImageView) convertView.findViewById(R.id.iv_logo);
                convertView.setTag(viewHolder);
            }


            return viewHolder;
        }

        private ViewHolder holder;

        class ViewHolder {
            TextView indexTextView;
            TextView nameTextView;
            LinearLayout llItem;
            ImageView ivLogo;
        }
    }
}
