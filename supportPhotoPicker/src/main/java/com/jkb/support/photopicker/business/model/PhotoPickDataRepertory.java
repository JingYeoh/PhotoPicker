package com.jkb.support.photopicker.business.model;

import com.jkb.support.photopicker.bean.Photo;
import com.jkb.support.photopicker.bean.PhotoDirectory;

import java.util.List;

/**
 * 图片选择的数据仓库
 * Created by yj on 2017/5/17.
 */

public class PhotoPickDataRepertory implements PhotoPickDataSource {

    private PhotoPickDataSource localDataSource;

    private static PhotoPickDataRepertory sInstance = null;

    private PhotoPickDataRepertory(PhotoPickDataSource localDataSource) {

    }

    public static PhotoPickDataRepertory newInstance(PhotoPickDataSource localDataSource) {
        if (sInstance == null) {
            synchronized (PhotoPickDataRepertory.class) {
                if (sInstance == null) {
                    sInstance = new PhotoPickDataRepertory(localDataSource);
                }
            }
        }
        return sInstance;
    }

    @Override
    public void cacheExpired() {
        if (localDataSource != null) localDataSource.cacheExpired();
    }

    @Override
    public void destroy() {
        if (localDataSource != null) localDataSource.destroy();
        sInstance = null;
    }

    @Override
    public void loadPhotos(LoadDataCallBack<List<Photo>> callBack) {
        localDataSource.loadPhotos(callBack);
    }

    @Override
    public void loadGallery(LoadDataCallBack<List<PhotoDirectory>> callBack) {
        localDataSource.loadGallery(callBack);
    }
}
