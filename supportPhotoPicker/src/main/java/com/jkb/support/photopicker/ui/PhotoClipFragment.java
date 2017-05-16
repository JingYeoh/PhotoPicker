package com.jkb.support.photopicker.ui;

import android.os.Bundle;

import com.jkb.support.photopicker.listener.PhotoClipListener;

/**
 * 图片裁剪器
 * Created by yj on 2017/5/16.
 */

public class PhotoClipFragment extends BaseFragment {

    public static PhotoClipFragment newInstance() {
        Bundle args = new Bundle();
        PhotoClipFragment fragment = new PhotoClipFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private PhotoClipListener mPhotoClipListener;

    @Override
    protected int getRootViewId() {
        return 0;
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initView() {

    }

    /**
     * 设置图片裁剪的监听器
     */
    public void setPhotoClipListener(PhotoClipListener listener) {
        this.mPhotoClipListener = listener;
    }
}
