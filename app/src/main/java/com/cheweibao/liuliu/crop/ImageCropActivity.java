package com.cheweibao.liuliu.crop;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.edmodo.cropper.CropImageView;
import com.cheweibao.liuliu.R;
import com.cheweibao.liuliu.common.MyConstants;
import com.cheweibao.liuliu.common.MyGlobal;
import com.cheweibao.liuliu.common.Utils;


public class ImageCropActivity extends Activity implements View.OnClickListener {

    private Bitmap bitmap = null;
    private int viewWidth, viewHeight;
    private int bitmapWidth, bitmapHeight;

    float rate = 1.0f;
    float viewBitmapWidth = 0;
    float viewBitmapHeight = 0;

    private CropImageView imgView;
    private int mAspectRatioX = 10;
    private int mAspectRatioY = 10;
    private String path;

    float m_fBitHRate;

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_crop);

        initView();
        initSize();

        this.path = getIntent().getStringExtra(Utils.IT_KEY_1);
        this.mAspectRatioX = getIntent().getIntExtra(Utils.IT_KEY_2, -2);
        this.mAspectRatioY = getIntent().getIntExtra(Utils.IT_KEY_3, -2);

        setBitmap();
    }

    //初始化UI
    private void initView() {

        findViewById(R.id.tvCancel).setOnClickListener(this);
        findViewById(R.id.tvConfirm).setOnClickListener(this);
        imgView = ((CropImageView) findViewById(R.id.cropImageView));
    }

    //获取ImageViewSize
    private void initSize() {

        viewWidth = MyGlobal.getInstance().SCR_WIDTH;
        viewHeight = MyGlobal.getInstance().SCR_HEIGHT - Utils.dp2Pixel(this, 50);
    }

    //获取BitamSize
    private void getSize() {

        bitmapWidth = bitmap.getWidth();
        bitmapHeight = bitmap.getHeight();

        float viewAspect = (float) viewHeight / (float) viewWidth;
        float bitmapAspect = (float) bitmapHeight / (float) bitmapWidth;
        if (bitmapAspect > viewAspect) {
            viewBitmapHeight = viewHeight;
            rate = (float) viewHeight / (float) bitmapHeight;
            viewBitmapWidth = rate * (float) bitmapWidth;
        } else {
            viewBitmapWidth = viewWidth;
            rate = (float) viewWidth / (float) bitmapWidth;
            viewBitmapHeight = rate * (float) bitmapHeight;
        }
    }

    protected void onResume() {
        super.onResume();
//        MobclickAgent.onResume(this);
    }

    protected void onPause() {
        super.onPause();
//        MobclickAgent.onPause(this);
    }


    //设置Bitmap
    private void setBitmap() {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int m_nOrgBitmapW = options.outWidth;
        int m_nOrgBitmapH = options.outHeight;

        int rotation = Utils.GetExifOrientation(path);
        if (rotation == 90
                || rotation == 270) {
            int tmpVal = m_nOrgBitmapW;
            m_nOrgBitmapW = m_nOrgBitmapH;
            m_nOrgBitmapH = tmpVal;
        }

        bitmap = Utils.getSafeDecodeBitmapAndAdjustXYWithRGB_565(path, 512, viewWidth, viewHeight);
        int m_nBitmapH = bitmap.getHeight();
        m_fBitHRate = (float) m_nOrgBitmapH / (float) m_nBitmapH;

        if (bitmap == null) {
            finish();
            return;
        }
        imgView.setImageBitmap(bitmap);
        imgView.setAspectRatio(mAspectRatioX, mAspectRatioY);
        imgView.setGuidelines(2);
        imgView.setFixedAspectRatio(true);

        getSize();
    }

    protected void onRestoreInstanceState(Bundle paramBundle) {
        super.onRestoreInstanceState(paramBundle);
        this.mAspectRatioX = paramBundle.getInt("ASPECT_RATIO_X");
        this.mAspectRatioY = paramBundle.getInt("ASPECT_RATIO_Y");
    }

    protected void onSaveInstanceState(Bundle paramBundle) {
        super.onSaveInstanceState(paramBundle);
        paramBundle.putInt("ASPECT_RATIO_X", this.mAspectRatioX);
        paramBundle.putInt("ASPECT_RATIO_Y", this.mAspectRatioY);
    }

    //作花边的图片
    private class TaskMakeImg extends AsyncTask<Void, Void, Void> {

        ProgressDialog m_dlgWait = null;
        int[] arrInt = new int[4];

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            RectF rect = imgView.getActualCropRect();
            arrInt[0] = (int) ((float) rect.left * (float) m_fBitHRate);
            arrInt[1] = (int) ((float) rect.top * (float) m_fBitHRate);
            arrInt[2] = (int) ((float) rect.right * (float) m_fBitHRate);
            arrInt[3] = (int) ((float) rect.bottom * (float) m_fBitHRate);

            m_dlgWait = new ProgressDialog(ImageCropActivity.this, AlertDialog.THEME_HOLO_LIGHT);
            m_dlgWait.setTitle("");
            m_dlgWait.setMessage(MyConstants.MSG_WAITING);
            m_dlgWait.setCancelable(false);
            m_dlgWait.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Bitmap bitmap = Utils.getBitmapWithRectWithRotation(path, (int) arrInt[0], (int) arrInt[1]
                        , (int) (arrInt[2] - arrInt[0]), (int) (arrInt[3] - arrInt[1]), Utils.GetExifOrientation(path), 1024);

                if (bitmap != null) {
                    Utils.saveBitmap2File(bitmap, path, CompressFormat.JPEG);
                    bitmap.recycle();
                    bitmap = null;
                }
            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (m_dlgWait != null)
                m_dlgWait.dismiss();
            m_dlgWait = null;

            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    public void onClick(View paramView) {
        switch (paramView.getId()) {
            default:
                return;
            case R.id.tvConfirm:
                new TaskMakeImg().execute();
                break;
            case R.id.tvCancel:
                finish();
                break;
        }
    }
}