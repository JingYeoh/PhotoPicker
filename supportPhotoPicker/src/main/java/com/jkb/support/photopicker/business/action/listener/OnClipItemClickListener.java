package com.jkb.support.photopicker.business.action.listener;

import com.jkb.support.photopicker.bean.Photo;

/**
 * 图片裁剪的条目点击监听器
 * Created by yj on 2017/5/23.
 */

public interface OnClipItemClickListener {

    /**
     * 裁剪条目被点击
     *
     * @param photo 需要裁剪的图片
     */
    void onClipItemClick(Photo photo);
}
