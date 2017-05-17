package com.jkb.support.photopicker.business.pick.contract;

import com.jkb.support.photopicker.bean.Photo;
import com.jkb.support.photopicker.bean.PhotoDirectory;
import com.jkb.support.photopicker.business.BasePresenter;
import com.jkb.support.photopicker.business.BaseView;

import java.util.List;

/**
 * 图片选择的页面协议类
 * Created by yj on 2017/5/17.
 */

public interface PhotoPickContract {

    interface View extends BaseView {
        /**
         * 绑定照片
         */
        void bindPhotos(List<Photo> photos);

        /**
         * 绑定相册
         */
        void bindGallery(List<PhotoDirectory> gallery);

        /**
         * 切换相册
         */
        void switchGallery(List<Photo> photos);
    }

    interface Presenter extends BasePresenter {
        /**
         * 切换相册
         */
        void switchGallery(int position);
    }
}
