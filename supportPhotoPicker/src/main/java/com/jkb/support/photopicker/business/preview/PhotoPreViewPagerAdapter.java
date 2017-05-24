package com.jkb.support.photopicker.business.preview;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.chrisbanes.photoview.PhotoView;
import com.jkb.support.photopicker.R;
import com.jkb.support.photopicker.bean.Photo;
import com.jkb.support.photopicker.bean.PhotoPickBean;

import java.util.List;

/**
 * 图片预览的页面适配器
 * Created by yj on 2017/5/22.
 */

public class PhotoPreViewPagerAdapter extends PagerAdapter {

    private Context context;
    private PhotoPickBean photoPickBean;
    public List<Photo> mPhotos;

    public PhotoPreViewPagerAdapter(Context context, PhotoPickBean photoPickBean, List<Photo> mPhotos) {
        this.context = context;
        this.photoPickBean = photoPickBean;
        this.mPhotos = mPhotos;
    }

    @Override
    public int getCount() {
        return mPhotos.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        String bigImgUrl = mPhotos.get(position).getPath();
        View view = LayoutInflater.from(context).inflate(R.layout.item_photo_preview, container,
                false);
        PhotoView imageView = (PhotoView) view.findViewById(R.id.iv_media_image);
        photoPickBean.getImageLoader().displayImage(context, bigImgUrl, imageView, false);
        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
