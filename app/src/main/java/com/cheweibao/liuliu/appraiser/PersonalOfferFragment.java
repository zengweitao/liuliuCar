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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by user on 2018/4/11.
 */

public class PersonalOfferFragment extends Fragment {
    @Bind(R.id.lin_personal_yiban_select)
    LinearLayout linPersonalYibanSelect;
    @Bind(R.id.lin_personal_yiban_no_select)
    LinearLayout linPersonalYibanNoSelect;
    @Bind(R.id.lin_personal_lianghao_select)
    LinearLayout linPersonalLianghaoSelect;
    @Bind(R.id.lin_personal_lianghao_no_select)
    LinearLayout linPersonalLianghaoNoSelect;
    @Bind(R.id.lin_personal_jihao_select)
    LinearLayout linPersonalJihaoSelect;
    @Bind(R.id.lin_personal_jihao_no_select)
    LinearLayout linPersonalJihaoNoSelect;
    @Bind(R.id.lin_personal_charts)
    BarChart linPersonalCharts;
    @Bind(R.id.tv_personal_ay)
    TextView tvPersonalAy;
    @Bind(R.id.tv_personal_an)
    TextView tvPersonalAn;
    @Bind(R.id.tv_personal_by)
    TextView tvPersonalBy;
    @Bind(R.id.tv_personal_bn)
    TextView tvPersonalBn;
    @Bind(R.id.tv_personal_cy)
    TextView tvPersonalCy;
    @Bind(R.id.tv_personal_cn)
    TextView tvPersonalCn;
    @Bind(R.id.tv_personal_qujian)
    TextView tvPersonalQujian;
    @Bind(R.id.lin_person_no_data)
    LinearLayout linPersonNoData;
    @Bind(R.id.rela_person_no_data)
    RelativeLayout relaPersonNoData;

    private String trimId;
    private String buyCarDate;
    private String mileage;
    private String cityId;
    private String city;
    private String type;

    private String upA;
    private String downA;
    private String upB;
    private String downB;
    private String upC;
    private String downC;

    private List<PaiXu> list = new ArrayList<>();

    public static PersonalOfferFragment newInstance() {
        PersonalOfferFragment personalOfferFragment = new PersonalOfferFragment();
        return personalOfferFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_personal_offer, container, false);
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
                                    if (linPersonNoData!=null){
                                        linPersonNoData.setVisibility(View.VISIBLE);
                                    }


                                    CarDealerOfferBean.ContentBean content = carDealerOfferBean.getContent();
                                    String excellent = content.getEstimate().getC2CPrices().getA().getMid();
                                    String good = content.getEstimate().getC2CPrices().getB().getMid();
                                    String commonly = content.getEstimate().getC2CPrices().getC().getMid();

                                    upA = content.getEstimate().getC2CPrices().getA().getUp();
                                    downA = content.getEstimate().getC2CPrices().getA().getLow();
                                    upB = content.getEstimate().getC2CPrices().getB().getUp();
                                    downB = content.getEstimate().getC2CPrices().getB().getLow();
                                    upC = content.getEstimate().getC2CPrices().getC().getUp();
                                    downC = content.getEstimate().getC2CPrices().getC().getLow();

                                    List<CarDealerOfferBean.ContentBean.ResidualRatioRankingBean> list1 =content.getResidualRatioRanking();
                                    if (list1.size()>0){
                                        String c = list1.get(0).getMonth().charAt(6)+"";
                                        for (int i=0;i<list1.size();i++){
                                            if (c.equals(list1.get(i).getMonth().charAt(6)+"")){
                                                float val = Float.parseFloat(list1.get(i)
                                                        .getC2BPrices());
                                                String month = list1.get(i).getMonth();
                                                String sub = month.substring(0, 4);
                                                list.add(new PaiXu(sub, val));
                                            }

                                        }
                                    }

                                    tvPersonalQujian.setText(downC + "-" + upC + "万");
                                    tvPersonalAy.setText(commonly + "万");
                                    tvPersonalAn.setText(commonly + "万");
                                    tvPersonalBy.setText(good + "万");
                                    tvPersonalBn.setText(good + "万");
                                    tvPersonalCy.setText(excellent + "万");
                                    tvPersonalCn.setText(excellent + "万");

                                    initChart();

                                } else {
                                    if (linPersonNoData!=null){
                                        linPersonNoData.setVisibility(View.GONE);
                                    }
                                    if (relaPersonNoData!=null){
                                        relaPersonNoData.setVisibility(View.VISIBLE);
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


    private void initChart() {
        BarChartManager barChartManager = new BarChartManager(linPersonalCharts, list);
    }

    @OnClick({R.id.lin_personal_yiban_no_select, R.id.lin_personal_lianghao_no_select, R.id
            .lin_personal_jihao_no_select})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lin_personal_yiban_no_select:
                tvPersonalQujian.setText(downC + "-" + upC + "万");
                initSelect("一般");
                break;
            case R.id.lin_personal_lianghao_no_select:
                tvPersonalQujian.setText(downB + "-" + upB + "万");
                initSelect("良好");
                break;
            case R.id.lin_personal_jihao_no_select:
                tvPersonalQujian.setText(downA + "-" + upA + "万");
                initSelect("极好");
                break;
        }
    }

    private void initSelect(String select) {
        if (select.equals("一般")) {
            linPersonalYibanSelect.setVisibility(View.VISIBLE);
            linPersonalYibanNoSelect.setVisibility(View.GONE);
            linPersonalLianghaoSelect.setVisibility(View.GONE);
            linPersonalLianghaoNoSelect.setVisibility(View.VISIBLE);
            linPersonalJihaoSelect.setVisibility(View.GONE);
            linPersonalJihaoNoSelect.setVisibility(View.VISIBLE);
        } else if (select.equals("良好")) {
            linPersonalYibanSelect.setVisibility(View.GONE);
            linPersonalYibanNoSelect.setVisibility(View.VISIBLE);
            linPersonalLianghaoSelect.setVisibility(View.VISIBLE);
            linPersonalLianghaoNoSelect.setVisibility(View.GONE);
            linPersonalJihaoSelect.setVisibility(View.GONE);
            linPersonalJihaoNoSelect.setVisibility(View.VISIBLE);
        } else if (select.equals("极好")) {
            linPersonalYibanSelect.setVisibility(View.GONE);
            linPersonalYibanNoSelect.setVisibility(View.VISIBLE);
            linPersonalLianghaoSelect.setVisibility(View.GONE);
            linPersonalLianghaoNoSelect.setVisibility(View.VISIBLE);
            linPersonalJihaoSelect.setVisibility(View.VISIBLE);
            linPersonalJihaoNoSelect.setVisibility(View.GONE);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        OkGo.getInstance().cancelTag(this);
    }

    class MapComparator implements Comparator<PaiXu> {

        public int compare(PaiXu lhs, PaiXu rhs) {
            return lhs.getTime().compareTo(rhs.getTime());
        }

    }
}
