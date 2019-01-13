package com.cheweibao.liuliu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.common.MyGlobal;
import com.cheweibao.liuliu.common.Utils;
import com.cheweibao.liuliu.data.BrandHeadInfo;
import com.cheweibao.liuliu.data.BrandInfo;
import com.cheweibao.liuliu.data.FinanceInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sjk on 2017/9/19.
 */

public class ProgrammeAdapter extends BaseAdapter {
    private List<FinanceInfo> list = new ArrayList<>();
    private Context context;
    private int checkItemPosition = 0;

    public ProgrammeAdapter(Context context, List list) {
        this.list = list;
        this.context = context;
    }

    public void setCheckItem(int position) {
        checkItemPosition = position;
        notifyDataSetChanged();
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
            view = LayoutInflater.from(context).inflate(R.layout.item_gv_staging_scheme, null);
            holder.isChoose = (RelativeLayout) view.findViewById(R.id.is_choose);
            holder.first_payment = (TextView) view.findViewById(R.id.tv_first_payment);
            holder.first_payment_unit = (TextView) view.findViewById(R.id.tv_first_payment_unit);
            holder.bond = (TextView) view.findViewById(R.id.tv_bond);
            holder.bond_unit = (TextView) view.findViewById(R.id.tv_bond_unit);
            holder.monthly_supply = (TextView) view.findViewById(R.id.tv_monthly_supply);
            holder.monthly_supply_unit = (TextView) view.findViewById(R.id.tv_monthly_supply_unit);
            holder.monthly_supply_month = (TextView) view.findViewById(R.id.tv_monthly_supply_month);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        holder.first_payment.setText(list.get(position).getDownPayments());
        holder.first_payment_unit.setText("万");
        holder.bond.setText(list.get(position).getBond());
        holder.bond_unit.setText("元");
        holder.monthly_supply.setText(list.get(position).getMonthPay());
        holder.monthly_supply_unit.setText("元");
        holder.monthly_supply_month.setText("月供(" + list.get(position).getTerm() + "月)");
        if (checkItemPosition != -1) {
            if (checkItemPosition == position) {
                holder.isChoose.setVisibility(View.VISIBLE);
            } else {
                holder.isChoose.setVisibility(View.GONE);
            }
        }
        return view;
    }

    class Holder {
        private RelativeLayout isChoose;
        private TextView first_payment;
        private TextView first_payment_unit;
        private TextView bond;
        private TextView bond_unit;
        private TextView monthly_supply;
        private TextView monthly_supply_unit;
        private TextView monthly_supply_month;
    }
}
