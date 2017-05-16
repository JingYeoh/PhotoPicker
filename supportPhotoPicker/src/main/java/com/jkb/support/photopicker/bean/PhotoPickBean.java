package com.jkb.support.photopicker.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;

import com.jkb.support.photopicker.R;
import com.jkb.support.photopicker.config.PhotoPickConfig;
import com.jkb.support.photopicker.loader.ImageLoader;

/**
 * 图片选择的Bean类
 * Created by yj on 2017/5/16.
 */

public class PhotoPickBean implements Parcelable {

    private int maxPickSize;            //最多可以选择多少张图片
    private int spanCount;              //recyclerView有多少列
    private int pickMode;               //单选还是多选
    private int clipMode;     //圆形裁剪方式
    private boolean isShowCamera;         //是否展示拍照icon
    private boolean isClipPhoto;          //是否启动裁剪图片
    private boolean isOriginalPicture;    //是否选择的是原图
    @DrawableRes
    private int cameraResId;            //照相机资源id
    @DrawableRes
    private int failedPictureResId;     //缓存的图片资源id
    private ImageLoader imageLoader;    //加载方式

    public PhotoPickBean() {
        //生成默认配置
        setSpanCount(3);
        setMaxPickSize(9);
        setPickMode(PhotoPickConfig.PickMode.MULTI);
        setClipMode(PhotoPickConfig.ClipMode.RECTANGLE);
        setShowCamera(true);
        setClipPhoto(true);
        setOriginalPicture(false);
        setCameraResId(R.drawable.take_photo);
        setFailedPictureResId(R.drawable.failure_image);
    }

    protected PhotoPickBean(Parcel in) {
        maxPickSize = in.readInt();
        spanCount = in.readInt();
        pickMode = in.readInt();
        clipMode = in.readInt();
        isShowCamera = in.readByte() != 0;
        isClipPhoto = in.readByte() != 0;
        isOriginalPicture = in.readByte() != 0;
        cameraResId = in.readInt();
        failedPictureResId = in.readInt();
    }

    public static final Creator<PhotoPickBean> CREATOR = new Creator<PhotoPickBean>() {
        @Override
        public PhotoPickBean createFromParcel(Parcel in) {
            return new PhotoPickBean(in);
        }

        @Override
        public PhotoPickBean[] newArray(int size) {
            return new PhotoPickBean[size];
        }
    };

    public int getMaxPickSize() {
        return maxPickSize;
    }

    public void setMaxPickSize(int maxPickSize) {
        this.maxPickSize = maxPickSize;
    }

    public int getSpanCount() {
        return spanCount;
    }

    public void setSpanCount(int spanCount) {
        this.spanCount = spanCount;
    }

    public int getPickMode() {
        return pickMode;
    }

    public void setPickMode(int pickMode) {
        this.pickMode = pickMode;
    }

    public int getClipMode() {
        return clipMode;
    }

    public void setClipMode(int clipMode) {
        this.clipMode = clipMode;
    }

    public boolean isShowCamera() {
        return isShowCamera;
    }

    public void setShowCamera(boolean showCamera) {
        isShowCamera = showCamera;
    }

    public boolean isClipPhoto() {
        return isClipPhoto;
    }

    public void setClipPhoto(boolean clipPhoto) {
        isClipPhoto = clipPhoto;
    }

    public boolean isOriginalPicture() {
        return isOriginalPicture;
    }

    public void setOriginalPicture(boolean originalPicture) {
        isOriginalPicture = originalPicture;
    }

    public int getCameraResId() {
        return cameraResId;
    }

    public void setCameraResId(int cameraResId) {
        this.cameraResId = cameraResId;
    }

    public int getFailedPictureResId() {
        return failedPictureResId;
    }

    public void setFailedPictureResId(int failedPictureResId) {
        this.failedPictureResId = failedPictureResId;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public void setImageLoader(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(maxPickSize);
        dest.writeInt(spanCount);
        dest.writeInt(pickMode);
        dest.writeInt(clipMode);
        dest.writeByte((byte) (isShowCamera ? 1 : 0));
        dest.writeByte((byte) (isClipPhoto ? 1 : 0));
        dest.writeByte((byte) (isOriginalPicture ? 1 : 0));
        dest.writeInt(cameraResId);
        dest.writeInt(failedPictureResId);
    }
}
