package com.jkb.support.photopicker.business.preview.i;

import com.jkb.support.photopicker.bean.Photo;

import java.util.ArrayList;

/**
 * 图片预览点击返回的结果监听器
 * Created by yj on 2017/5/23.
 */

public interface PhotoPreviewOnBackResultCallback {
    /**
     * 图片预览点击返回的时候
     *
     * @param photos 照片
     */
    void onPreviewBackPressed(ArrayList<Photo> photos);
}
