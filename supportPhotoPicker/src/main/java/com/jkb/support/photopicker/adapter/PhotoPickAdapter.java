package com.jkb.support.photopicker.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.jkb.support.photopicker.R;
import com.jkb.support.photopicker.bean.Photo;
import com.jkb.support.photopicker.bean.PhotoPickBean;
import com.jkb.support.photopicker.listener.PhotoPickChangedListener;

import java.util.ArrayList;

/**
 * 图片展示的数据适配器
 * Created by yj on 2017/5/16.
 */

public class PhotoPickAdapter extends RecyclerView.Adapter<PhotoPickAdapter.ViewHolder> {

    private Context context;
    private PhotoPickBean photoPickBean;
    //data
    private ArrayList<Photo> photos;
    private ArrayList<String> selectPhotos;
    //attributes
    private int imageSize;
    //listener
    private PhotoPickChangedListener photoPickChangedListener;
    private OnPhotoItemClickListener onPhotoItemClickListener;

    public PhotoPickAdapter(Context context, PhotoPickBean photoPickBean) {
        this.context = context;
        this.photoPickBean = photoPickBean;

        photos = new ArrayList<>();
        selectPhotos = new ArrayList<>();
        //动态设置图片大小
        DisplayMetrics metrics = new DisplayMetrics();
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        display.getMetrics(metrics);
        this.imageSize = metrics.widthPixels / photoPickBean.getSpanCount();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_photo_pick, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.showItemView(position);
    }

    @Override
    public int getItemCount() {
        return photoPickBean.isShowCamera() ? (photos == null ? 0 : photos.size() + 1)
                : (photos == null ? 0 : photos.size());
    }

    private Photo getItem(int position) {
        return photoPickBean.isShowCamera() ? photos.get(position - 1) : photos.get(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        CheckBox checkBox;

        ViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.ipp_iv);
            checkBox = (CheckBox) view.findViewById(R.id.ipp_cb);
            imageView.getLayoutParams().height = imageSize;
            imageView.getLayoutParams().width = imageSize;
            //listener
            checkBox.setOnClickListener(this);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (selectPhotos.size() == photoPickBean.getMaxPickSize()) {
                checkBox.setChecked(false);
                return;
            }
            int position = getAdapterPosition();
            if (v.getId() == R.id.ipp_iv) {
                //图片被点击
                if (onPhotoItemClickListener != null) {
                    onPhotoItemClickListener.onPhotoItemClick(getAdapterPosition());
                }
            } else if (v.getId() == R.id.ipp_cb) {
                if (selectPhotos.contains(getItem(position).getPath())) {
                    checkBox.setChecked(false);
                    selectPhotos.remove(getItem(position).getPath());
                } else {
                    checkBox.setChecked(true);
                    selectPhotos.add(getItem(position).getPath());
                }
                if (photoPickChangedListener != null) {
                    photoPickChangedListener
                            .onPhotoPickChanged(photoPickBean.getMaxPickSize(), selectPhotos.size());
                }
            }
        }

        /**
         * 显示条目视图
         */
        void showItemView(int position) {
            if (photoPickBean.isShowCamera() && position == 0) {
                checkBox.setVisibility(View.GONE);
                imageView.setImageResource(photoPickBean.getCameraResId());
            } else {
                Photo photo = getItem(position);
                if (photoPickBean.getMaxPickSize() == 1) {
                    checkBox.setVisibility(View.GONE);
                } else {
                    checkBox.setVisibility(View.VISIBLE);
                    checkBox.setChecked(selectPhotos.contains(photo.getPath()));
                }
                String url = photo.getPath();
                photoPickBean.getImageLoader().displayImage(context, url, imageView, true);
            }
        }
    }

    /**
     * 设置图片数量变化的监听器
     */
    public void setPhotoPickChangedListener(PhotoPickChangedListener photoPickChangedListener) {
        this.photoPickChangedListener = photoPickChangedListener;
    }

    /**
     * 设置照片被点击的监听器
     *
     * @param onPhotoItemClickListener 监听器对象
     */
    public void setOnPhotoItemClickListener(OnPhotoItemClickListener onPhotoItemClickListener) {
        this.onPhotoItemClickListener = onPhotoItemClickListener;
    }

    public interface OnPhotoItemClickListener {
        /**
         * 图片条目被点击
         *
         * @param position 被点击的条目
         */
        void onPhotoItemClick(int position);
    }
}
