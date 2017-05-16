package com.jkb.support.photopicker.loader;

import android.content.Context;
import android.widget.ImageView;

/**
 * 图片的加载接口
 * Created by yj on 2017/5/16.
 */

public interface ImageLoader {

    /**
     * 加载图片
     *
     * @param context   上下文
     * @param path      本地路径
     * @param imageView imageView
     * @param resize    是否原图
     */
    void displayImage(Context context, String path, ImageView imageView, boolean resize);

    /**
     * 清楚缓存
     */
    void clearMemoryCache();
}
