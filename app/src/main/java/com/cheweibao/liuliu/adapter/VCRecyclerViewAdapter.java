package com.cheweibao.liuliu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.common.MyGlobal;
import com.cheweibao.liuliu.common.Utils;
import com.cheweibao.liuliu.data.ConfigInfo;
import com.cheweibao.liuliu.ui.BaseListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sjk on 2017/9/19.
 */

public class VCRecyclerViewAdapter extends BaseAdapter {
    private List<ConfigInfo> list;
    private Context context;

    public VCRecyclerViewAdapter(Context context, List list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        if (null != list) {
            return list.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        Holder holder;
        if (view == null) {
            holder = new Holder();
            view = LayoutInflater.from(context).inflate(R.layout.item_vecon_list, null);
            holder.tvName = (TextView) view.findViewById(R.id.tv_name);
            holder.rvList = (BaseListView) view.findViewById(R.id.rv_list_item);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        VehicleConfigurationAdapter basicsAdapter = new VehicleConfigurationAdapter(context, list.get(position).getParam());
        holder.rvList.setAdapter(basicsAdapter);
        Utils.setListViewHeight(holder.rvList);
        holder.tvName.setText(list.get(position).getName());
        return view;
    }

    class Holder {
        private TextView tvName;
        private BaseListView rvList;
    }
}
