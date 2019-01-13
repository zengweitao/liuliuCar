package com.cheweibao.liuliu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.common.MyGlobal;
import com.cheweibao.liuliu.data.ConfigInfo;
import com.cheweibao.liuliu.data.VehicleConfigurationInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sjk on 2017/9/19.
 */

public class VehicleConfigurationAdapter extends BaseAdapter {
    private List<VehicleConfigurationInfo> list = new ArrayList<>();
    private Context context;

    public VehicleConfigurationAdapter(Context context, List list) {
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
            view = LayoutInflater.from(context).inflate(R.layout.item_rv_vehicle_configuration, null);
            holder.tvName = (TextView) view.findViewById(R.id.tv_name);
            holder.tvContent = (TextView) view.findViewById(R.id.tv_content);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        holder.tvName.setText(list.get(position).getParamKey());
        holder.tvContent.setText(list.get(position).getParamValue());
        return view;
    }

    class Holder {
        private TextView tvName;
        private TextView tvContent;
    }
}
