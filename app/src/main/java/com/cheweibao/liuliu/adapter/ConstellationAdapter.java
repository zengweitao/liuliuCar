package com.cheweibao.liuliu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cheweibao.liuliu.R;

import java.util.ArrayList;
import java.util.List;


public class ConstellationAdapter extends BaseAdapter {

    private Context context;
    private List<Integer> mlist = new ArrayList<>();
    private int checkItemPosition = 0;

    public void setCheckItem(int position) {
        checkItemPosition = position;
        notifyDataSetChanged();
    }

    public ConstellationAdapter(Context context, List<Integer> list) {
        this.context = context;
        this.mlist = list;
    }

    @Override
    public int getCount() {
        return mlist.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView != null) {
            holder = (Holder) convertView.getTag();
        } else {
            holder = new Holder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_constellation_layout, null);
            holder.mText = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        }
        fillValue(position, holder);
        return convertView;
    }

    private void fillValue(int position, Holder viewHolder) {
        Integer str = mlist.get(position);
        viewHolder.mText.setText(context.getResources().getString(str));
        if (checkItemPosition != -1) {
            if (checkItemPosition == position) {
                viewHolder.mText.setTextColor(context.getResources().getColor(R.color.tv_green));
                viewHolder.mText.setBackgroundResource(R.drawable.check_bg);
            } else {
                viewHolder.mText.setTextColor(context.getResources().getColor(R.color.gray));
                viewHolder.mText.setBackgroundResource(R.drawable.uncheck_bg);
            }
        }
    }

    class Holder {
        TextView mText;
    }
}
