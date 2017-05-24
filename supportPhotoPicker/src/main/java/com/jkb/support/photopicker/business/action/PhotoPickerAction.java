package com.jkb.support.photopicker.business.action;

import com.jkb.support.photopicker.business.action.listener.PhotoPickerResultCallback;
import com.jkb.support.photopicker.business.action.listener.PhotoSelectStatusChangedListener;

/**
 * 图片裁剪类的动作接口
 * Created by yj on 2017/5/23.
 */

public interface PhotoPickerAction {

    /**
     * 选择完成
     */
    void pickDone();

    /**
     * 添加图片选择状态变化监听器
     */
    void addPhotoSelectStatusChangedListener(PhotoSelectStatusChangedListener listener);

    /**
     * 添加图片选择结果的回调
     */
    void addPhotoPickerResultCallback(PhotoPickerResultCallback callback);
}
