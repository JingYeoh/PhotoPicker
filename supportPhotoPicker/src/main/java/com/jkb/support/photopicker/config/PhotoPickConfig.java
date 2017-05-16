package com.jkb.support.photopicker.config;

/**
 * 图片选择器的配置类
 * Created by yj on 2017/5/16.
 */

public interface PhotoPickConfig {

    /**
     * 图片选择模式
     */
    interface PickMode {
        int SINGLE = 1001;//单张
        int MULTI = 1002;//多张
    }

    /**
     * 图片裁剪模式
     */
    interface ClipMode {
        int CIRCLE = 1001;//圆形
        int RECTANGLE = 1002;//方形
    }

    interface KeyBundle {
        String PHPTO_PICK = "photoPick";
    }
}
