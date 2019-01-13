package com.cheweibao.liuliu.agent;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.adapter.ChooseCarAdapter;
import com.cheweibao.liuliu.common.BaseActivity;
import com.cheweibao.liuliu.common.MyConstants;
import com.cheweibao.liuliu.common.Utils;
import com.cheweibao.liuliu.data.AppCarSelfListInfo;
import com.cheweibao.liuliu.data.CarModelInfo;
import com.cheweibao.liuliu.db.DBHelper;
import com.cheweibao.liuliu.net.ServerUrl;
import com.cheweibao.liuliu.ui.MyEditText;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity {

    Context context;
    @Bind(R.id.ll_top)
    LinearLayout llTop;
    @Bind(R.id.fl_history)
    TagFlowLayout flHistory;
    @Bind(R.id.fl_hot)
    TagFlowLayout flHot;
    @Bind(R.id.et_search)
    MyEditText etSearch;
    @Bind(R.id.ll_hot)
    LinearLayout llHot;
    @Bind(R.id.ll_history)
    RelativeLayout llHistory;
    @Bind(R.id.ll_choose)
    LinearLayout llChoose;
    @Bind(R.id.rv_choose_cars)
    ListView rvChooseCars;
    @Bind(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @Bind(R.id.is_none)
    RelativeLayout isNone;
    private List<String> strings = new ArrayList<>();
    private List<String> hotstrings = new ArrayList<>();
    //布局管理器
    private LayoutInflater mInflater;
    //流式布局的子布局
    private TextView tv;
    private TextView hottv;
    private SQLiteDatabase db;
    private ChooseCarAdapter choosearadapter;
    private List<CarModelInfo> list = new ArrayList<>();
    private int pageNum = 1;
    private int pageSize = 20;
    private String keyOrder = "";//排序规则：car_price_htol、car_price_ltoh、down_payments_htol、down_payments_ltoh、month_pay_htol、month_pay_ltoh
    private String name = "";
    private String downPaymentArry = "";
    private String monthPayArray = "";
    private String[] arrText = new String[]{
            "大众", "宝马", "奔驰",
            "奥迪", "丰田", "本田", "福特", "别克",
    };
    private int isUsed = 0;

    /****/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        context = this;
        isUsed = getIntent().getIntExtra("isUsed", 0);
        helper = DBHelper.getInstance(myglobal.getInstance());
        initView();
//        setInfo();

        // 第一次进入查询所有的历史记录
        queryData();
        if (strings.size() == 0) {
            llHistory.setVisibility(View.GONE);
        } else {
            llHistory.setVisibility(View.VISIBLE);
            //通知handler更新UI
            handler.sendEmptyMessageDelayed(1, 0);
        }
    }

//    public void backtv(String tv) {
//        if (!hasData(tv)) {
//            insertData(tv);
//            queryData();
//            //通知handler更新UI
//            if (strings.size() == 0) {
//                llHistory.setVisibility(View.GONE);
//            } else {
//                llHistory.setVisibility(View.VISIBLE);
//                //通知handler更新UI
//                handler.sendEmptyMessageDelayed(1, 0);
//            }
//        }
//        Intent it = new Intent(MyConstants.SEARCH);
//        it.putExtra("name", tv);
//        LocalBroadcastManager.getInstance(mContext).sendBroadcast(it);
//        finish();
//    }

    private void setInfo() {
        keyOrder = getIntent().getStringExtra("keyOrder");//排序规则：car_price_htol、car_price_ltoh、down_payments_htol、down_payments_ltoh、month_pay_htol、month_pay_ltoh
        downPaymentArry = getIntent().getStringExtra("downPaymentArry");
        monthPayArray = getIntent().getStringExtra("monthPayArray");
        name = getIntent().getStringExtra("name");
        if (!TextUtils.isEmpty(name)) {
            etSearch.setText(name);
            getAppCarSelfList(pageNum, pageSize, keyOrder, name, downPaymentArry, monthPayArray);
        }
    }

    private void initView() {
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                list.clear();
                pageNum = 1;
                pageSize = 20;
                getAppCarSelfList(pageNum, pageSize, keyOrder, name, downPaymentArry, monthPayArray);
                refreshlayout.finishRefresh(2000);
                refreshLayout.setEnableLoadmore(true);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                pageNum++;
                pageSize = 10;
                getAppCarSelfList(pageNum, pageSize, keyOrder, name, downPaymentArry, monthPayArray);
                refreshlayout.finishLoadmore(2000);
            }
        });

        mInflater = LayoutInflater.from(this);
        Utils.setEditTextInhibitInputSpeChat(etSearch);
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == 3) {
                    // 先隐藏键盘
                    String aa = etSearch.getText().toString().trim();
                    name = aa;
                    pageNum = 1;
                    pageSize = 20;
                    if (list.size() > 0) {
                        list.clear();
                    }
//                    getAppCarSelfList(pageNum, pageSize, keyOrder, name, downPaymentArry, monthPayArray);
                    //搜索相应标签 若查询有数据进行记录
                    if (!"".equals(aa)) {
//                        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
//                                getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        hideKeybrond();
//                        if (!hasData(aa)) {
//                            insertData(aa);
//                            queryData();
//                            //通知handler更新UI
//                            if (strings.size() == 0) {
//                                llHistory.setVisibility(View.GONE);
//                            } else {
//                                llHistory.setVisibility(View.VISIBLE);
//                                //通知handler更新UI
//                                handler.sendEmptyMessageDelayed(1, 0);
//                            }
//                        }
                    }
                    backtv(name, isUsed);
                    finish();
                    return true;
                }
                return false;

            }

        });
        //流式布局tag的点击方法
        flHistory.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                //搜索相应标签
                name = strings.get(position);
                etSearch.setText(name);
                pageNum = 1;
                pageSize = 20;
                if (list.size() > 0) {
                    list.clear();
                }
                backtv(name, isUsed);
                finish();
//                getAppCarSelfList(pageNum, pageSize, keyOrder, name, downPaymentArry, monthPayArray);
                return true;
            }
        });
        hotstrings = new ArrayList<>();
        for (int i = 0; i < arrText.length; i++) {
            hotstrings.add(arrText[i]);
        }
        flHot.setAdapter(new TagAdapter<String>(hotstrings) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                hottv = (TextView) mInflater.inflate(R.layout.fl_tv,
                        flHot, false);
                hottv.setText(s);
                return hottv;
            }
        });
        flHot.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                //搜索相应标签
                name = hotstrings.get(position);
                etSearch.setText(name);
                pageNum = 1;
                pageSize = 20;
                if (list.size() > 0) {
                    list.clear();
                }
                backtv(name, isUsed);
                finish();
//                getAppCarSelfList(pageNum, pageSize, keyOrder, name, downPaymentArry, monthPayArray);
                return true;
            }
        });
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    flHistory.setAdapter(new TagAdapter<String>(strings) {
                        @Override
                        public View getView(FlowLayout parent, int position, String s) {
                            tv = (TextView) mInflater.inflate(R.layout.fl_tv,
                                    flHistory, false);
                            tv.setText(s);
                            return tv;
                        }
                    });
                    break;
            }
            super.handleMessage(msg);
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.clear, R.id.delete_history})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clear:
                if (View.VISIBLE == isNone.getVisibility()) {
                    isNone.setVisibility(View.GONE);
                    etSearch.setText("");
                    choosearadapter.notifyDataSetChanged();
                    llChoose.setVisibility(View.VISIBLE);
                } else if (list.size() > 0) {
                    list.clear();
                    choosearadapter.notifyDataSetChanged();
                    llChoose.setVisibility(View.VISIBLE);
                } else {
                    finish();
                }
                break;
            case R.id.delete_history:
                deleteData();
                break;
        }
    }

//    /**
//     * 插入数据
//     */
//    private void insertData(String tempName) {
//        db = helper.getWritableDatabase();
//        db.beginTransaction();
//        try {
//            ContentValues cv = new ContentValues();
//            cv.put("name", tempName);
//            db.insert("searchHistory", null, cv);
//            db.setTransactionSuccessful();
//        } catch (Exception e) {
//        } finally {
//            db.endTransaction();
//        }
//    }

    /**
     * 模糊查询数据
     */
    private void queryData() {
        strings = new ArrayList<>();
        // 创建adapter适配器对象
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from searchHistory", null);
        while (cursor.moveToNext()) {
            strings.add(cursor.getString(cursor.getColumnIndex("name")));
        }
        Collections.reverse(strings);
        cursor.close();
        List<String> oldstrings = new ArrayList<>();
        oldstrings.addAll(strings);
        if (oldstrings.size() > hismax) {
            strings.clear();
            for (int j = 0; j < hismax; j++) {
                strings.add(oldstrings.get(j));
            }
        }
    }

//    /**
//     * 检查数据库中是否已经有该条记录
//     */
//    private boolean hasData(String tempName) {
//        Cursor cursor = helper.getReadableDatabase().rawQuery(
//                "select id as _id,name from searchHistory where name =?", new String[]{tempName});
//        //判断是否有下一个
//        if (cursor.moveToNext()) {
//            SQLiteDatabase db = helper.getReadableDatabase();
//            db.delete("searchHistory", "name =?", new String[]{tempName});
////            return cursor.moveToNext();
//        }
//        return false;
//    }

    /**
     * 清空数据
     */
    private void deleteData() {
        db = helper.getWritableDatabase();
        db.execSQL("delete from searchHistory");
        db.close();
        queryData();
        //通知handler更新UI
        if (strings.size() == 0) {
            llHistory.setVisibility(View.GONE);
        } else {
            llHistory.setVisibility(View.VISIBLE);
            //通知handler更新UI
            handler.sendEmptyMessageDelayed(1, 0);
        }
    }

    private void getAppCarSelfList(int pageNum, int pageSize, String keyOrder, String name, String downPaymentArry, String monthPayArray) {
        if (ServerUrl.isNetworkConnected(mContext)) {
            try {
                showProgress();
                HashMap<String, String> fields = new HashMap<String, String>();
                fields.put("pageNum", pageNum + "");
                fields.put("pageSize", pageSize + "");
                fields.put("keyOrder", keyOrder);
                fields.put("name", name);
                fields.put("downPaymentArry", downPaymentArry);
                fields.put("monthPayArray", monthPayArray);
                postMap(ServerUrl.getAppCarSelfList, fields, listhandler);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (mContext != null)
                shortToast("网络连接不可用");
        }
    }

    private Handler listhandler = new Handler() {

        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 0:
                    @SuppressWarnings("unchecked")
                    HashMap<String, Object> result = (HashMap<String, Object>) msg.obj;
                    try {
                        String status = result.get("code") + "";
                        if ("0".equals(status)) {
                            Gson gson = new Gson();
                            JSONObject data = JSON.parseObject(gson.toJson(result.get("result")));
                            String strdata = data.toJSONString();
                            AppCarSelfListInfo info = gson.fromJson(strdata, AppCarSelfListInfo.class);
                            list.addAll(info.getList());
                            if (info.getList().size() < pageSize) {
                                refreshLayout.setEnableLoadmore(false);
                            } else {
                                refreshLayout.setEnableLoadmore(true);
                            }
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
            refreshLayout.finishRefresh();
            refreshLayout.finishLoadmore();
        }

    };

    private void setView() {
        if (list.size() > 0) {
            llChoose.setVisibility(View.GONE);
            if (choosearadapter != null) {
                choosearadapter.notifyDataSetChanged();
            } else {
                choosearadapter = new ChooseCarAdapter(context, list);
                rvChooseCars.setAdapter(choosearadapter);
            }
            Utils.setListViewHeight(rvChooseCars);
            isNone.setVisibility(View.GONE);
        } else {
            isNone.setVisibility(View.VISIBLE);
        }
    }

}
