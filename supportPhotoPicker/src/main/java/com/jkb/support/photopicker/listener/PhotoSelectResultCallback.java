package com.jkb.support.photopicker.listener;

import java.util.List;

/**
 * 图片选择结果回调
 * Created by yj on 2017/5/17.
 */

public interface PhotoSelectResultCallback {
    /**
     * 图片选择结果
     *
     * @param paths 路径
     */
    void onPhotoSelectComplected(List<String> paths);
}
