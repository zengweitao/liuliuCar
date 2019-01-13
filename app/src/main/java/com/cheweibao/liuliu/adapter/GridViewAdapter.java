package com.cheweibao.liuliu.adapter;

import android.content.Context;
import android.graphics.Picture;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.data.BrandInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by unknow on 2018/5/30.
 */

public class GridViewAdapter extends BaseAdapter {
    private Context context;
    private List<BrandInfo> pictures;

    public GridViewAdapter(Context context, List<BrandInfo> pictures) {
        this.context = context;
        this.pictures = pictures;
    }

    @Override
    public int getCount() {
        if (null != pictures) {
            return pictures.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return pictures.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_gv_brand, null);
            holder.iv = (ImageView) convertView.findViewById(R.id.iv_item);
            holder.tv = (TextView) convertView.findViewById(R.id.tv_item);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        //设置显示图片
        holder.iv.setImageResource(Integer.parseInt(pictures.get(position).getLogo()));
        //设置标题
        holder.tv.setText(pictures.get(position).getName());
        return convertView;
    }

    class Holder {
        ImageView iv;
        TextView tv;
    }
}