package com.jkb.support.photopicker.business.action.listener;

/**
 * 图片选择状态变化的监听器
 * Created by yj on 2017/5/23.
 */

public interface PhotoSelectStatusChangedListener {

    /**
     * 图片选择状态变化
     *
     * @param sumCount      图片总数
     * @param selectedCount 选中的图片数量
     */
    void onPhotoSelectChanged(int sumCount, int selectedCount);
}
