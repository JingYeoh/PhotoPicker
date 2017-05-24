package com.jkb.support.photopicker.business.pick;

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
import com.jkb.support.photopicker.bean.PhotoDirectory;
import com.jkb.support.photopicker.bean.PhotoPickBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片选择器：相册的数据适配器
 * Created by yj on 2017/5/23.
 */

class PickGalleryAdapter extends RecyclerView.Adapter<PickGalleryAdapter.ViewHolder> {

    private Context context;
    private PhotoPickBean photoPickBean;
    public int selected = 0;
    private List<PhotoDirectory> directories;
    private int imageLength;

    private OnGalleryItemClickListener onGalleryItemClickListener;

    PickGalleryAdapter(Context context, PhotoPickBean photoPickBean) {
        this.context = context;
        this.photoPickBean = photoPickBean;
        //初始化数据
        selected = 0;
        directories = new ArrayList<>();
        //初始化属性
        DisplayMetrics metrics = new DisplayMetrics();
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        display.getMetrics(metrics);
        imageLength = metrics.widthPixels / 6;
    }

    /**
     * 刷新
     */
    void refresh(ArrayList<PhotoDirectory> directories) {
        this.directories = directories;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo_gallery, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.showGalleryItemView(position);
    }

    @Override
    public int getItemCount() {
        return directories.size();
    }

    public void changeSelect(int position) {
        this.selected = position;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView;
        private ImageView photo_gallery_select;
        private TextView name, num;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.ipg_iv_photo);
            name = (TextView) itemView.findViewById(R.id.ipg_tv_name);
            num = (TextView) itemView.findViewById(R.id.ipg_tv_num);
            photo_gallery_select = (ImageView) itemView.findViewById(R.id.ipg_selected);
            imageView.getLayoutParams().height = imageLength;
            imageView.getLayoutParams().width = imageLength;
            itemView.setOnClickListener(this);
        }

        /**
         * 显示相册条目视图
         */
        void showGalleryItemView(int position) {
            PhotoDirectory directory = directories.get(position);
            if (directory == null || directory.getCoverPath() == null) return;
            if (selected == position) {
                photo_gallery_select.setVisibility(View.VISIBLE);
            } else {
                photo_gallery_select.setVisibility(View.INVISIBLE);
            }
            name.setText(directory.getName());
            num.setText(context.getString(R.string.gallery_num, String.valueOf(directory.getPhotoPaths().size())));
            photoPickBean.getImageLoader().displayImage(context, directory.getCoverPath(), imageView, true);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (v.getId() == R.id.photo_gallery_rl) {
                if (onGalleryItemClickListener != null) {
                    changeSelect(position);
                    onGalleryItemClickListener.onGalleryItemClick(position);
                }
            }
        }
    }

    void setOnGalleryItemClickListener(OnGalleryItemClickListener onGalleryItemClickListener) {
        this.onGalleryItemClickListener = onGalleryItemClickListener;
    }

    interface OnGalleryItemClickListener {
        /**
         * 相册条目被点击
         */
        void onGalleryItemClick(int position);
    }
}
