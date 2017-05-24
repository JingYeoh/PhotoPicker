package com.jkb.support.photopicker.business.preview.i;

import com.jkb.support.photopicker.bean.Photo;

/**
 * 图片裁剪条目点击监听
 * Created by yj on 2017/5/23.
 */

public interface OnPhotoClipItemClickListener {

    /**
     * 图片裁剪条目被点击
     *
     * @param position 需要裁剪的条目
     * @param photo    需要裁剪的图片对象
     */
    void onPhotoClipItemClick(int position, Photo photo);
}
