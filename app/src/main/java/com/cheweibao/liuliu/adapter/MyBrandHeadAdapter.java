package com.cheweibao.liuliu.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.common.MyGlobal;
import com.cheweibao.liuliu.data.BrandHeadInfo;

import java.util.List;

/**
 * Created by sjk on 2017/9/19.
 */

public class MyBrandHeadAdapter extends RecyclerView.Adapter<MyBrandHeadAdapter.MyBrandHeadViewHolder> {
    private List<BrandHeadInfo> list;

    public MyBrandHeadAdapter(List list) {
        this.list = list;

    }

    @Override
    public MyBrandHeadViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        MyBrandHeadViewHolder holder = new MyBrandHeadViewHolder(LayoutInflater.from(MyGlobal.context)
                .inflate(R.layout.brand_head_view_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyBrandHeadViewHolder holder, final int position) {
        Glide.with(MyGlobal.context).load(list.get(position).getImg()).centerCrop().into(holder.imageView);
        holder.textView.setText(list.get(position).getText());
        holder.linBrandHeadView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyBrandHeadViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textView;
        private LinearLayout linBrandHeadView;

        public MyBrandHeadViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.head_item_img);
            textView = (TextView) view.findViewById(R.id.head_item_tv);
            linBrandHeadView = (LinearLayout) view.findViewById(R.id.lin_brand_headview);


        }


    }

    public interface OnItemClickListener {
        void onClick(View v, int position);
    }

    public OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {            //创建构造函数
        this.listener = listener;
    }


}
