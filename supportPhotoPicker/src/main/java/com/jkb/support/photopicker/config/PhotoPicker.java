package com.jkb.support.photopicker.config;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import com.jkb.support.photopicker.bean.PhotoPickBean;
import com.jkb.support.photopicker.business.clip.PhotoClipFragment;
import com.jkb.support.photopicker.business.pick.PhotoPickFragment;
import com.jkb.support.photopicker.business.picker.PhotoPickerFragment;
import com.jkb.support.photopicker.loader.ImageLoader;

/**
 * 图片选择器类
 * 采用Builder模式，用于创建Fragment对象
 * Created by yj on 2017/5/16.
 */

public class PhotoPicker {

    private Builder mBuilder;

    private PhotoPicker(@NonNull Builder builder) {
        mBuilder = builder;
    }

    /**
     * 返回图片选择器Fragment
     * 包含选择图片和裁剪，若启用裁剪则选择完毕后自动切换裁剪页面
     */
    public PhotoPickerFragment createPicker() {
        return PhotoPickerFragment.newInstance(mBuilder.photoPickBean);
    }

    /**
     * 返回图片选择器Fragment
     */
    public PhotoPickFragment createPick() {
        return PhotoPickFragment.newInstance(mBuilder.photoPickBean);
    }

    /**
     * 返回图片裁剪Fragment
     */
    public PhotoClipFragment createClip() {
        return null;
    }

    public static class Builder {
        //图片选择配置
        private PhotoPickBean photoPickBean;

        public Builder() {
            photoPickBean = new PhotoPickBean();
        }

        /**
         * 设置图片加载类
         */
        public Builder imageLoader(ImageLoader imageLoader) {
            photoPickBean.setImageLoader(imageLoader);
            return this;
        }

        /**
         * 设置选择图片的最大数量，默认9张
         */
        public Builder maxPickSize(int maxPickSize) {
            photoPickBean.setMaxPickSize(maxPickSize);
            if (maxPickSize > 1) {
                photoPickBean.setPickMode(PhotoPickConfig.PickMode.MULTI);
            } else {
                photoPickBean.setPickMode(PhotoPickConfig.PickMode.SINGLE);
            }
            return this;
        }

        /**
         * 设置一行一共多少列，默认3列
         */
        public Builder spanCount(int spanCount) {
            photoPickBean.setSpanCount(spanCount);
            return this;
        }

        /**
         * 裁剪模式
         *
         * @param clipMode {@link PhotoPickConfig.ClipMode}
         */
        public Builder clipMode(int clipMode) {
            photoPickBean.setClipMode(clipMode);
            return this;
        }

        /**
         * 是否使用相机
         */
        public Builder showCamera(boolean isShowCamera) {
            photoPickBean.setShowCamera(isShowCamera);
            return this;
        }

        /**
         * 是否启用裁剪图片
         */
        public Builder clipPhoto(boolean isClipPhoto) {
            photoPickBean.setClipPhoto(isClipPhoto);
            return this;
        }

        /**
         * 是否使用原图
         */
        public Builder originalPicture(boolean isOriginalPicture) {
            photoPickBean.setOriginalPicture(isOriginalPicture);
            return this;
        }

        /**
         * 设置相机的图片
         */
        public Builder cameraDrawable(@DrawableRes int cameraResId) {
            photoPickBean.setCameraResId(cameraResId);
            return this;
        }

        /**
         * 设置坏损的图片
         */
        public Builder failedPictureDrawable(@DrawableRes int failedPictureResId) {
            photoPickBean.setFailedPictureResId(failedPictureResId);
            return this;
        }

        /**
         * 设置图片裁剪的背景颜色
         */
        public Builder clipActionBarBackgroundColor(@ColorRes int color) {
            photoPickBean.setClipActionBarBackgroundColor(color);
            return this;
        }

        /**
         * 创建picker对象
         */
        public PhotoPicker build() {
            return new PhotoPicker(this);
        }
    }
}
