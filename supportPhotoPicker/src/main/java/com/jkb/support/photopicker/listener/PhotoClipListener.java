package com.jkb.support.photopicker.listener;

/**
 * 图片裁剪监听器
 * Created by yj on 2017/5/16.
 */

public interface PhotoClipListener {
    /**
     * 裁剪取消
     */
    void onClipCancled();

    /**
     * 图片裁剪完毕
     */
    void onClipComplected();

    /**
     * 裁剪失败
     */
    void onClipFailed();
}
