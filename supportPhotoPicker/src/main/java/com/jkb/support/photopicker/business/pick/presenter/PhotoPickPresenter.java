package com.jkb.support.photopicker.business.pick.presenter;

import com.jkb.support.photopicker.business.model.PhotoPickDataRepertory;
import com.jkb.support.photopicker.business.pick.contract.PhotoPickContract;

/**
 * 图片选择：Presenter
 * Created by yj on 2017/5/17.
 */

public class PhotoPickPresenter implements PhotoPickContract.Presenter {

    private PhotoPickContract.View mView;
    private PhotoPickDataRepertory mRepertory;

    public PhotoPickPresenter(PhotoPickContract.View mView, PhotoPickDataRepertory mRepertory) {
        this.mView = mView;
        this.mRepertory = mRepertory;
        this.mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void switchGallery(int position) {

    }
}
