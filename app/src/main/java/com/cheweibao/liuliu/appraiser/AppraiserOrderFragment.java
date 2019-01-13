package com.cheweibao.liuliu.appraiser;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.common.BaseFragment;
import com.cheweibao.liuliu.common.MyConstants;
import com.cheweibao.liuliu.common.Utils;
import com.cheweibao.liuliu.data.MessageEvent;
import com.cheweibao.liuliu.data.PjOrderListInfo;
import com.cheweibao.liuliu.net.ServerUrl;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 主界面-订单
 */
public class AppraiserOrderFragment extends BaseFragment {

    int type = 0; // 0-评估订单 1-历史订单
    @Bind(R.id.tv_search)
    EditText tvSearch;
    @Bind(R.id.lv_list)
    PullToRefreshListView lvList;
    @Bind(R.id.ll_empty)
    LinearLayout llEmpty;
    @Bind(R.id.rl_waiting)
    RelativeLayout rlWaiting;

    ListView actualListView;
    int pageNum = 0;
    boolean isMore = false;

    ArrayList<PjOrderListInfo> arrItems;
    ListAdapter adapter;

    String keyStr = "";

    public static AppraiserOrderFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt("type", type);
        AppraiserOrderFragment fragment = new AppraiserOrderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle args = getArguments();
        type = args.getInt("type");

        View view = inflater.inflate(R.layout.fragment_order, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        if (type == 0) {
            tvSearch.setVisibility(View.GONE);
        }
        actualListView = lvList.getRefreshableView();
        lvList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                RefreshData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (isMore) {
                    GetMoreData();
                } else {
                    Utils.postRefreshComplete(lvList);
                    shortToast("没有更多数据～～");
                }
            }
        });

        actualListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                if (pos < 1 || pos > arrItems.size()) return;
                Intent it = new Intent(mContext, com.cheweibao.liuliu.appraiser.AppraiserPjOrderDetailActivity.class);
                it.putExtra("orderId", arrItems.get(pos - 1).pjdId);
                startActivity(it);
            }
        });

        tvSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                keyStr = tvSearch.getText().toString().trim();
                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(tvSearch.getWindowToken(), 0);
                RefreshData();
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (arrItems == null || arrItems.size() < 1) {
            arrItems = new ArrayList<>();
            RefreshData();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    private void RefreshData() {
        if (getThread_flag()) return;
        setThread_flag(true);
        isMore = false;
        pageNum = 0;

        arrItems.clear();
        getArrItems();
    }

    private void GetMoreData() {
        setThread_flag(true);
        pageNum++;
        getArrItems();

    }

    public void getArrItems() {

        if (ServerUrl.isNetworkConnected(mContext)) {
            try {
                HashMap<String, String> fields = new HashMap<String, String>();
                fields.put("token", myglobal.user.token);
                fields.put("keyStr", keyStr);
                fields.put("pageNum", pageNum + "");
                fields.put("type", type + "");
                postMap(ServerUrl.getAppraiserOrderList, fields, getArrsHandler);

            } catch (Exception e) {
                setThread_flag(false);
                e.printStackTrace();
            }
        } else {
            setThread_flag(false);
            shortToast("网络连接不可用");
        }
    }

    private Handler getArrsHandler = new Handler() {
        public void handleMessage(Message msg) {
            setThread_flag(false);
            Utils.postRefreshComplete(lvList);
            switch (msg.what) {
                case 0:
                    HashMap<String, Object> result = (HashMap<String, Object>) msg.obj;
                    try {
                        String status = result.get("result_code") + "";
                        if ("200".equals(status)) {
                            Gson gson = new Gson();
                            JSONObject data = JSON.parseObject(gson.toJson(result.get("data")));
                            JSONArray list = data.getJSONArray("list");

                            for (int i = 0; i < list.size(); i++) {
                                arrItems.add(new PjOrderListInfo((JSONObject) list.get(i)));
                            }
                            if (list.size() >= MyConstants.DEFAUT_PAGE_SIZE)
                                isMore = true;
                            else
                                isMore = false;

                            if (pageNum == 0 && type == 0) {
                                String count = data.getString("total");
                                MessageEvent event = new MessageEvent(MyConstants.ORDER_COUNT);
                                event.putExtra("orderCount", count);
                                postEvent(event);
                            }
                            setAdapter();
                        }else if("401".equals(status)){
                            String message = result.get("message") + "";
                            shortToast(message);
                            goLoginPage();
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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (arrItems == null || arrItems.size() < 1)
                    llEmpty.setVisibility(View.VISIBLE);
                else
                    llEmpty.setVisibility(View.GONE);
                if (adapter == null) {
                    adapter = new ListAdapter();
                    actualListView.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
            }
        }, 500);

    }

    private class ListAdapter extends BaseAdapter {

        LayoutInflater mInflater;

        public ListAdapter() {
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return arrItems.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                if (type == 0)
                    convertView = mInflater.inflate(R.layout.layout_agent_order_item, null);
                else
                    convertView = mInflater.inflate(R.layout.layout_agent_order_js_item, null);

                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tvAppriseTime.setVisibility(View.GONE);
            holder.tvOrderType.setVisibility(View.GONE);

            PjOrderListInfo info = arrItems.get(position);
            holder.tvOrderNo.setText("订单编号：" + info.pjdNo);
            holder.tvBorrower.setText("机构名称：" + info.jgName);
            holder.tvCarType.setText("车辆型号：" + info.carTypeName);

            holder.tvTime.setText("发起时间：" + info.createTime);
            if ("0.00".equals(info.carPrePrice))
                holder.tvCarPrice.setText("评估价格：无");
            else
                holder.tvCarPrice.setText("评估价格：" + Utils.getFormatedPrice(info.carPrePrice));

            return convertView;
        }
    }


    class ViewHolder {
        @Bind(R.id.tv_order_no)
        TextView tvOrderNo;
        @Bind(R.id.tv_borrower)
        TextView tvBorrower;
        @Bind(R.id.tv_car_type)
        TextView tvCarType;
        @Bind(R.id.tv_order_type)
        TextView tvOrderType;
        @Bind(R.id.tv_time)
        TextView tvTime;
        @Bind(R.id.tv_car_price)
        TextView tvCarPrice;
        @Bind(R.id.tv_apprise_time)
        TextView tvAppriseTime;
        @Bind(R.id.tv_status)
        TextView tvStatus;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.isMsgOf(MyConstants.APPRAISER_SET_PRICE_SUCCESS)) {
            RefreshData();
        }
    }
}
