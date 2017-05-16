package com.jkb.support.photopicker.ui;

import android.os.Bundle;

import com.jkb.support.photopicker.bean.PhotoPickBean;
import com.jkb.support.photopicker.config.PhotoPickConfig;

/**
 * 图片选择器
 * Created by yj on 2017/5/16.
 */

public class PhotoPickerFragment extends BaseFragment {

    public static PhotoPickerFragment newInstance(PhotoPickBean photoBean) {
        Bundle args = new Bundle();
        args.putParcelable(PhotoPickConfig.KeyBundle.PHPTO_PICK, photoBean);
        PhotoPickerFragment fragment = new PhotoPickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private PhotoPickBean mPhotoBean;

    @Override
    protected int getRootViewId() {
        return 0;
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        Bundle args = savedInstanceState;
        if (savedInstanceState == null) {
            args = getArguments();
        }
        mPhotoBean = args.getParcelable(PhotoPickConfig.KeyBundle.PHPTO_PICK);
    }

    @Override
    protected void initView() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(PhotoPickConfig.KeyBundle.PHPTO_PICK, mPhotoBean);
    }
}
