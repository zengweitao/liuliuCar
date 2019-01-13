package com.cheweibao.liuliu.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.common.HanziToPinyin;
import com.cheweibao.liuliu.common.MyGlobal;
import com.cheweibao.liuliu.data.Citys;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2018/4/9.
 */
public class MyRegisterCityAdapter extends BaseAdapter {
    private List<Citys> list;
    private ViewHolder viewHolder;
    private RegisterCityCallback callback;

    public interface RegisterCityCallback {
        void onRegister(Citys s);
    }

    public void setCallback(RegisterCityCallback callback) {
        this.callback = callback;
    }

    public MyRegisterCityAdapter(List<Citys> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = View.inflate(MyGlobal.context, R.layout.layout_index_list_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.nameTextView.setText(list.get(position).getName());
        String currIndexWord = getLetter(list.get(position).getName());
        if (position > 0) {
            String lastIndexWord = getLetter(list.get(position - 1).getName());
            if (lastIndexWord.equals(currIndexWord)) {
                viewHolder.indexTextView.setVisibility(View.GONE);
            } else {
                viewHolder.indexTextView.setVisibility(View.VISIBLE);
                viewHolder.indexTextView.setText(currIndexWord);
            }
        } else {
            viewHolder.indexTextView.setVisibility(View.VISIBLE);
            viewHolder.indexTextView.setText(currIndexWord);
        }


        viewHolder.linLLItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onRegister(list.get(position));
            }
        });


        return convertView;
    }

    public class ViewHolder {
        private TextView indexTextView;
        private TextView nameTextView;
        private LinearLayout linLLItem;

        public ViewHolder(View convertView) {
            indexTextView = (TextView) convertView.findViewById(R.id.index);
            nameTextView = (TextView) convertView.findViewById(R.id.name);

            linLLItem = (LinearLayout) convertView.findViewById(R.id.ll_item);
        }
    }

    /**
     * 根据字符串获取当前首字母
     *
     * @param name
     * @return
     */
    private String getLetter(String name) {
        String DefaultLetter = "#";
        if (TextUtils.isEmpty(name)) {
            return DefaultLetter;
        }
        char char0 = name.toLowerCase().charAt(0);
        if (Character.isDigit(char0)) {
            return DefaultLetter;
        }
        ArrayList<HanziToPinyin.Token> l = HanziToPinyin.getInstance().get(name.substring(0, 1));
        if (l != null && l.size() > 0 && l.get(0).target.length() > 0) {
            String letter;
            if ("重".equals(l.get(0).source)) {
                letter = "C";
            } else {
                HanziToPinyin.Token token = l.get(0);
                // toLowerCase()返回小写， toUpperCase()返回大写
                letter = token.target.substring(0, 1).toUpperCase();
            }
            char c = letter.charAt(0);
            // 这里的 'a' 和 'z' 要和letter的大小写保持一直。
            if (c < 'A' || c > 'Z') {
                return DefaultLetter;
            }
            return letter;
        }
        return DefaultLetter;
    }
}
