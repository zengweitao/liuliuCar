package com.cheweibao.liuliu.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.common.Utils;
import com.cheweibao.liuliu.data.BrandInfo;
import com.cheweibao.liuliu.data.CarImgInfo;
import com.cheweibao.liuliu.data.IDPhotoInfo;
import com.cheweibao.liuliu.examine.ToExamineOneActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by unknow on 2018/5/30.
 */

public class UploadDataAdapter extends BaseAdapter {
    private ToExamineOneActivity context;
    private List<IDPhotoInfo> pictures;


    public UploadDataAdapter(ToExamineOneActivity context, List<IDPhotoInfo> info) {
        this.context = context;
        pictures = info;
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        Holder holder;
        IDPhotoInfo info = pictures.get(position);
        if (convertView == null) {
            holder = new Holder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_gv_upload, null);
            holder.iv = (ImageView) convertView.findViewById(R.id.iv_item);
            holder.img_clear = (ImageView) convertView.findViewById(R.id.img_clear);
            holder.tv = (TextView) convertView.findViewById(R.id.tv_item);
            holder.tv_right = (TextView) convertView.findViewById(R.id.tv_item_right);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        //设置显示图片
        if (!TextUtils.isEmpty(info.value)) {
            Glide.with(context)
                    .load(info.value)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.iv);
            holder.img_clear.setVisibility(View.VISIBLE);
        } else {
            holder.img_clear.setVisibility(View.GONE);
            holder.iv.setImageResource(R.drawable.ic_addpic);
        }
        //设置标题
        holder.tv.setText(info.getName());
        if (info.isType()) {
            holder.tv_right.setVisibility(View.VISIBLE);
        } else {
            holder.tv_right.setVisibility(View.GONE);
        }
        holder.img_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showQuestionView(context, "温謦提示", "确定要删除吗？", "删除", "取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                pictures.get(position).value = "";
                                context.setTvTopRight();
                                notifyDataSetChanged();
                            }
                        },
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
            }
        });
        return convertView;
    }

    class Holder {
        ImageView iv;
        ImageView img_clear;
        TextView tv;
        TextView tv_right;
    }
}