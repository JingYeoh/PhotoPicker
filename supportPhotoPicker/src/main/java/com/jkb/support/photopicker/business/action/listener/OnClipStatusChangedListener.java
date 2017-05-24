package com.jkb.support.photopicker.business.action.listener;

import com.jkb.support.photopicker.bean.Photo;

/**
 * 裁剪状态变化的监听器
 * Created by yj on 2017/5/23.
 */

public interface OnClipStatusChangedListener {

    /**
     * 裁剪成功
     *
     * @param photo 裁剪成功的图片
     */
    void onClipSuccess(Photo photo);

    /**
     * 裁剪失败
     */
    void onClipFailed();

    /**
     * 裁剪取消
     */
    void onClipCancled();
}
