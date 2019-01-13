package com.cheweibao.liuliu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.common.Utils;
import com.cheweibao.liuliu.data.BrandInfo;
import com.cheweibao.liuliu.data.DefaultInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by unknow on 2018/5/30.
 */

public class GridViewDefaultAdapter extends BaseAdapter {
    private Context context;
    private int style;
    private List<DefaultInfo> pictures;

    public GridViewDefaultAdapter(Context context, List<DefaultInfo> pictures, int style) {
        this.context = context;
        this.style = style;
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
            switch (style) {
                case 0:
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_gv_default, null);
                    holder.iv = (TextView) convertView.findViewById(R.id.iv_item);
                    break;
                case 1:
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_gv_default_line, null);
                    break;
            }
            holder.tv = (TextView) convertView.findViewById(R.id.tv_name);
            holder.content = (TextView) convertView.findViewById(R.id.tv_content);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        //设置标题
        holder.tv.setText(pictures.get(position).getName());
        switch (style) {
            case 0:
                holder.iv.setText(position + 1 + "");
                holder.tv.setTextColor(context.getResources().getColor(R.color.tv_green));
                break;
            case 1:
                holder.tv.setTextColor(context.getResources().getColor(R.color.tv_black));
                break;
        }
        holder.content.setText(pictures.get(position).getContent());
        return convertView;
    }

    class Holder {
        TextView iv;
        TextView tv;
        TextView content;
    }
}