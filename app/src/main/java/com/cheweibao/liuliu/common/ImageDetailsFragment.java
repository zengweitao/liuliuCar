package com.cheweibao.liuliu.common;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.cheweibao.liuliu.R;

import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * 单张图片显示Fragment
 */
public class ImageDetailsFragment extends Fragment {
	private String mImageUrl,mThumbnailUrl;
	private ImageView mImageView,mThumbnailView;
	private ProgressBar progressBar;
	private PhotoViewAttacher mAttacher;
	public static ImageDetailsFragment newInstance(String imageUrl, String thumbnail) {
		final ImageDetailsFragment f = new ImageDetailsFragment();

		final Bundle args = new Bundle();
		args.putString("url", imageUrl);
        args.putString("thumbnail",thumbnail);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImageUrl = getArguments() != null ? getArguments().getString("url") : null;
        mThumbnailUrl = getArguments() != null? getArguments().getString("thumbnail"):null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.fragment_image_detail, container, false);
		mImageView = (ImageView) v.findViewById(R.id.image);
        mThumbnailView = (ImageView) v.findViewById(R.id.iv_thumbnail);
		mAttacher = new PhotoViewAttacher(mImageView);

		mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
			@Override
			public void onPhotoTap(View arg0, float arg1, float arg2) {
                if (getActivity() != null){
                    getActivity().finish();
                }
			}
		});

		progressBar = (ProgressBar) v.findViewById(R.id.loading);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

        if (mThumbnailUrl != null){
            mThumbnailView.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(mThumbnailUrl, mThumbnailView);
        }
		ImageLoader.getInstance().displayImage(mImageUrl, mImageView, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				progressBar.setVisibility(View.VISIBLE);
			}

			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				String message = null;
				switch (failReason.getType()) {
				case IO_ERROR:
					message = "下载错误";
					break;
				case DECODING_ERROR:
					message = "图片无法显示";
					break;
				case NETWORK_DENIED:
					message = "网络有问题，无法下载";
					break;
				case OUT_OF_MEMORY:
					message = "图片太大无法显示";
					break;
				case UNKNOWN:
					message = "未知的错误";
					break;
				}
                //ToastUtil.showToast(message);
				mImageView.setImageResource(R.drawable.default_noimage);
                progressBar.setVisibility(View.GONE);
				mThumbnailView.setVisibility(View.GONE);
                mAttacher.update();
            }

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				progressBar.setVisibility(View.GONE);
                mThumbnailView.setVisibility(View.GONE);
				mAttacher.update();
			}
		});
	}

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
