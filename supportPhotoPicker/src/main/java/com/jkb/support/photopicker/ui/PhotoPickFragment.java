package com.jkb.support.photopicker.ui;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.jkb.support.photopicker.R;
import com.jkb.support.photopicker.adapter.GalleryAdapter;
import com.jkb.support.photopicker.adapter.PhotoPickAdapter;
import com.jkb.support.photopicker.bean.Photo;
import com.jkb.support.photopicker.bean.PhotoDirectory;
import com.jkb.support.photopicker.bean.PhotoPickBean;
import com.jkb.support.photopicker.config.PhotoPickConfig;
import com.jkb.support.photopicker.helper.PermissionHelper;
import com.jkb.support.photopicker.helper.PhotoPickHelper;
import com.jkb.support.photopicker.listener.OnPhotoPickItemClickListener;
import com.jkb.support.photopicker.listener.PhotoSelectResultCallback;
import com.jkb.support.photopicker.utils.UtilsHelper;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * 图片选择器
 * Created by yj on 2017/5/16.
 */

public class PhotoPickFragment extends BaseFragment implements GalleryAdapter.OnGalleryItemClickListener, View
        .OnClickListener, PhotoPickAdapter.OnPhotoItemClickListener {

    public static PhotoPickFragment newInstance(PhotoPickBean photoBean) {
        Bundle args = new Bundle();
        args.putParcelable(PhotoPickConfig.KeyBundle.PHOTO_PICK, photoBean);
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
    private Uri mCameraTemporaryUri;

    //listener
    private PhotoSelectResultCallback photoSelectResultCallback;
    private OnPhotoPickItemClickListener photoPickItemClickListener;

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
        mPhotoBean = args.getParcelable(PhotoPickConfig.KeyBundle.PHOTO_PICK);
        if (mPhotoBean == null) {
            mPhotoBean = new PhotoPickBean();
        }
        //初始化适配器及其他
        photoRecyclerView.setLayoutManager(new GridLayoutManager(mContext, mPhotoBean.getSpanCount()));
        if (photoPickAdapter == null) photoPickAdapter = new PhotoPickAdapter(mContext, mPhotoBean);
        photoRecyclerView.setAdapter(photoPickAdapter);
        if (galleryAdapter == null) galleryAdapter = new GalleryAdapter(mContext, mPhotoBean);
        galleryRecyclerView.setAdapter(galleryAdapter);

        startLoadPhoto();  //申请权限
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
        outState.putParcelable(PhotoPickConfig.KeyBundle.PHOTO_PICK, mPhotoBean);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    /**
     * 检查存储权限
     */
    public void startLoadPhoto() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            loadPhoto();
            return;
        }
        if (!PermissionHelper.checkPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            PermissionHelper.requestPermission(this, PhotoPickConfig.RequestCode.PERMISSION_SDCARD,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
        } else {
            loadPhoto();
        }
    }

    /**
     * 检查相机权限
     */
    private void checkCameraPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            launchCameraToSelectPhoto();
            return;
        }
        if (!PermissionHelper.checkPermission(mContext, Manifest.permission.CAMERA)) {
            PermissionHelper.requestPermission(this, PhotoPickConfig.RequestCode.PERMISSION_CAMERA,
                    Manifest.permission.CAMERA);
        } else {
            launchCameraToSelectPhoto();
        }
    }

    //读写权限请求成功
    @PermissionGrant(PhotoPickConfig.RequestCode.PERMISSION_SDCARD)
    public void requestSdcardSuccess() {
        loadPhoto();
    }

    //读写权限请求失败
    @PermissionDenied(PhotoPickConfig.RequestCode.PERMISSION_SDCARD)
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
    @PermissionGrant(PhotoPickConfig.RequestCode.PERMISSION_CAMERA)
    public void requestCameraSuccess() {
        launchCameraToSelectPhoto();
    }

    //相机权限请求失败
    @PermissionDenied(PhotoPickConfig.RequestCode.PERMISSION_CAMERA)
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
                if (mActivity == null) return;
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
        if (photoPickItemClickListener == null || photoPickAdapter == null) return;
        photoPickItemClickListener.onPhotoItemClick(position, photoPickAdapter.getPhotos());
    }

    @Override
    public void onCameraClick() {//调用系统相机拍照
        checkCameraPermission();
    }

    /**
     * 启动相机并选择图片
     */
    private void launchCameraToSelectPhoto() {
        if (!android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            Toast.makeText(mContext, R.string.cannot_take_pic, Toast.LENGTH_SHORT).show();
            return;
        }
        // 直接将拍到的照片存到手机默认的文件夹
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ContentValues values = new ContentValues();
        mCameraTemporaryUri = mContext.getContentResolver()
                .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraTemporaryUri);
        startActivityForResult(intent, PhotoPickConfig.RequestCode.CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case PhotoPickConfig.RequestCode.CAMERA://相机
                handleCameraResult();
                break;
        }
    }

    /**
     * 处理相机返回结果
     */
    private void handleCameraResult() {
        String filePath = UtilsHelper.getRealPathFromURI(mCameraTemporaryUri, mContext);
        showShortToast(filePath);
        if (TextUtils.isEmpty(filePath)) {
            Toast.makeText(mContext, R.string.unable_find_pic, Toast.LENGTH_LONG).show();
        } else {
            startLoadPhoto();
        }
    }

    /**
     * 添加图片选择结果的回调
     */
    public void addPhotoSelectResultCallback(PhotoSelectResultCallback callback) {
        photoSelectResultCallback = callback;
    }

    /**
     * 设置图片条目的点击监听事件
     */
    public void setOnPhotoPickItemClickListener(OnPhotoPickItemClickListener listener) {
        photoPickItemClickListener = listener;
    }

    /**
     * 完成选择
     */
    public void pickDone() {
        if (photoSelectResultCallback == null) return;
        if (photoPickAdapter == null) return;
        photoSelectResultCallback.onPhotoSelectComplected(photoPickAdapter.getSelectPhotos());
    }
}
