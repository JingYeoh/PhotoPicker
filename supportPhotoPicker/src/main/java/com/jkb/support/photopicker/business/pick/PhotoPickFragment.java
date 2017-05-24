package com.jkb.support.photopicker.business.pick;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.jkb.support.photopicker.R;
import com.jkb.support.photopicker.base.OnCameraPermissionCallback;
import com.jkb.support.photopicker.base.OnStoragePermissionCallback;
import com.jkb.support.photopicker.base.PhotoPickBaseFragment;
import com.jkb.support.photopicker.bean.PhotoDirectory;
import com.jkb.support.photopicker.bean.PhotoPickBean;
import com.jkb.support.photopicker.business.action.listener.PhotoPickerResultCallback;
import com.jkb.support.photopicker.business.action.listener.PhotoSelectStatusChangedListener;
import com.jkb.support.photopicker.business.pick.i.OnPhotoItemClickListener;
import com.jkb.support.photopicker.business.pick.i.PhotoPickAction;
import com.jkb.support.photopicker.config.PhotoPickConfig;
import com.jkb.support.photopicker.helper.PhotoPickHelper;
import com.jkb.support.photopicker.utils.UtilsHelper;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * 图片选择的基类
 * Created by yj on 2017/5/23.
 */

public class PhotoPickFragment extends PhotoPickBaseFragment implements PhotoPickAction, OnStoragePermissionCallback,
        OnCameraPermissionCallback {

    public static PhotoPickFragment newInstance(@Nullable PhotoPickBean photoBean) {
        Bundle args = new Bundle();
        args.putParcelable(PhotoPickConfig.KeyBundle.PHOTO_PICK, photoBean);
        PhotoPickFragment fragment = new PhotoPickFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //data
    private PhotoPickBean mPhotoBean;
    private ArrayList<PhotoDirectory> mPhotoDirectories;
    private Uri mCameraTemporaryUri;

    //ui
    private SlidingUpPanelLayout slidingUpPanelLayout;
    private RecyclerView rvPhotos;
    private RecyclerView rvGallery;
    private PickPhotoAdapter photoAdapter;
    private PickGalleryAdapter galleryAdapter;

    //listener
    private OnPhotoItemClickListener onPhotoItemClickListener;
    private PhotoSelectStatusChangedListener photoSelectStatusChangedListener;
    private PhotoPickerResultCallback photoPickerResultCallback;

    @Override
    protected int getRootViewId() {
        return R.layout.frg_photo_pick;
    }

    @Override
    protected void initView() {
        slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.pick_supl);
        slidingUpPanelLayout.setAnchorPoint(0.5f);
        //相册
        rvGallery = (RecyclerView) findViewById(R.id.pick_rv_gallery);
        rvGallery.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        //图片
        rvPhotos = (RecyclerView) findViewById(R.id.pick_rv_photo);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        //恢复数据
        Bundle args = savedInstanceState;
        if (savedInstanceState == null) args = getArguments();
        mPhotoBean = args.getParcelable(PhotoPickConfig.KeyBundle.PHOTO_PICK);
        //初始化数据
        mPhotoDirectories = new ArrayList<>();
        //初始化适配器及其他
        rvPhotos.setLayoutManager(new GridLayoutManager(mContext, mPhotoBean.getSpanCount()));
        if (photoAdapter == null) photoAdapter = new PickPhotoAdapter(mContext, mPhotoBean);
        rvPhotos.setAdapter(photoAdapter);
        if (galleryAdapter == null) galleryAdapter = new PickGalleryAdapter(mContext, mPhotoBean);
        rvGallery.setAdapter(galleryAdapter);
        //检查权限
        checkStoragePermission();

    }

    @Override
    protected void initListener() {
        slidingUpPanelLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });
        galleryAdapter.setOnGalleryItemClickListener(new PickGalleryAdapter.OnGalleryItemClickListener() {
            @Override
            public void onGalleryItemClick(int position) {
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                photoAdapter.refresh(mPhotoDirectories.get(position).getPhotos());
            }
        });
        photoAdapter.setOnPhotoItemClickListener(new PickPhotoAdapter.OnPhotoItemClickListener() {
            @Override
            public void onPhotoItemClick(int position) {
                if (onPhotoItemClickListener != null) {
                    onPhotoItemClickListener.onPhotoItemClick(position, photoAdapter.photos);
                }
            }

            @Override
            public void onCameraClick() {
                checkCameraPermission();
            }
        });
        photoAdapter.setPhotoSelectStatusChangedListener(photoSelectStatusChangedListener);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(PhotoPickConfig.KeyBundle.PHOTO_PICK, mPhotoBean);
    }

    @Override
    public void addOnPhotoItemClickListener(OnPhotoItemClickListener listener) {
        onPhotoItemClickListener = listener;
    }

    @Override
    public void refreshPhotos() {
        galleryAdapter.refresh(mPhotoDirectories);
        photoAdapter.refresh(mPhotoDirectories.get(galleryAdapter.selected).getPhotos());
    }

    @Override
    public void pickDone() {
        if (photoPickerResultCallback == null) return;
        if (photoAdapter.selectPhotos.isEmpty()) {
            photoPickerResultCallback.onPhotoPickFailed();
        } else {
            photoPickerResultCallback.onPhotoPickSuccess(photoAdapter.selectPhotos);
        }
    }

    @Override
    public void addPhotoSelectStatusChangedListener(PhotoSelectStatusChangedListener listener) {
        photoSelectStatusChangedListener = listener;
    }

    @Override
    public void addPhotoPickerResultCallback(PhotoPickerResultCallback callback) {
        photoPickerResultCallback = callback;
    }

    @Override
    public void storagePermissionAllowed() {
        loadPhotos();
    }

    @Override
    public void storagePermissionDenied() {
        loadPhotos();
    }

    @Override
    public void cameraPermissionAllowed() {
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
    public void cameraPermissionDenied() {
    }

    /**
     * 加载图片
     */
    private void loadPhotos() {
        PhotoPickHelper.getPhotoDirs(mContext, new PhotoPickHelper.PhotosResultCallback() {
            @Override
            public void onResultCallback(final List<PhotoDirectory> directories) {
                mPhotoDirectories = (ArrayList<PhotoDirectory>) directories;
                loadPhotosToAdapter();//加载数据到适配器中
            }
        });
    }

    /**
     * 检查存储权限
     */
    private void checkStoragePermission() {
        checkStoragePermission(this);
    }

    /**
     * 显示相机
     */
    private void checkCameraPermission() {
        checkCameraPermission(this);
    }

    /**
     * 加载数据到适配器中
     */
    private void loadPhotosToAdapter() {
        if (mActivity == null || mPhotoDirectories.isEmpty()) return;
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                refreshPhotos();
            }
        });
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
            checkStoragePermission();//刷新存储卡
        }
    }
}
