package com.jkb.support.photopicker.listener;

import com.jkb.support.photopicker.bean.Photo;

import java.util.List;

/**
 * 选择照片条目点击监听事件
 * Created by yj on 2017/5/22.
 */

public interface OnPhotoPickItemClickListener {

    /**
     * 图片条目点击监听事件
     *
     * @param position 点击的图片条目
     * @param photos   当前相册中的图片
     */
    void onPhotoItemClick(int position, List<Photo> photos);
}
