package com.cheweibao.liuliu.agent;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.common.BaseActivity;
import com.cheweibao.liuliu.common.MyConstants;
import com.cheweibao.liuliu.data.BrandInfo;
import com.cheweibao.liuliu.event.SelectCarTypeEvent;
import com.cheweibao.liuliu.net.ServerUrl;
import com.cheweibao.liuliu.ui.QuickIndexBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 2018/4/19.
 */

public class BrandListReleaseActivity extends BaseActivity {
    @Bind(R.id.listview)
    ListView listview;
    @Bind(R.id.quickbar)
    QuickIndexBar quickbar;
    @Bind(R.id.showLabel)
    TextView showLabel;

    ArrayList<BrandInfo> brandList;
    @Bind(R.id.tvTopTitle)
    TextView tvTopTitle;
    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_brand_release_list);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        initViews();
    }

    private void initViews() {
        tvTopTitle.setText("选择车辆品牌");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (brandList == null || brandList.size() < 1) {
            brandList = new ArrayList<>();
            getInfo();
        }
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void getInfo() {
        if (ServerUrl.isNetworkConnected(mContext)) {
            try {
                HashMap<String, String> fields = new HashMap<String, String>();
                postMap(ServerUrl.getBrandList, fields, handler);
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

                            if(list != null) {
                                for (int i = 0; i < list.size(); i++) {
                                    brandList.add(new BrandInfo((JSONObject) list.get(i)));
                                }
                            }

                            setAdapter();
                        } else {
                            String message = result.get("message") + "";
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
        mAdapter = new MyAdapter();
        listview.setAdapter(mAdapter);

        quickbar.setIndexChangedListener(new QuickIndexBar.IndexChangedListener() {
            @Override
            public void indexChanged(String word) {
                showIndexLabel(word);
                for (int i = 0; i < brandList.size(); i++) {
                    String indexWord = brandList.get(i).initial;
                    if (indexWord.equals(word)) {
                        listview.setSelection(i);
                        break;
                    }
                }
            }
        });
    }

    private Handler mHandler = new Handler();

    private void showIndexLabel(String word) {
        showLabel.setVisibility(View.VISIBLE);
        showLabel.setText(word);

        mHandler.removeCallbacksAndMessages(null); //移除所有消息队列
        mHandler.postDelayed(new Runnable() {      //发送延时消息
            @Override
            public void run() {
                showLabel.setVisibility(View.GONE);
            }
        }, 1200);
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
            final BrandInfo item = getItem(position);
            holder.nameTextView.setText(item.name);
            Glide.with(mContext)
                    .load(item.logo)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.ivLogo);
            String currIndexWord = item.initial;

            if (position > 0) {
                String lastIndexWord = getItem(position - 1).initial;
                if (lastIndexWord.equals(currIndexWord)) {
                    holder.indexTextView.setVisibility(View.GONE);
                } else {
                    holder.indexTextView.setVisibility(View.VISIBLE);
                    holder.indexTextView.setText(currIndexWord);
                }
            } else {
                holder.indexTextView.setVisibility(View.VISIBLE);
                holder.indexTextView.setText(currIndexWord);
            }

            holder.ivLogo.setVisibility(View.VISIBLE);

            holder.llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent it = new Intent(mContext, com.cheweibao.liuliu.agent.CarTypeListActivity.class);
                    it.putExtra("parentid", item.id);
                    startActivity(it);
                }
            });


            return convertView;
        }

        private ViewHolder getHolder(View convertView) {
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            if (viewHolder == null) {
                viewHolder = new ViewHolder();
                viewHolder.llItem = (LinearLayout) convertView.findViewById(R.id.ll_item);
                viewHolder.indexTextView = (TextView) convertView.findViewById(R.id.index);
                viewHolder.nameTextView = (TextView) convertView.findViewById(R.id.name);
                viewHolder.ivLogo = (ImageView) convertView.findViewById(R.id.iv_logo);

                convertView.setTag(viewHolder);
            }


            return viewHolder;
        }

        private ViewHolder holder;

        class ViewHolder {
            LinearLayout llItem;
            TextView indexTextView;
            TextView nameTextView;
            ImageView ivLogo;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SelectCarTypeEvent event) {
        if(event.isMsgOf(MyConstants.SEL_CAR_NAME))
            finish();
    }

}
