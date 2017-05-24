package com.jkb.support.photopicker.base;

/**
 * 相机权限的回调
 * Created by yj on 2017/5/23.
 */

public interface OnCameraPermissionCallback {
    /**
     * 请求相机权限成功
     */
    void cameraPermissionAllowed();

    /**
     * 请求相机权限失败
     */
    void cameraPermissionDenied();
}
