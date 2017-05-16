package com.jkb.support.photopicker.listener;

/**
 * 选择图片数量变化时候的监听器
 * Created by yj on 2017/5/16.
 */

public interface PhotoPickChangedListener {
    /**
     * 图片选择数量变化
     *
     * @param sumCount      总数量
     * @param selectedCount 选择数量
     */
    void onPhotoPickChanged(int sumCount, int selectedCount);
}
