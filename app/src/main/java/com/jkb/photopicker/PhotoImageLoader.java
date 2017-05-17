package com.jkb.photopicker;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jkb.support.photopicker.loader.ImageLoader;

/**
 * 图片加载类
 * Created by yj on 2017/5/17.
 */

public class PhotoImageLoader implements ImageLoader {
    @Override
    public void displayImage(Context context, String path, ImageView imageView, boolean resize) {
        DrawableRequestBuilder builder = null;
        builder = Glide.with(context)
                .load(path);
        if (resize)
            builder = builder.centerCrop();
        builder.crossFade()
                .error(context.getResources().getDrawable(R.mipmap.error_image))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
    }

    @Override
    public void clearMemoryCache() {

    }
}
