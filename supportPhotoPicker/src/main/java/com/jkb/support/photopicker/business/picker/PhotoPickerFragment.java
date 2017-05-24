package com.jkb.support.photopicker.business.picker;

import android.os.Bundle;
import android.text.TextUtils;

import com.jkb.support.helper.SupportManager;
import com.jkb.support.photopicker.R;
import com.jkb.support.photopicker.base.PhotoPickBaseFragment;
import com.jkb.support.photopicker.bean.Photo;
import com.jkb.support.photopicker.bean.PhotoPickBean;
import com.jkb.support.photopicker.business.action.PhotoPickerAction;
import com.jkb.support.photopicker.business.action.listener.PhotoPickerResultCallback;
import com.jkb.support.photopicker.business.action.listener.PhotoSelectStatusChangedListener;
import com.jkb.support.photopicker.business.pick.PhotoPickFragment;
import com.jkb.support.photopicker.business.pick.i.OnPhotoItemClickListener;
import com.jkb.support.photopicker.business.preview.PhotoPreviewFragment;
import com.jkb.support.photopicker.business.preview.i.OnPhotoClipItemClickListener;
import com.jkb.support.photopicker.business.preview.i.PhotoPreviewOnBackResultCallback;
import com.jkb.support.photopicker.config.PhotoPickConfig;

import java.util.ArrayList;

/**
 * 选择图片：功能组合
 * Created by yj on 2017/5/24.
 */

public class PhotoPickerFragment extends PhotoPickBaseFragment implements PhotoPickerAction, OnPhotoItemClickListener,
        PhotoPreviewOnBackResultCallback, OnPhotoClipItemClickListener, PhotoSelectStatusChangedListener,
        PhotoPickerResultCallback {

    public static PhotoPickerFragment newInstance(PhotoPickBean photoBean) {
        Bundle args = new Bundle();
        args.putParcelable(PhotoPickConfig.KeyBundle.PHOTO_PICK, photoBean);
        PhotoPickerFragment fragment = new PhotoPickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //data
    private PhotoPickBean mPhotoBean;
    private int pickerContent = R.id.pickerContent;
    //fragment
    private PhotoPickFragment mPhotoPickFragment;
    private String mPickTag;
    //listener
    private PhotoSelectStatusChangedListener photoSelectStatusChangedListener;
    private PhotoPickerResultCallback photoPickerResultCallback;

    @Override
    protected int getRootViewId() {
        return R.layout.frg_photo_picker;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            Bundle args = getArguments();
            mPhotoBean = args.getParcelable(PhotoPickConfig.KeyBundle.PHOTO_PICK);
        } else {
            mPhotoBean = savedInstanceState.getParcelable(PhotoPickConfig.KeyBundle.PHOTO_PICK);
        }
        initPickFragment();
        if (savedInstanceState == null) {
            showFragment(mPhotoPickFragment, pickerContent);
        }
    }

    /**
     * 显示选择图片
     */
    private void initPickFragment() {
        if (TextUtils.isEmpty(mPickTag)) {
            mPhotoPickFragment = PhotoPickFragment.newInstance(mPhotoBean);
            mPickTag = mPhotoPickFragment.getFragmentTAG();
        } else {
            mPhotoPickFragment = (PhotoPickFragment) SupportManager.getFragment(mChildFm, mPickTag);
        }
    }

    @Override
    protected void initListener() {
        mPhotoPickFragment.addOnPhotoItemClickListener(this);
        mPhotoPickFragment.addPhotoSelectStatusChangedListener(this);
        mPhotoPickFragment.addPhotoPickerResultCallback(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(PhotoPickConfig.KeyBundle.PHOTO_PICK, mPhotoBean);
        outState.putString(PhotoPickConfig.KeyBundle.FRAGMENT_TAG_PICK, mPickTag);
    }

    @Override
    public void onPhotoItemClick(int position, ArrayList<Photo> photos) {
        showPreView(position, photos);
    }

    @Override
    public void onPhotoSelectChanged(int sumCount, int selectedCount) {
        if (photoSelectStatusChangedListener != null) {
            photoSelectStatusChangedListener.onPhotoSelectChanged(sumCount, selectedCount);
        }
    }

    @Override
    public void onPhotoPickSuccess(ArrayList<String> photoPaths) {
        photoPickerResultCallback.onPhotoPickSuccess(photoPaths);
    }

    @Override
    public void onPhotoPickFailed() {
        photoPickerResultCallback.onPhotoPickFailed();
    }

    /**
     * 显示图片预览
     */
    private void showPreView(int position, ArrayList<Photo> photos) {
        PhotoPreviewFragment preViewFragment = PhotoPreviewFragment.newInstance(position, photos, mPhotoBean);
        //设置监听器等
        preViewFragment.addPhotoPreviewOnBackResultCallback(this);
        preViewFragment.addOnClipItemClickListener(this);
        preViewFragment.addPhotoSelectStatusChangedListener(this);
        //显示
        showFragment(preViewFragment, pickerContent);
    }

    @Override
    public void onPreviewBackPressed(ArrayList<Photo> photos) {
        if (mPhotoPickFragment != null) {
            mPhotoPickFragment.refreshPhotos();
        }
    }

    @Override
    public void onPhotoClipItemClick(int position, Photo photo) {
        showClip(position, photo);
    }

    /**
     * 显示图片裁剪
     */
    private void showClip(int position, Photo photo) {

    }

    ///////////////////////////////////////PickerAction///////////////////////////////
    @Override
    public void pickDone() {
        mPhotoPickFragment.pickDone();
    }

    @Override
    public void addPhotoSelectStatusChangedListener(PhotoSelectStatusChangedListener listener) {
        photoSelectStatusChangedListener = listener;
    }

    @Override
    public void addPhotoPickerResultCallback(PhotoPickerResultCallback callback) {
        photoPickerResultCallback = callback;
    }
    ///////////////////////////////////////PickerAction///////////////////////////////
}
