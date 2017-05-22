package com.jkb.support.photopicker.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;

import com.jkb.support.photopicker.R;
import com.jkb.support.photopicker.adapter.PhotoPreViewPagerAdapter;
import com.jkb.support.photopicker.bean.Photo;
import com.jkb.support.photopicker.bean.PhotoPickBean;
import com.jkb.support.photopicker.config.PhotoPickConfig;

import java.util.ArrayList;

/**
 * 图片预览
 * Created by yj on 2017/5/22.
 */

public class PhotoPreViewFragment extends BaseFragment implements View.OnClickListener, ViewPager.OnPageChangeListener {

    public static PhotoPreViewFragment newInstance(int position, ArrayList<Photo> photos, PhotoPickBean photoBean) {
        Bundle args = new Bundle();
        args.putInt(PhotoPickConfig.KeyBundle.PHOTO_PREVIEW_POSITION, position);
        args.putSerializable(PhotoPickConfig.KeyBundle.PHOTO_PHOTOS, photos);
        args.putParcelable(PhotoPickConfig.KeyBundle.PHOTO_PICK, photoBean);
        PhotoPreViewFragment fragment = new PhotoPreViewFragment();
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
        if (savedInstanceState == null) {
            args = getArguments();
        }
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
        findViewById(R.id.fpp_preview).setOnClickListener(this);
        viewPager.addOnPageChangeListener(this);
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
        if (v.getId() == R.id.fpp_select) {
            Photo photo = mPhotos.get(mCurrentPosition);
            photo.setSelected(!photo.isSelected());
            checkBox.setChecked(photo.isSelected());
        } else if (v.getId() == R.id.fpp_preview) {//编辑图片

        }
    }

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
}
