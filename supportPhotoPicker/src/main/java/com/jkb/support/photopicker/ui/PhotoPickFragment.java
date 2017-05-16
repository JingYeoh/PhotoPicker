package com.jkb.support.photopicker.ui;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jkb.support.photopicker.R;
import com.jkb.support.photopicker.bean.PhotoPickBean;
import com.jkb.support.photopicker.config.PhotoPickConfig;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

/**
 * 图片选择器
 * Created by yj on 2017/5/16.
 */

public class PhotoPickFragment extends BaseFragment {

    public static PhotoPickFragment newInstance(PhotoPickBean photoBean) {
        Bundle args = new Bundle();
        args.putParcelable(PhotoPickConfig.KeyBundle.PHPTO_PICK, photoBean);
        PhotoPickFragment fragment = new PhotoPickFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //ui
    private SlidingUpPanelLayout slidingUpPanelLayout;
    private RecyclerView galleryRecyclerView;
    private RecyclerView photoRecyclerView;

    //data
    private PhotoPickBean mPhotoBean;

    @Override
    protected int getRootViewId() {
        return R.layout.frg_photo_pick;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
    }

    @Override
    protected void initView() {
        slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.pick_supl);
        //相册
        galleryRecyclerView = (RecyclerView) findViewById(R.id.pick_rv_gallery);
        galleryRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        //图片
        photoRecyclerView = (RecyclerView) findViewById(R.id.pick_rv_photo);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        Bundle args = savedInstanceState;
        if (savedInstanceState == null) {
            args = getArguments();
        }
        mPhotoBean = args.getParcelable(PhotoPickConfig.KeyBundle.PHPTO_PICK);
        if (mPhotoBean == null) {
            mPhotoBean = new PhotoPickBean();
        }

        photoRecyclerView.setLayoutManager(new GridLayoutManager(mContext, mPhotoBean.getSpanCount()));

    }

    @Override
    protected void initListener() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(PhotoPickConfig.KeyBundle.PHPTO_PICK, mPhotoBean);
    }
}
