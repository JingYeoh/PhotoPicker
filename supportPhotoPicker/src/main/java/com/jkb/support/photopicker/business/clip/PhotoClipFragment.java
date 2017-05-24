package com.jkb.support.photopicker.business.clip;

import android.os.Bundle;

import com.jkb.support.photopicker.base.PhotoPickBaseFragment;

/**
 * Created by yj on 2017/5/24.
 */

public class PhotoClipFragment extends PhotoPickBaseFragment {

    public static PhotoClipFragment newInstance() {
        Bundle args = new Bundle();
        PhotoClipFragment fragment = new PhotoClipFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getRootViewId() {
        return 0;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initListener() {

    }
}
