package com.jkb.support.photopicker.business.action.listener;

import java.util.ArrayList;

/**
 * 图片选择器的结果回调
 * Created by yj on 2017/5/23.
 */

public interface PhotoPickerResultCallback {

    /**
     * 图片选择成功
     *
     * @param photoPaths 选择的图片路径
     */
    void onPhotoPickSuccess(ArrayList<String> photoPaths);

    /**
     * 图片选择失败
     */
    void onPhotoPickFailed();
}
