package com.jkb.support.photopicker.base;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jkb.support.photopicker.R;
import com.jkb.support.photopicker.config.PhotoPickConfig;
import com.jkb.support.photopicker.helper.PermissionHelper;
import com.jkb.support.ui.SupportFragment;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

/**
 * Fragment的基类
 * Created by yj on 2017/5/16.
 */

public abstract class PhotoPickBaseFragment extends SupportFragment {
    //view
    protected View rootView;
    private OnCameraPermissionCallback onCameraPermissionCallback;
    private OnStoragePermissionCallback onStoragePermissionCallback;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(getRootViewId(), container, false);
        return rootView;
    }

    /**
     * 返回根视图id
     */
    @LayoutRes
    protected abstract int getRootViewId();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void init(Bundle savedInstanceState) {
        initView();
        initData(savedInstanceState);
        initListener();
    }

    /**
     * 初始化视图
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData(Bundle savedInstanceState);

    /**
     * 初始化监听器
     */
    protected abstract void initListener();

    public View findViewById(@IdRes int id) {
        return rootView.findViewById(id);
    }

    public void showShortToast(String value) {
        if (TextUtils.isEmpty(value)) return;
        Toast.makeText(mContext, value, Toast.LENGTH_SHORT).show();
    }

    /**
     * 检查相机权限
     */
    protected void checkCameraPermission(@Nullable OnCameraPermissionCallback callback) {
        onCameraPermissionCallback = callback;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onCameraPermissionCallback.cameraPermissionAllowed();
            return;
        }
        if (!PermissionHelper.checkPermission(mContext, Manifest.permission.CAMERA)) {
            PermissionHelper.requestPermission(this, PhotoPickConfig.RequestCode.PERMISSION_CAMERA,
                    Manifest.permission.CAMERA);
        } else {
            onCameraPermissionCallback.cameraPermissionAllowed();
        }
    }

    /**
     * 检查读取存储权限
     */
    protected void checkStoragePermission(@Nullable OnStoragePermissionCallback callback) {
        onStoragePermissionCallback = callback;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onStoragePermissionCallback.storagePermissionAllowed();
            return;
        }
        if (!PermissionHelper.checkPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            PermissionHelper.requestPermission(this, PhotoPickConfig.RequestCode.PERMISSION_SDCARD,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
        } else {
            onStoragePermissionCallback.storagePermissionAllowed();
        }
    }

    //读写权限请求成功
    @PermissionGrant(PhotoPickConfig.RequestCode.PERMISSION_SDCARD)
    public void requestSdcardSuccess() {
        onStoragePermissionCallback.storagePermissionAllowed();
    }

    //读写权限请求失败
    @PermissionDenied(PhotoPickConfig.RequestCode.PERMISSION_SDCARD)
    public void requestSdcardFailed() {
        PermissionHelper.showSystemSettingDialog(mContext, getString(R.string.permission_tip_SD),
                new PermissionHelper.OnSystemSettingDialogListener() {
                    @Override
                    public void onCancelClick() {
                        onStoragePermissionCallback.storagePermissionDenied();
                    }
                });
    }

    //相机权限请求成功
    @PermissionGrant(PhotoPickConfig.RequestCode.PERMISSION_CAMERA)
    public void cameraPermissionAllowed() {
        onCameraPermissionCallback.cameraPermissionAllowed();
    }

    //相机权限请求失败
    @PermissionDenied(PhotoPickConfig.RequestCode.PERMISSION_CAMERA)
    public void requestCameraFailed() {
        PermissionHelper.showSystemSettingDialog(mContext, getString(R.string.permission_tip_SD),
                new PermissionHelper.OnSystemSettingDialogListener() {
                    @Override
                    public void onCancelClick() {
                        onCameraPermissionCallback.cameraPermissionDenied();
                    }
                });
    }
}
