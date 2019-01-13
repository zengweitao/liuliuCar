package com.cheweibao.liuliu.checker;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.common.BaseFragment;
import com.cheweibao.liuliu.common.ImagePagerActivity;
import com.cheweibao.liuliu.common.MyConstants;
import com.cheweibao.liuliu.common.Utils;
import com.cheweibao.liuliu.data.CheckCarData;
import com.cheweibao.liuliu.data.MessageEvent;
import com.cheweibao.liuliu.net.ServerUrl;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 主界面-订单
 */
public class PhotoFragment extends BaseFragment {


    String orderId;
    @Bind(R.id.imageView0)
    ImageView imageView0;
    @Bind(R.id.imageView1)
    ImageView imageView1;
    @Bind(R.id.imageView2)
    ImageView imageView2;
    @Bind(R.id.imageView3)
    ImageView imageView3;
    @Bind(R.id.imageView4)
    ImageView imageView4;
    @Bind(R.id.imageView5)
    ImageView imageView5;
    @Bind(R.id.imageView6)
    ImageView imageView6;
    @Bind(R.id.imageView7)
    ImageView imageView7;
    @Bind(R.id.imageView8)
    ImageView imageView8;
    @Bind(R.id.imageView9)
    ImageView imageView9;
    @Bind(R.id.imageView10)
    ImageView imageView10;
    @Bind(R.id.imageView11)
    ImageView imageView11;
    @Bind(R.id.imageView12)
    ImageView imageView12;
    @Bind(R.id.imageView13)
    ImageView imageView13;
    @Bind(R.id.imageView14)
    ImageView imageView14;
    @Bind(R.id.imageView15)
    ImageView imageView15;

    @Bind(R.id.textView0)
    TextView textView0;
    @Bind(R.id.textView1)
    TextView textView1;
    @Bind(R.id.textView2)
    TextView textView2;
    @Bind(R.id.textView3)
    TextView textView3;
    @Bind(R.id.textView4)
    TextView textView4;
    @Bind(R.id.textView5)
    TextView textView5;
    @Bind(R.id.textView6)
    TextView textView6;
    @Bind(R.id.textView7)
    TextView textView7;
    @Bind(R.id.textView8)
    TextView textView8;
    @Bind(R.id.textView9)
    TextView textView9;
    @Bind(R.id.textView10)
    TextView textView10;
    @Bind(R.id.textView11)
    TextView textView11;
    @Bind(R.id.textView12)
    TextView textView12;
    @Bind(R.id.textView13)
    TextView textView13;
    @Bind(R.id.textView14)
    TextView textView14;
    @Bind(R.id.textView15)
    TextView textView15;
    @Bind(R.id.btn_save)
    Button btnSave;


    List<ImageView> imageViewList;
    List<TextView> textViewList;

    public static PhotoFragment newInstance(String _orderId) {
        Bundle args = new Bundle();
        args.putString("orderId", _orderId);
        PhotoFragment fragment = new PhotoFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle args = getArguments();
        orderId = args.getString("orderId");
        View view = inflater.inflate(R.layout.fragment_checker_order_photo, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        imageViewList = new ArrayList<>();
        textViewList = new ArrayList<>();
        imageViewList.add(imageView0);
        imageViewList.add(imageView1);
        imageViewList.add(imageView2);
        imageViewList.add(imageView3);
        imageViewList.add(imageView4);
        imageViewList.add(imageView5);
        imageViewList.add(imageView6);
        imageViewList.add(imageView7);
        imageViewList.add(imageView8);
        imageViewList.add(imageView9);
        imageViewList.add(imageView10);
        imageViewList.add(imageView11);
        imageViewList.add(imageView12);
        imageViewList.add(imageView13);
        imageViewList.add(imageView14);
        imageViewList.add(imageView15);


        for (int i = 0; i < imageViewList.size(); i++) {
            final int photoId = i;

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageViewList.get(i).getLayoutParams();
            params.height = (myglobal.SCR_WIDTH - Utils.dp2Pixel(mContext, 1)) * 3 / 10;
            imageViewList.get(i).setLayoutParams(params);

            final int finalId = i;
            imageViewList.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ("1".equals(CheckCarData.checkAll)) { //已检测
                        if(TextUtils.isEmpty(CheckCarData.photoList.get(finalId)))
                            return;
                        ArrayList<String> list = new ArrayList<>();
                        int tmp = 0, curIdx = 0;
                        for(int j = 0; j < CheckCarData.photoList.size(); j++){
                            if(!TextUtils.isEmpty(CheckCarData.photoList.get(j))) {
                                list.add(CheckCarData.photoList.get(j));
                                if(j == finalId) curIdx = tmp;
                                tmp ++;
                            }

                        }
                        Intent intent = new Intent(mContext, ImagePagerActivity.class);
                        intent.putExtra(ImagePagerActivity.FILE_FLAG, false);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, list);
                        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, curIdx);
                        startActivity(intent);
                    }else {
                        MessageEvent event = new MessageEvent(MyConstants.REQUEST_TAKE_PIC);
                        event.putExtra("position", photoId);
                        event.putExtra("fromPhoto", 1);
                        EventBus.getDefault().post(event);
                    }
                }
            });
        }
        textViewList.add(textView0);
        textViewList.add(textView1);
        textViewList.add(textView2);
        textViewList.add(textView3);
        textViewList.add(textView4);
        textViewList.add(textView5);
        textViewList.add(textView6);
        textViewList.add(textView7);
        textViewList.add(textView8);
        textViewList.add(textView9);
        textViewList.add(textView10);
        textViewList.add(textView11);
        textViewList.add(textView12);
        textViewList.add(textView13);
        textViewList.add(textView14);
        textViewList.add(textView15);

        for (int i = 0; i < 16; i++) {
            if(!TextUtils.isEmpty(CheckCarData.photoList.get(i))) {
                showImgWithGlid(CheckCarData.photoList.get(i), imageViewList.get(i));
                textViewList.get(i).setVisibility(View.VISIBLE);
            }else{
                textViewList.get(i).setVisibility(View.GONE);
            }

        }

        if ("1".equals(CheckCarData.checkAll)) {
            disableAll();
        }
    }

    private void disableAll(){
        btnSave.setVisibility(View.GONE);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);

    }


    @OnClick({R.id.btn_save,})
    public void onClick(View view) {
        Intent it;
        switch (view.getId()) {
            case R.id.btn_save:
                saveData();
                break;
        }

    }

    private void saveData() {
        if (getThread_flag()) return;
        setThread_flag(true);

        JSONArray photo = new JSONArray();
        for (int i = 0; i < CheckCarData.photoList.size(); i++) {
            if(i < 13 && TextUtils.isEmpty(CheckCarData.photoList.get(i))){
                shortToast("还有照片要拍");
                setThread_flag(false);
                return;
            }
            JSONObject object = new JSONObject();
            object.put("imgUrl", CheckCarData.photoList.get(i));
            photo.add(object);
        }


        if (ServerUrl.isNetworkConnected(mContext)) {
            try {
                showProgress();
                HashMap<String, String> fields = new HashMap<String, String>();
                fields.put("token", myglobal.user.token);
                fields.put("detailId", CheckCarData.detailId);
                fields.put("carId", CheckCarData.carId);

                fields.put("photo", photo.toJSONString());
                postMap(ServerUrl.savePhoto, fields, saveHandler);
            } catch (Exception e) {
                e.printStackTrace();
                setThread_flag(false);
            }
        } else {
            shortToast("网络连接不可用");
            setThread_flag(false);
        }
    }

    private Handler saveHandler = new Handler() {

        public void handleMessage(Message msg) {
            setThread_flag(false);
            hideProgress();
            switch (msg.what) {
                case 0:
                    @SuppressWarnings("unchecked")
                    HashMap<String, Object> result = (HashMap<String, Object>) msg.obj;
                    try {
                        String status = result.get("result_code") + "";
                        if ("200".equals(status)) {
                            shortToast("已保存～～");
                            CheckCarData.photoSave = "1";
                            MessageEvent event = new MessageEvent(MyConstants.SAVE_CHECK_INFO);
                            postEvent(event);
                        }else if("401".equals(status)){
                            String message = result.get("message") + "";
                            shortToast(message);
                            goLoginPage();
                        } else {
                            String message = result.get("message") + "";
                            shortToast(message);
                        }
                    } catch (Exception e) {
                        shortToast("抱歉数据异常");
                    }
                    break;
                default:
                    shortToast("网络不给力!");
                    break;
            }
        }

        ;
    };


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.isMsgOf(MyConstants.UPDATE_PHOTO)) {
            int photoId = event.getIntExtra("photoId", -1);
            if (photoId >= 0) {
                showImgWithGlid(CheckCarData.photoList.get(photoId), imageViewList.get(photoId));
                textViewList.get(photoId).setVisibility(View.VISIBLE);
            }

        }else if(event.isMsgOf(MyConstants.CHECK_ALL)){
            disableAll();
        }
    }


}
