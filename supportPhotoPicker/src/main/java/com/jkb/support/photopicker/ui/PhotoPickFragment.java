package com.jkb.support.photopicker.ui;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jkb.support.photopicker.R;
import com.jkb.support.photopicker.adapter.GalleryAdapter;
import com.jkb.support.photopicker.adapter.PhotoPickAdapter;
import com.jkb.support.photopicker.bean.Photo;
import com.jkb.support.photopicker.bean.PhotoDirectory;
import com.jkb.support.photopicker.bean.PhotoPickBean;
import com.jkb.support.photopicker.config.PhotoPickConfig;
import com.jkb.support.photopicker.helper.PermissionHelper;
import com.jkb.support.photopicker.helper.PhotoPickHelper;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import java.util.List;

/**
 * 图片选择器
 * Created by yj on 2017/5/16.
 */

public class PhotoPickFragment extends BaseFragment implements GalleryAdapter.OnGalleryItemClickListener, View
        .OnClickListener, PhotoPickAdapter.OnPhotoItemClickListener {

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
    private PhotoPickAdapter photoPickAdapter;
    private GalleryAdapter galleryAdapter;

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
        slidingUpPanelLayout.setAnchorPoint(0.5f);
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
        //初始化适配器及其他
        photoRecyclerView.setLayoutManager(new GridLayoutManager(mContext, mPhotoBean.getSpanCount()));
        photoPickAdapter = new PhotoPickAdapter(mContext, mPhotoBean);
        photoRecyclerView.setAdapter(photoPickAdapter);
        galleryAdapter = new GalleryAdapter(mContext, mPhotoBean);
        galleryRecyclerView.setAdapter(galleryAdapter);
        //申请权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermission();
        } else {
            loadPhoto();//加载图片
        }
    }

    @Override
    protected void initListener() {
        galleryAdapter.setOnGalleryItemClickListener(this);
        photoPickAdapter.setOnPhotoItemClickListener(this);
        slidingUpPanelLayout.setFadeOnClickListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(PhotoPickConfig.KeyBundle.PHPTO_PICK, mPhotoBean);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    /**
     * 请求权限
     */
    private void requestPermission() {
        if (!PermissionHelper.checkPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            PermissionHelper.requestPermission(this, PhotoPickConfig.PermissionRequestCode.SDCARD,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
        } else {
            loadPhoto();
        }
    }

    //读写权限请求成功
    @PermissionGrant(PhotoPickConfig.PermissionRequestCode.SDCARD)
    public void requestSdcardSuccess() {
        loadPhoto();
    }

    //读写权限请求失败
    @PermissionDenied(PhotoPickConfig.PermissionRequestCode.SDCARD)
    public void requestSdcardFailed() {
        PermissionHelper.showSystemSettingDialog(mContext, getString(R.string.permission_tip_SD),
                new PermissionHelper.OnSystemSettingDialogListener() {
                    @Override
                    public void onCancelClick() {
                        closeCurrentAndShowPopFragment();
                    }
                });
    }

    //相机权限请求成功
    @PermissionGrant(PhotoPickConfig.PermissionRequestCode.CAMERA)
    public void requestCameraSuccess() {
        launchCameraToSelectPhoto();
    }

    //相机权限请求失败
    @PermissionDenied(PhotoPickConfig.PermissionRequestCode.CAMERA)
    public void requestCameraFailed() {
        PermissionHelper.showSystemSettingDialog(mContext, getString(R.string.permission_tip_SD),
                new PermissionHelper.OnSystemSettingDialogListener() {
                    @Override
                    public void onCancelClick() {
                        closeCurrentAndShowPopFragment();
                    }
                });
    }

    /**
     * 加载图片
     */
    private void loadPhoto() {
        PhotoPickHelper.getPhotoDirs(mContext, new PhotoPickHelper.PhotosResultCallback() {
            @Override
            public void onResultCallback(final List<PhotoDirectory> directories) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        photoPickAdapter.refresh(directories.get(0).getPhotos());
                        galleryAdapter.refresh(directories);
                    }
                });
            }
        });
    }

    @Override
    public void onGalleryItemClick(List<Photo> photos) {
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        photoPickAdapter.refresh(photos);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.pick_supl) {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
    }

    @Override
    public void onBackPressed() {
        if (slidingUpPanelLayout != null &&
                (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED ||
                        slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onPhotoItemClick(int position) {//图片被点击

    }

    @Override
    public void onCameraClick() {//调用系统相机拍照

    }

    /**
     * 启动相机并选择图片
     */
    private void launchCameraToSelectPhoto() {

    }
}
