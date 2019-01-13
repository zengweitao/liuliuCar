package com.cheweibao.liuliu.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.agent.InfoCarActivity;
import com.cheweibao.liuliu.common.MyGlobal;
import com.cheweibao.liuliu.common.Utils;
import com.cheweibao.liuliu.data.CarModelInfo;

import java.util.List;


public class ChooseCarAdapter extends BaseAdapter {

    private Context mContext;
    private List<CarModelInfo> mList;

    public ChooseCarAdapter(Context mContext, List<CarModelInfo> mList) {
        this.mContext = mContext;
        this.mList = mList;
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
    public CarModelInfo getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Holder holder;
        final CarModelInfo info = mList.get(position);
        if (convertView == null) {
            holder = new Holder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_choose_car, null);
            holder.img_car = (ImageView) convertView.findViewById(R.id.img_car);
            holder.tv_car_name = (TextView) convertView.findViewById(R.id.tv_car_name);
            holder.tv_car_min_price = (TextView) convertView.findViewById(R.id.tv_car_min_price);
            holder.tv_car_min_down_payments = (TextView) convertView.findViewById(R.id.tv_car_min_down_payments);
            holder.tv_car_min_down_payments_unit = (TextView) convertView.findViewById(R.id.tv_car_min_down_payments_unit);
            holder.tv_car_monthly_supply = (TextView) convertView.findViewById(R.id.tv_car_monthly_supply);
            holder.ll_choose_car = (View) convertView.findViewById(R.id.ll_choose_car);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        if (!TextUtils.isEmpty(info.getBanner())) {
            Glide.with(MyGlobal.context).load(info.getBanner()).centerCrop().into(holder.img_car);
        } else if (!TextUtils.isEmpty(info.getCarImage())) {
            Glide.with(MyGlobal.context).load(info.getCarImage()).centerCrop().into(holder.img_car);
        }
        holder.tv_car_name.setText(info.getName());
        holder.tv_car_min_price.setText("新车指导价" + info.getGuidePrice() + "万");
        holder.tv_car_min_down_payments.setText("" + info.getDownPayments());
        holder.tv_car_min_down_payments_unit.setText("万");
        if (!TextUtils.isEmpty(info.getMonthPay())) {
            holder.tv_car_monthly_supply.setText("月供" + Utils.getPrice(info.getMonthPay()));
        }
        return convertView;
    }

    class Holder {
        public View ll_choose_car;
        public ImageView img_car;
        public TextView tv_car_name;
        public TextView tv_car_min_price;
        public TextView tv_car_min_down_payments;
        public TextView tv_car_min_down_payments_unit;
        public TextView tv_car_monthly_supply;
    }
}
