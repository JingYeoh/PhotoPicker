package com.jkb.support.photopicker.utils;

import android.graphics.BitmapFactory;

/**
 * 文件工具类
 */
public class FileUtils {

    private static final String TAG = "FileUtils";

    private final String IMAGE_STORE_FILE_NAME = "IMG_%s.jpg";

    /**
     * 检查文件是否损坏
     * Check if the file is corrupted
     *
     * @param filePath 路径
     */
    public static boolean checkImgCorrupted(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        return (options.mCancel || options.outWidth == -1
                || options.outHeight == -1);
    }
}