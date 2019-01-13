package com.cheweibao.liuliu.appraiser;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;
import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.adapter.PaiXu;
import com.cheweibao.liuliu.common.BarChartManager;
import com.cheweibao.liuliu.common.JsonUtil;
import com.cheweibao.liuliu.common.ToastUtil;
import com.cheweibao.liuliu.data.CarDealerOfferBean;
import com.cheweibao.liuliu.net.ServerUrl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by user on 2018/4/11.
 */

public class CarDealerOfferFragment extends Fragment {
    @Bind(R.id.tv_car_dealer_ay)
    TextView tvCarDealerAy;
    @Bind(R.id.tv_car_dealer_an)
    TextView tvCarDealerAn;
    @Bind(R.id.tv_car_dealer_by)
    TextView tvCarDealerBy;
    @Bind(R.id.tv_car_dealer_bn)
    TextView tvCarDealerBn;
    @Bind(R.id.tv_car_dealer_cy)
    TextView tvCarDealerCy;
    @Bind(R.id.tv_car_dealer_cn)
    TextView tvCarDealerCn;
    @Bind(R.id.lin_cardealer_yiban_select)
    LinearLayout linCardealerYibanSelect;
    @Bind(R.id.lin_cardealer_yiban_no_select)
    LinearLayout linCardealerYibanNoSelect;
    @Bind(R.id.lin_cardealer_lianghao_select)
    LinearLayout linCardealerLianghaoSelect;
    @Bind(R.id.lin_cardealer_lianghao_no_select)
    LinearLayout linCardealerLianghaoNoSelect;
    @Bind(R.id.lin_cardealer_jihao_select)
    LinearLayout linCardealerJihaoSelect;
    @Bind(R.id.lin_cardealer_jihao_no_select)
    LinearLayout linCardealerJihaoNoSelect;
    @Bind(R.id.car_dealer_charts)
    BarChart mBarChart;
    @Bind(R.id.tv_car_qujian)
    TextView tvCarQujian;
    @Bind(R.id.rela_car_no_data)
    RelativeLayout relaCarNoData;
    @Bind(R.id.lin_car_no_data)
    LinearLayout linCarNoData;

    private String trimId;
    private String buyCarDate;
    private String mileage;
    private String cityId;
    private String city;
    private String type;
    private List<PaiXu> list = new ArrayList<>();
    private String upA;
    private String downA;
    private String upB;
    private String downB;
    private String upC;
    private String downC;

    public static CarDealerOfferFragment newInstance() {
        CarDealerOfferFragment carDealerOfferFragment = new CarDealerOfferFragment();
        return carDealerOfferFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_cardealer_offer, container, false);
        ButterKnife.bind(this, v);
        initView();
        initData();


        return v;
    }

    private void initView() {
        initSelect("一般");
    }

    private void initData() {
        Bundle bundle = getArguments();
        trimId = bundle.getString("trimId");
        buyCarDate = bundle.getString("buyCarDate");
        mileage = bundle.getString("mileage");
        cityId = bundle.getString("cityId");
        city = bundle.getString("city");
        type = bundle.getString("type");

        if (ServerUrl.isNetworkConnected(getActivity())) {
            OkGo.post(ServerUrl.pingu)
                    .tag(this)
                    .params("trimId", trimId)
                    .params("buyCarDate", buyCarDate+"-01")
                    .params("mileage",( Integer.parseInt(mileage)*10000)+"")
                    .params("cityId", cityId)
                    .params("city", city)
                    .params("type", type)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            CarDealerOfferBean carDealerOfferBean = JsonUtil.parseJsonToBean(s,
                                    CarDealerOfferBean.class);
                            if (carDealerOfferBean != null) {
                                if (carDealerOfferBean.isSuccess()) {
                                    if (linCarNoData!=null){
                                        linCarNoData.setVisibility(View.VISIBLE);
                                    }


                                    CarDealerOfferBean.ContentBean content = carDealerOfferBean.getContent();
                                    String excellent = content.getEstimate().getB2CPrices().getA().getMid();
                                    String good = content.getEstimate().getB2CPrices().getB().getMid();
                                    String commonly = content.getEstimate().getB2CPrices().getC().getMid();

                                    upA = content.getEstimate().getB2CPrices().getA().getUp();
                                    downA = content.getEstimate().getB2CPrices().getA().getLow();
                                    upB = content.getEstimate().getB2CPrices().getB().getUp();
                                    downB = content.getEstimate().getB2CPrices().getB().getLow();
                                    upC = content.getEstimate().getB2CPrices().getC().getUp();
                                    downC = content.getEstimate().getB2CPrices().getC().getLow();



                                    List<CarDealerOfferBean.ContentBean.ResidualRatioRankingBean> list1 =content.getResidualRatioRanking();
                                    if (list1.size()>0){
                                        String c = list1.get(0).getMonth().charAt(6)+"";
                                        for (int i=0;i<list1.size();i++){
                                            if (c.equals(list1.get(i).getMonth().charAt(6)+"")){
                                                float val = Float.parseFloat(list1.get(i)
                                                        .getB2CPrices());
                                                String month = list1.get(i).getMonth();
                                                String sub = month.substring(0, 4);
                                                list.add(new PaiXu(sub, val));
                                            }

                                        }
                                    }

                                    tvCarQujian.setText(downC + "-" + upC + "万");
                                    tvCarDealerAy.setText(commonly + "万");
                                    tvCarDealerAn.setText(commonly + "万");
                                    tvCarDealerBy.setText(good + "万");
                                    tvCarDealerBn.setText(good + "万");
                                    tvCarDealerCy.setText(excellent + "万");
                                    tvCarDealerCn.setText(excellent + "万");

                                   initLineChart();
                                } else {
                                    if (linCarNoData!=null){
                                        linCarNoData.setVisibility(View.GONE);
                                    }
                                    if (relaCarNoData!=null){
                                        relaCarNoData.setVisibility(View.VISIBLE);
                                    }


                                }
                            }else {
                                ToastUtil.showToast("请求网络失败");
                            }
                        }

                        @Override
                        public void onBefore(BaseRequest request) {
                            super.onBefore(request);

                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            super.onError(call, response, e);
                            ToastUtil.showToast("请求网络失败了");

                        }
                    });
        }


    }


    /**
     * 初始化LineChart
     */
    private void initLineChart() {

        BarChartManager barChartManager = new BarChartManager(mBarChart, list);
    }


    @OnClick({R.id.lin_cardealer_yiban_no_select, R.id.lin_cardealer_lianghao_no_select, R.id
            .lin_cardealer_jihao_no_select})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lin_cardealer_yiban_no_select:  //点击一般
                tvCarQujian.setText(downC + "-" + upC + "万");
                initSelect("一般");
                break;
            case R.id.lin_cardealer_lianghao_no_select:  //点击良好
                tvCarQujian.setText(downB + "-" + upB + "万");
                initSelect("良好");
                break;
            case R.id.lin_cardealer_jihao_no_select:  //点击极好
                tvCarQujian.setText(downA + "-" + upA + "万");
                initSelect("极好");
                break;
        }
    }


    private void initSelect(String select) {
        if (select.equals("一般")) {
            linCardealerYibanSelect.setVisibility(View.VISIBLE);
            linCardealerYibanNoSelect.setVisibility(View.GONE);
            linCardealerLianghaoSelect.setVisibility(View.GONE);
            linCardealerLianghaoNoSelect.setVisibility(View.VISIBLE);
            linCardealerJihaoSelect.setVisibility(View.GONE);
            linCardealerJihaoNoSelect.setVisibility(View.VISIBLE);
        } else if (select.equals("良好")) {
            linCardealerYibanSelect.setVisibility(View.GONE);
            linCardealerYibanNoSelect.setVisibility(View.VISIBLE);
            linCardealerLianghaoSelect.setVisibility(View.VISIBLE);
            linCardealerLianghaoNoSelect.setVisibility(View.GONE);
            linCardealerJihaoSelect.setVisibility(View.GONE);
            linCardealerJihaoNoSelect.setVisibility(View.VISIBLE);
        } else if (select.equals("极好")) {
            linCardealerYibanSelect.setVisibility(View.GONE);
            linCardealerYibanNoSelect.setVisibility(View.VISIBLE);
            linCardealerLianghaoSelect.setVisibility(View.GONE);
            linCardealerLianghaoNoSelect.setVisibility(View.VISIBLE);
            linCardealerJihaoSelect.setVisibility(View.VISIBLE);
            linCardealerJihaoNoSelect.setVisibility(View.GONE);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        OkGo.getInstance().cancelTag(this);
    }

    public String parseMapToJson(Map<?, ?> map) {
        try {
            Gson gson = new Gson();
            return gson.toJson(map);
        } catch (Exception e) {
        }
        return null;
    }


    class MapComparator implements Comparator<PaiXu> {

        public int compare(PaiXu lhs, PaiXu rhs) {
            return lhs.getTime().compareTo(rhs.getTime());
        }

    }
}
