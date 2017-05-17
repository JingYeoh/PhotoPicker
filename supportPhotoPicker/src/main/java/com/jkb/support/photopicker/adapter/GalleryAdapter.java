package com.jkb.support.photopicker.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jkb.support.photopicker.R;
import com.jkb.support.photopicker.bean.Photo;
import com.jkb.support.photopicker.bean.PhotoDirectory;
import com.jkb.support.photopicker.bean.PhotoPickBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 相册数据适配器
 * Created by yj on 2017/5/17.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private Context context;
    private PhotoPickBean photoPickBean;
    private int selected;
    private List<PhotoDirectory> directories = new ArrayList<>();
    private int imageSize;

    private OnGalleryItemClickListener onGalleryItemClickListener;

    public GalleryAdapter(Context context, PhotoPickBean photoPickBean) {
        this.context = context;
        this.photoPickBean = photoPickBean;
        DisplayMetrics metrics = new DisplayMetrics();
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        display.getMetrics(metrics);
        this.imageSize = metrics.widthPixels / 6;
    }

    /**
     * 刷新相册
     */
    public void refresh(List<PhotoDirectory> directories) {
        this.directories.clear();
        this.directories.addAll(directories);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo_gallery, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.showGalleryView(getItem(position), position);
    }

    @Override
    public int getItemCount() {
        return directories.size();
    }

    private PhotoDirectory getItem(int position) {
        return this.directories.get(position);
    }

    private void changeSelect(int position) {
        this.selected = position;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView;
        private ImageView photo_gallery_select;
        private TextView name, num;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.ipg_iv_photo);
            name = (TextView) itemView.findViewById(R.id.ipg_tv_name);
            num = (TextView) itemView.findViewById(R.id.ipg_tv_num);
            photo_gallery_select = (ImageView) itemView.findViewById(R.id.ipg_selected);
            imageView.getLayoutParams().height = imageSize;
            imageView.getLayoutParams().width = imageSize;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (v.getId() == R.id.photo_gallery_rl) {
                if (onGalleryItemClickListener != null) {
                    changeSelect(position);
                    onGalleryItemClickListener.onGalleryItemClick(getItem(position).getPhotos());
                }
            }
        }

        void showGalleryView(PhotoDirectory directory, int position) {
            if (directory == null || directory.getCoverPath() == null) {
                return;
            }
            if (selected == position) {
                photo_gallery_select.setVisibility(View.VISIBLE);
            } else {
                photo_gallery_select.setVisibility(View.INVISIBLE);
            }
            name.setText(directory.getName());
            num.setText(context.getString(R.string.gallery_num, String.valueOf(directory.getPhotoPaths().size())));
            photoPickBean.getImageLoader().displayImage(context, directory.getCoverPath(), imageView, true);
        }
    }

    public void setOnGalleryItemClickListener(OnGalleryItemClickListener onGalleryItemClickListener) {
        this.onGalleryItemClickListener = onGalleryItemClickListener;
    }

    public interface OnGalleryItemClickListener {
        /**
         * 相册条目被点击
         */
        void onGalleryItemClick(List<Photo> photos);
    }
}
