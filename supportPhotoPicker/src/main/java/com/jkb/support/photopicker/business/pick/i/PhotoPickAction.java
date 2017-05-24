package com.jkb.support.photopicker.business.pick.i;

import com.jkb.support.photopicker.business.action.PhotoPickerAction;

/**
 * 选择图片的Action
 * Created by yj on 2017/5/23.
 */

public interface PhotoPickAction extends PhotoPickerAction {

    /**
     * 图片单张被点击的监听事件
     */
    void addOnPhotoItemClickListener(OnPhotoItemClickListener listener);

    /**
     * 刷新照片
     */
    void refreshPhotos();
}
