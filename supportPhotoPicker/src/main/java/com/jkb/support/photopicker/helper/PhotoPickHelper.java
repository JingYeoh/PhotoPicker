package com.jkb.support.photopicker.helper;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.jkb.support.photopicker.bean.PhotoDirectory;
import com.jkb.support.photopicker.data.Data;

import java.util.List;

/**
 * 图片选择器的帮助类
 * Created by yj on 2017/5/17.
 */

public class PhotoPickHelper {

    /**
     * 获取手机相册
     *
     * @param context  上下文
     * @param callback 回调
     */
    public static void getPhotoDirs(Context context, PhotosResultCallback callback) {
        getPhotoDirs(context, callback, true);
    }

    public static void getPhotoDirs(final Context context, final PhotosResultCallback resultCallback,
                                    final boolean checkImageStatus) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                PhotoCursorLoader loader = new PhotoCursorLoader();
                ContentResolver contentResolver = context.getContentResolver();
                Cursor cursor = contentResolver.query(loader.getUri(), loader.getProjection(), loader.getSelection(),
                        loader.getSelectionArgs(), loader.getSortOrder());
                if (cursor == null) return;

                List<PhotoDirectory> directories = Data.getDataFromCursor(context, cursor, checkImageStatus);
                cursor.close();
                if (resultCallback != null) {
                    resultCallback.onResultCallback(directories);
                }
            }
        }).start();
    }

    static class PhotoDirLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {

        private Context context;
        private PhotosResultCallback resultCallback;
        private boolean checkImageStatus;//是否检查图片已经损坏

        public PhotoDirLoaderCallbacks(Context context, boolean checkImageStatus, PhotosResultCallback resultCallback) {
            this.context = context;
            this.resultCallback = resultCallback;
            this.checkImageStatus = checkImageStatus;
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new PhotoDirectoryLoader(context);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data == null) return;
            List<PhotoDirectory> directories = Data.getDataFromCursor(context, data, checkImageStatus);
            data.close();
            if (resultCallback != null) {
                resultCallback.onResultCallback(directories);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }

    /**
     * 获取图片的回调
     */
    public interface PhotosResultCallback {
        void onResultCallback(List<PhotoDirectory> directories);
    }
}
