package com.jkb.support.photopicker.business.model.local;

import android.content.Context;

import com.jkb.support.photopicker.bean.Photo;
import com.jkb.support.photopicker.bean.PhotoDirectory;
import com.jkb.support.photopicker.business.model.PhotoPickDataSource;

import java.util.List;

/**
 * 图片本地数据来源类
 * Created by yj on 2017/5/17.
 */

public class PhotoPickLocalDataSource implements PhotoPickDataSource {

    private Context context;

    private static PhotoPickLocalDataSource sInstance;

    private PhotoPickLocalDataSource(Context context) {
        this.context = context;
    }

    public static PhotoPickLocalDataSource newInstance(Context context) {
        if (sInstance == null) {
            synchronized (PhotoPickLocalDataSource.class) {
                if (sInstance == null) {
                    sInstance = new PhotoPickLocalDataSource(context);
                }
            }
        }
        return sInstance;
    }

    @Override
    public void cacheExpired() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void loadPhotos(LoadDataCallBack<List<Photo>> callBack) {

    }

    @Override
    public void loadGallery(LoadDataCallBack<List<PhotoDirectory>> callBack) {

    }
}
