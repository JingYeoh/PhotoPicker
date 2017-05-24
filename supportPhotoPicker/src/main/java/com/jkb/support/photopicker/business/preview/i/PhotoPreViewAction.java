package com.jkb.support.photopicker.business.preview.i;

import com.jkb.support.photopicker.business.action.PhotoPickerAction;

/**
 * 图片预览的Action
 * Created by yj on 2017/5/23.
 */

public interface PhotoPreViewAction extends PhotoPickerAction {

    /**
     * 设置裁剪的点击监听事件
     */
    void addOnClipItemClickListener(OnPhotoClipItemClickListener listener);

    /**
     * 图片预览返回时结果的监听器
     */
    void addPhotoPreviewOnBackResultCallback(PhotoPreviewOnBackResultCallback callback);
}
