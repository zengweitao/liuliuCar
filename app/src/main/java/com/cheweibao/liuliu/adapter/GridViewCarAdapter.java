package com.cheweibao.liuliu.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.common.MyGlobal;
import com.cheweibao.liuliu.common.Utils;
import com.cheweibao.liuliu.data.BrandInfo;
import com.cheweibao.liuliu.data.CarModelInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by unknow on 2018/5/30.
 */

public class GridViewCarAdapter extends BaseAdapter {
    private Context context;
    private GridView mGv;
    private List<CarModelInfo> pictures;
    private static int ROW_NUMBER = 3;

    public GridViewCarAdapter(Context context, GridView gv, List<CarModelInfo> pictures) {
        this.context = context;
        this.mGv = gv;
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
        CarModelInfo info = pictures.get(position);
        if (convertView == null) {
            holder = new Holder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_gv_car, null);
            holder.iv = (ImageView) convertView.findViewById(R.id.iv_item);
            holder.tv_name_item = (TextView) convertView.findViewById(R.id.tv_name_item);
            holder.tv_first_item = (TextView) convertView.findViewById(R.id.tv_first_item);
            holder.tv_first_item_unit = (TextView) convertView.findViewById(R.id.tv_first_item_unit);
            holder.tv_month_item = (TextView) convertView.findViewById(R.id.tv_month_item);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        //设置显示图片
        if (!TextUtils.isEmpty(info.getBanner())) {
            Glide.with(MyGlobal.context).load(info.getBanner()).centerCrop().into(holder.iv);
        } else if (!TextUtils.isEmpty(info.getCarImage())) {
            Glide.with(MyGlobal.context).load(info.getCarImage()).centerCrop().into(holder.iv);
        }
        holder.tv_name_item.setText(info.getCarTypeName());//名字
        holder.tv_first_item.setText(info.getDownPayments());//首付
        holder.tv_first_item_unit.setText("万");//首付
//        holder.tv_first_item.setText(Double.parseDouble(info.getDownPayments()) / 10000 + "");//首付
        holder.tv_month_item.setText("月供" + info.getMonthPay() + "元");//月供
        return convertView;
    }

    class Holder {
        ImageView iv;
        TextView tv_name_item;
        TextView tv_first_item;
        TextView tv_first_item_unit;
        TextView tv_month_item;
    }
}