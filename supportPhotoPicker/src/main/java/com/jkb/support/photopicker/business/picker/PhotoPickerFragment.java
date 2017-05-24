package com.jkb.support.photopicker.business.picker;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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
import com.jkb.support.photopicker.utils.ImageUtils;
import com.jkb.support.photopicker.utils.UCropUtils;
import com.jkb.support.utils.LogUtils;
import com.yalantis.ucrop.UCrop;

import java.io.File;
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
    private PhotoPreviewFragment mPreviewFragment;
    private String mPreTag;
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
        initPreviewFragment();
        if (savedInstanceState == null) {
            showFragment(mPhotoPickFragment, pickerContent);
        }
    }

    /**
     * 初始化预览
     */
    private void initPreviewFragment() {
        if (!TextUtils.isEmpty(mPreTag)) {
            mPreviewFragment = (PhotoPreviewFragment) SupportManager.getFragment(mChildFm, mPreTag);
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
        outState.putString(PhotoPickConfig.KeyBundle.FRAGMENT_TAG_PREVIEW, mPreTag);
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
        mPreviewFragment = PhotoPreviewFragment.newInstance(position, photos, mPhotoBean);
        mPreTag = mPreviewFragment.getFragmentTAG();
        //设置监听器等
        mPreviewFragment.addPhotoPreviewOnBackResultCallback(this);
        mPreviewFragment.addOnClipItemClickListener(this);
        mPreviewFragment.addPhotoSelectStatusChangedListener(this);
        //显示
        showFragment(mPreviewFragment, pickerContent);
    }

    @Override
    public void onPreviewBackPressed(ArrayList<Photo> photos) {
        if (mPhotoPickFragment != null) mPhotoPickFragment.refreshPhotos();
    }

    @Override
    public void onPhotoClipItemClick(int position, Photo photo) {
        showClip(photo);
    }

    /*待裁剪的图片*/
    private Photo cropPhoto;

    /**
     * 显示图片裁剪
     */
    private void showClip(Photo photo) {
        cropPhoto = photo;
        String imagePath = ImageUtils.getImagePath(mContext, "/Crop/");
        File corpFile = new File(imagePath + ImageUtils.createFile());
        LogUtils.d(this, "clip path=" + photo.getPath());
        UCropUtils.start(this, mContext, new File(photo.getPath()), corpFile,
                mPhotoBean.getClipMode() == PhotoPickConfig.ClipMode.CIRCLE,
                mPhotoBean.getClipActionBarBackgroundColor());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        switch (requestCode) {
            case UCrop.REQUEST_CROP:
                handleClipResult(UCrop.getOutput(data));
                break;
            case UCrop.RESULT_ERROR:
                Throwable cropError = UCrop.getError(data);
                LogUtils.d(this, "裁剪失败:" + cropError.getMessage());
                break;
        }
    }

    /**
     * 处理裁剪结果
     */
    private void handleClipResult(Uri output) {
        LogUtils.d(this, "裁剪成功：" + output.getPath());
        cropPhoto.setPath(output.getPath());
        if (mPhotoPickFragment != null) mPhotoPickFragment.refreshPhotos();
        if (mPreviewFragment != null) mPreviewFragment.refreshPhotos();
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
