package com.jkb.support.photopicker.listener;

/**
 * 图片选择回调接口
 * Created by yj on 2017/5/16.
 */

public interface PhotoPickListener {
    /**
     * 选择取消
     */
    void onPickCancled();

    /**
     * 选择完毕
     */
    void onPickComplected();

    /**
     * 选择失败
     */
    void onPickFailed();
}
