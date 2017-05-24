package com.jkb.support.photopicker.base;

/**
 * 存储权限的回调
 * Created by yj on 2017/5/23.
 */

public interface OnStoragePermissionCallback {
    /**
     * 请求存储权限成功
     */
    void storagePermissionAllowed();

    /**
     * 请求存储权限失败
     */
    void storagePermissionDenied();
}
