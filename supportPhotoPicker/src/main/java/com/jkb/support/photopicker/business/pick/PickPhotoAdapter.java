package com.jkb.support.photopicker.business.pick;

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
import com.jkb.support.photopicker.business.action.listener.PhotoSelectStatusChangedListener;

import java.util.ArrayList;

/**
 * 图片选择的数据适配器
 * Created by yj on 2017/5/23.
 */

class PickPhotoAdapter extends RecyclerView.Adapter<PickPhotoAdapter.ViewHolder> {

    private Context context;
    private PhotoPickBean photoPickBean;
    public ArrayList<Photo> photos;
    public ArrayList<String> selectPhotos;
    //attributes
    private int imageLength;//图片边长
    //listener
    private PhotoSelectStatusChangedListener photoSelectStatusChangedListener;
    private OnPhotoItemClickListener onPhotoItemClickListener;

    PickPhotoAdapter(Context context, PhotoPickBean photoPickBean) {
        this.context = context;
        this.photoPickBean = photoPickBean;
        photos = new ArrayList<>();
        selectPhotos = new ArrayList<>();
        //动态设置图片大小
        DisplayMetrics metrics = new DisplayMetrics();
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        display.getMetrics(metrics);
        imageLength = metrics.widthPixels / photoPickBean.getSpanCount();
    }

    /**
     * 刷新
     */
    void refresh(ArrayList<Photo> photos) {
        this.photos = photos;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_photo_pick, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.showPhotoItemView(position);
    }

    @Override
    public int getItemCount() {
        return photoPickBean.isShowCamera() ? (photos == null ? 0 : photos.size() + 1)
                : (photos == null ? 0 : photos.size());
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        CheckBox checkBox;

        ViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.ipp_iv);
            checkBox = (CheckBox) view.findViewById(R.id.ipp_cb);
            view.getLayoutParams().height = imageLength;
            view.getLayoutParams().width = imageLength;
            //listener
            checkBox.setOnClickListener(this);
            imageView.setOnClickListener(this);
        }

        /**
         * 显示条目视图
         */
        void showPhotoItemView(int position) {
            if (photoPickBean.isShowCamera()) {
                if (position == 0) {
                    checkBox.setVisibility(View.GONE);
                    imageView.setImageResource(photoPickBean.getCameraResId());
                    return;
                } else {
                    position -= 1;
                }
            }
            Photo photo = photos.get(position);
            if (photoPickBean.getMaxPickSize() == 1) {
                checkBox.setVisibility(View.GONE);
            } else {
                checkBox.setVisibility(View.VISIBLE);
                checkBox.setChecked(photos.get(position).isSelected());
            }
            String url = photo.getPath();
            photoPickBean.getImageLoader().displayImage(context, url, imageView, true);
        }

        @Override
        public void onClick(View v) {
            if (selectPhotos.size() == photoPickBean.getMaxPickSize()) {
                checkBox.setChecked(false);
                return;
            }
            int position = getAdapterPosition();
            if (photoPickBean.isShowCamera()) position -= 1;
            if (v.getId() == R.id.ipp_iv) {
                if (position == -1) {
                    onPhotoItemClickListener.onCameraClick();
                } else {
                    onPhotoItemClickListener.onPhotoItemClick(position);
                }
            } else if (v.getId() == R.id.ipp_cb) {
                Photo photo = photos.get(position);
                photo.setSelected(!photo.isSelected());
                checkBox.setChecked(photo.isSelected());
                if (photo.isSelected()) {
                    selectPhotos.add(photo.getPath());
                } else {
                    selectPhotos.remove(photo.getPath());
                }
                photos.set(position, photo);
                if (photoSelectStatusChangedListener != null) {
                    photoSelectStatusChangedListener.onPhotoSelectChanged(photos.size(), selectPhotos.size());
                }
            }
        }
    }

    void setPhotoSelectStatusChangedListener(PhotoSelectStatusChangedListener photoSelectStatusChangedListener) {
        this.photoSelectStatusChangedListener = photoSelectStatusChangedListener;
    }

    void setOnPhotoItemClickListener(OnPhotoItemClickListener onPhotoItemClickListener) {
        this.onPhotoItemClickListener = onPhotoItemClickListener;
    }

    interface OnPhotoItemClickListener {
        /**
         * 图片条目被点击
         *
         * @param position 被点击的条目
         */
        void onPhotoItemClick(int position);

        /**
         * 相机被点击
         */
        void onCameraClick();
    }
}
