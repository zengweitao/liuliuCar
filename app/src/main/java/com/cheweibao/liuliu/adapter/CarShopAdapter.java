package com.cheweibao.liuliu.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.data.ShopInfo;

import java.util.List;

/**
 * Created by sjk on 2017/9/19.
 */

public class CarShopAdapter extends BaseAdapter {
    private List<ShopInfo> mList;
    private Context mContext;

    public CarShopAdapter(Context context, List list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        if (null != mList) {
            return mList.size();
        } else {
            return 0;
        }
    }

    @Override
    public ShopInfo getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Holder holder;
        final ShopInfo info = mList.get(position);
        if (convertView == null) {
            holder = new Holder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_rv_car_shop, null);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tvNear = (TextView) convertView.findViewById(R.id.tv_near);
            holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        if (position == 0) {
            holder.tvNear.setVisibility(View.VISIBLE);
        } else {
            holder.tvNear.setVisibility(View.GONE);
        }
        holder.tvName.setText(info.getChannleName());
        holder.tvContent.setText(info.getDetailAddress());
        return convertView;
    }

    class Holder {
        private TextView tvName;
        private TextView tvNear;
        private TextView tvContent;

    }
}
