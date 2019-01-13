package com.cheweibao.liuliu.popup;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.data.Province;
import com.cheweibao.liuliu.db.LocalCityTable;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sx on 2017/1/9.
 */
public class SelectOnePopWindow extends PopupWindow {

    private String[] list;
    @Bind(R.id.tv_cancel)
    TextView tvCancel;
    @Bind(R.id.tv_confirm)
    TextView tvConfirm;
    @Bind(R.id.wv)
    WheelView wv;
    private Context mContext;

    public SelectOnePopWindow(final Context context, String[] list) {
        super(context);
        mContext = context;
        this.list = list;
        //pop_bottom为自定义布局
        View view = LayoutInflater.from(context).inflate(R.layout.pop_select_one, null);
        //设置PopupWindow的View
        setContentView(view);
        ButterKnife.bind(this, view);

        initPopWindow();
        initWheelViewStyle();
        initWheelViewDate();
    }


    /**
     * 设置popwindow
     */
    private void initPopWindow() {

        //设置PopupWindow弹出窗体的宽
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        //设置PopupWindow弹出窗体的高
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        //设置PopupWindow弹出窗体可点击
        setFocusable(true);
        //设置PopupWindow弹出窗体动画效果
        setAnimationStyle(R.style.popupAnimation);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        getBackground().setAlpha(0);
        final Activity activity = (Activity) mContext;
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.alpha = 1;
        activity.getWindow().setAttributes(params);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        setOutsideTouchable(true);

        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = activity.getWindow().getAttributes();
                params.alpha = 1f;
                activity.getWindow().setAttributes(params);
            }
        });
    }

    /**
     * 初始化wheelview样式
     */
    private void initWheelViewStyle() {
        //样式配置
        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.textColor = Color.BLACK;
        style.selectedTextSize = 18;////单位dp
        style.holoBorderColor = Color.parseColor("#f4f4f4");
        style.textColor = Color.parseColor("#999999");
        style.selectedTextColor = Color.BLACK;

        //设置样式
        wv.setWheelAdapter(new ArrayWheelAdapter(mContext)); // 文本数据源
        wv.setSkin(WheelView.Skin.Holo); // holo皮肤
        wv.setStyle(style);
    }

    /**
     * 设置wheelview数据
     */
    private void initWheelViewDate() {
        wv.setWheelData(createMainDatas(list));
    }

    /**
     * 省份数据
     *
     * @return
     */
    private List<String> createMainDatas(String[] strs) {
        List<String> list = new ArrayList<>();
        for (String province : strs) {
            list.add(province);
        }
        return list;
    }

    @OnClick({R.id.tv_cancel, R.id.tv_confirm})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_confirm:
                if (onSelectListener != null) {
                    onSelectListener.selectArea(wv.getSelectionItem().toString(), wv.getCurrentPosition() + 1);
                    dismiss();
                }
                break;
        }
    }

    /**
     * 区域数据
     *
     * @return
     */
    public void setOnSelectOneListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }

    private OnSelectListener onSelectListener;

    public interface OnSelectListener {
        void selectArea(String str, int position);
    }
}
