package com.jkb.support.photopicker.business.preview;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.CheckBox;

import com.jkb.support.photopicker.R;
import com.jkb.support.photopicker.base.PhotoPickBaseFragment;
import com.jkb.support.photopicker.bean.Photo;
import com.jkb.support.photopicker.bean.PhotoPickBean;
import com.jkb.support.photopicker.business.action.listener.PhotoPickerResultCallback;
import com.jkb.support.photopicker.business.action.listener.PhotoSelectStatusChangedListener;
import com.jkb.support.photopicker.business.preview.i.OnPhotoClipItemClickListener;
import com.jkb.support.photopicker.business.preview.i.PhotoPreViewAction;
import com.jkb.support.photopicker.business.preview.i.PhotoPreviewOnBackResultCallback;
import com.jkb.support.photopicker.config.PhotoPickConfig;

import java.util.ArrayList;

/**
 * 图片选择器：图片预览
 * Created by yj on 2017/5/23.
 */

public class PhotoPreviewFragment extends PhotoPickBaseFragment implements PhotoPreViewAction, View.OnClickListener {

    public static PhotoPreviewFragment newInstance(int position, ArrayList<Photo> photos, PhotoPickBean photoBean) {
        Bundle args = new Bundle();
        args.putInt(PhotoPickConfig.KeyBundle.PHOTO_PREVIEW_POSITION, position);
        args.putSerializable(PhotoPickConfig.KeyBundle.PHOTO_PHOTOS, photos);
        args.putParcelable(PhotoPickConfig.KeyBundle.PHOTO_PICK, photoBean);
        PhotoPreviewFragment fragment = new PhotoPreviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //data
    private ArrayList<Photo> mPhotos;
    private int mCurrentPosition;
    private PhotoPickBean mPhotoBean;
    //view
    private ViewPager viewPager;
    private CheckBox checkBox;
    //listener
    private OnPhotoClipItemClickListener onPhotoClipItemClickListener;
    private PhotoPickerResultCallback photoPickerResultCallback;
    private PhotoPreviewOnBackResultCallback photoPreviewOnBackResultCallback;
    private PhotoSelectStatusChangedListener photoSelectStatusChangedListener;


    @Override
    protected int getRootViewId() {
        return R.layout.frg_photo_preview;
    }

    @Override
    protected void initView() {
        viewPager = (ViewPager) findViewById(R.id.fpp_pager);
        checkBox = (CheckBox) findViewById(R.id.fpp_select);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        Bundle args = savedInstanceState;
        if (savedInstanceState == null) args = getArguments();
        mCurrentPosition = args.getInt(PhotoPickConfig.KeyBundle.PHOTO_PREVIEW_POSITION);
        mPhotos = (ArrayList<Photo>) args.getSerializable(PhotoPickConfig.KeyBundle.PHOTO_PHOTOS);
        mPhotoBean = args.getParcelable(PhotoPickConfig.KeyBundle.PHOTO_PICK);

        PhotoPreViewPagerAdapter photoPreViewPagerAdapter = new PhotoPreViewPagerAdapter(mContext, mPhotoBean, mPhotos);
        viewPager.setAdapter(photoPreViewPagerAdapter);
        viewPager.setCurrentItem(mCurrentPosition);
        checkBox.setChecked(mPhotos.get(mCurrentPosition).isSelected());
    }

    @Override
    protected void initListener() {
        findViewById(R.id.fpp_edit).setOnClickListener(this);
        checkBox.setOnClickListener(this);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPosition = position;
                Photo photo = mPhotos.get(position);
                checkBox.setChecked(photo.isSelected());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PhotoPickConfig.KeyBundle.PHOTO_PREVIEW_POSITION, mCurrentPosition);
        outState.putSerializable(PhotoPickConfig.KeyBundle.PHOTO_PHOTOS, mPhotos);
        outState.putParcelable(PhotoPickConfig.KeyBundle.PHOTO_PICK, mPhotoBean);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fpp_edit) {
            if (onPhotoClipItemClickListener == null) return;
            onPhotoClipItemClickListener.onPhotoClipItemClick(mCurrentPosition, mPhotos.get(mCurrentPosition));
        } else if (v.getId() == R.id.fpp_select) {
            mPhotos.get(mCurrentPosition).setSelected(checkBox.isChecked());
            if (photoSelectStatusChangedListener == null) return;
            photoSelectStatusChangedListener.onPhotoSelectChanged(mPhotos.size(), getSelectPhoto().size());
        }
    }

    @Override
    public void addOnClipItemClickListener(OnPhotoClipItemClickListener listener) {
        onPhotoClipItemClickListener = listener;
    }

    @Override
    public void addPhotoPreviewOnBackResultCallback(PhotoPreviewOnBackResultCallback callback) {
        photoPreviewOnBackResultCallback = callback;
    }

    @Override
    public void pickDone() {
        if (photoPickerResultCallback == null) return;
        if (getSelectPhoto().isEmpty()) {
            photoPickerResultCallback.onPhotoPickFailed();
        } else {
            photoPickerResultCallback.onPhotoPickSuccess(getSelectPhoto());
        }
    }

    @Override
    public void addPhotoSelectStatusChangedListener(PhotoSelectStatusChangedListener listener) {
        photoSelectStatusChangedListener = listener;
    }

    @Override
    public void addPhotoPickerResultCallback(PhotoPickerResultCallback callback) {
        photoPickerResultCallback = callback;
    }

    @Override
    public void onBackPressed() {
        if (photoPreviewOnBackResultCallback != null) {
            photoPreviewOnBackResultCallback.onPreviewBackPressed(mPhotos);
        }
        super.onBackPressed();
    }

    /**
     * 返回选择的图片
     */
    private ArrayList<String> getSelectPhoto() {
        ArrayList<String> selectPhoto = new ArrayList<>();
        if (mPhotos == null || mPhotos.isEmpty()) return selectPhoto;
        for (Photo photo : mPhotos) {
            if (photo.isSelected()) selectPhoto.add(photo.getPath());
        }
        return selectPhoto;
    }
}
