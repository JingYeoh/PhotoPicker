package com.jkb.support.photopicker.ui;

import android.os.Bundle;
import android.text.TextUtils;

import com.jkb.support.helper.SupportManager;
import com.jkb.support.photopicker.R;
import com.jkb.support.photopicker.bean.Photo;
import com.jkb.support.photopicker.bean.PhotoPickBean;
import com.jkb.support.photopicker.config.PhotoPickConfig;
import com.jkb.support.photopicker.listener.OnPhotoPickItemClickListener;
import com.jkb.support.photopicker.listener.PhotoSelectResultCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片选择器
 * Created by yj on 2017/5/16.
 */

public class PhotoPickerFragment extends BaseFragment implements PhotoSelectResultCallback,
        OnPhotoPickItemClickListener {

    public static PhotoPickerFragment newInstance(PhotoPickBean photoBean) {
        Bundle args = new Bundle();
        args.putParcelable(PhotoPickConfig.KeyBundle.PHOTO_PICK, photoBean);
        PhotoPickerFragment fragment = new PhotoPickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private PhotoPickBean mPhotoBean;
    private int pickerContent = R.id.pickerContent;
    //fragment
    private PhotoPickFragment mPhotoPickFragment;
    private String mPickTag;
    private PhotoPreViewFragment mPhotoPreViewFragment;
    private String mPreViewTag;

    @Override
    protected int getRootViewId() {
        return R.layout.frg_photo_picker;
    }

    @Override
    protected void initListener() {
        mPhotoPickFragment.addPhotoSelectResultCallback(this);
        mPhotoPickFragment.setOnPhotoPickItemClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            Bundle args = getArguments();
            mPhotoBean = args.getParcelable(PhotoPickConfig.KeyBundle.PHOTO_PICK);
            mPickTag = args.getString(PhotoPickConfig.KeyBundle.FRAGMENT_TAG_PICK, null);
        } else {
            mPhotoBean = savedInstanceState.getParcelable(PhotoPickConfig.KeyBundle.PHOTO_PICK);
        }
        initFragments();
        if (savedInstanceState == null) {
            showFragment(mPhotoPickFragment, pickerContent);
        }
    }

    @Override
    protected void initView() {

    }

    /**
     * 初始化Fragment
     */
    private void initFragments() {
        if (TextUtils.isEmpty(mPickTag)) {
            mPhotoPickFragment = PhotoPickFragment.newInstance(mPhotoBean);
        } else {
            mPhotoPickFragment = (PhotoPickFragment) SupportManager.getFragment(mChildFm, mPickTag);
            mPickTag = mPhotoPickFragment.getFragmentTAG();
        }
        if (!TextUtils.isEmpty(mPreViewTag)) {
            mPhotoPreViewFragment = (PhotoPreViewFragment) SupportManager.getFragment(mChildFm, mPreViewTag);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(PhotoPickConfig.KeyBundle.PHOTO_PICK, mPhotoBean);
        outState.putString(PhotoPickConfig.KeyBundle.FRAGMENT_TAG_PICK, mPickTag);
        outState.putString(PhotoPickConfig.KeyBundle.FRAGMENT_TAG_PREVIEW, mPreViewTag);
    }

    @Override
    public void onPhotoSelectComplected(List<String> paths) {

    }

    @Override
    public void onPhotoItemClick(int position, List<Photo> photos) {
        mPhotoPreViewFragment = PhotoPreViewFragment.newInstance(position, (ArrayList<Photo>) photos, mPhotoBean);
        mPreViewTag = mPhotoPreViewFragment.getFragmentTAG();
        showFragment(mPhotoPreViewFragment, pickerContent);
    }
}
