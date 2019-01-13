package com.cheweibao.liuliu.common;

import android.graphics.Color;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.cheweibao.liuliu.adapter.PaiXu;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2018/4/11.
 */

public class BarChartManager {
    private BarChart mBarChart;
    private YAxis leftAxis;
    private YAxis rightAxis;
    private XAxis xAxis;
    private List<PaiXu>  list;

    public BarChartManager(BarChart barChart,List<PaiXu> list) {
        this.mBarChart = barChart;
        leftAxis = mBarChart.getAxisLeft();
        rightAxis = mBarChart.getAxisRight();
        xAxis = mBarChart.getXAxis();
        this.list = list;

        initLineChart();
    }

    /**
     * 初始化LineChart
     */
    private void initLineChart() {

        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawGridLines(false);
        leftAxis.setEnabled(false);
        rightAxis.setDrawAxisLine(false);
        rightAxis.setDrawGridLines(false);
        rightAxis.setEnabled(false);


        //背景颜色
        mBarChart.setBackgroundColor(Color.WHITE);
        //网格
        mBarChart.setDrawGridBackground(false);

        mBarChart.setDrawBarShadow(false);
        mBarChart.setDrawValueAboveBar(true);
        //背景阴影
        mBarChart.setDrawBarShadow(false);
        mBarChart.setHighlightFullBarEnabled(false);
        mBarChart.getLegend().setEnabled(false);
        mBarChart.getDescription().setText("");
        mBarChart.setTouchEnabled(false);
        mBarChart.setDragEnabled(false);
        //显示边界
        mBarChart.setDrawBorders(false);
        //设置动画效果
        mBarChart.animateY(1000, Easing.EasingOption.Linear);
        mBarChart.animateX(1000, Easing.EasingOption.Linear);

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(3);
        xAxis.setGranularity(1f);
        xAxis.setTextColor(Color.parseColor("#FFDCDCDC"));

        List<BarEntry> entries = new ArrayList<>();   //数据点的封装类变为BarChart

        for (int i = 0 ; i <list.size(); i++){
            entries.add(new BarEntry(i , list.get(i).getValue()));
        }

         xAxis.setValueFormatter(new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float v, AxisBase axisBase) {
                return list.get(((int) (v) % list.size())).getTime();
            }
        });

        BarDataSet set = new BarDataSet(entries, "BarDataSet");
        set.setColor(Color.parseColor("#0dbdb2"));  //给线条设置颜色
        final DecimalFormat df = new DecimalFormat("0.00");
        //设置数据样式
        set.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return df.format(value)+"";
            }
        });
        set.setDrawValues(true);
        set.setValueTextColor(Color.parseColor("#FFDCDCDC"));
        set.setValueTextSize(10);
        final BarData data = new BarData(set);
        data.setBarWidth(0.5f);      // 设置条形的宽度

       /* xAxis.setAxisMaximum(50f);
        xAxis.setAxisMinimum(0f);*/
        /*Matrix m = new Matrix();
        m.postScale(1.5f, 1f);
        mBarChart.getViewPortHandler().refresh(m, mBarChart, false);*/
        mBarChart.setData(data);
        mBarChart.setFitBars(true);
        mBarChart.invalidate();

    }



}
