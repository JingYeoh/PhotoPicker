package com.jkb.support.photopicker.business.pick.i;

import com.jkb.support.photopicker.bean.Photo;

import java.util.ArrayList;

/**
 * 图片单张被点击的监听器
 * Created by yj on 2017/5/23.
 */

public interface OnPhotoItemClickListener {

    /**
     * 图片条目被点击
     *
     * @param position 被点击的条目
     * @param photos   相册中的图片
     */
    void onPhotoItemClick(int position, ArrayList<Photo> photos);
}
