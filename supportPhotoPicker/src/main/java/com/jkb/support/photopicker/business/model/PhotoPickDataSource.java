package com.jkb.support.photopicker.business.model;

import com.jkb.support.photopicker.bean.Photo;
import com.jkb.support.photopicker.bean.PhotoDirectory;
import com.jkb.support.photopicker.business.BaseModel;

import java.util.List;

/**
 * 图片选择：Model
 * Created by yj on 2017/5/17.
 */

public interface PhotoPickDataSource extends BaseModel {

    /**
     * 加载图片
     *
     * @param callBack 回调
     */
    void loadPhotos(LoadDataCallBack<List<Photo>> callBack);

    /**
     * 加载相册
     *
     * @param callBack 回调
     */
    void loadGallery(LoadDataCallBack<List<PhotoDirectory>> callBack);
}
