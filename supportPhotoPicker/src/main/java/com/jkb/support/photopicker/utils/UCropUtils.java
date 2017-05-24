package com.jkb.support.photopicker.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.ColorRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.jkb.support.photopicker.bean.PhotoPickBean;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.File;


/**
 * 图片裁剪的工具类
 */
public class UCropUtils {
    public static void start(Activity mActivity, File sourceFile, File destinationFile,
                             boolean showClipCircle, @ColorRes int actionBarColor) {
        UCrop uCrop = UCrop.of(Uri.fromFile(sourceFile), Uri.fromFile(destinationFile));
        //.withAspectRatio(aspectRatioX, aspectRatioY)  //动态的设置图片的宽高比
        UCrop.Options options = new UCrop.Options();
        options.useSourceImageAspectRatio();                        //设置为图片原始宽高比列一样
        options.withMaxResultSize(500, 500);                        //设置将被载入裁剪图片的最大尺寸
        options.setCompressionFormat(Bitmap.CompressFormat.PNG);    //设置裁剪出来图片的格式
        options.setFreeStyleCropEnabled(true);                     //可以调整裁剪框
        if (showClipCircle == true) {
            options.setCircleDimmedLayer(true);                     //设置裁剪框圆形
            options.setShowCropFrame(false);                        //设置是否展示矩形裁剪框
            options.setShowCropGrid(false);                         //是否显示裁剪框网格
        } else {
            options.setShowCropFrame(true);                        //设置是否展示矩形裁剪框
            options.setShowCropGrid(true);
        }
        options.setToolbarColor(ContextCompat.getColor(mActivity, actionBarColor));
        options.setStatusBarColor(ContextCompat.getColor(mActivity, actionBarColor));
        uCrop.withOptions(options);
        uCrop.start(mActivity);
    }

    public static void start(Fragment fragment, Context context, File sourceFile, File destinationFile, boolean
            showClipCircle, @ColorRes int actionBarColor) {
        UCrop uCrop = UCrop.of(Uri.fromFile(sourceFile), Uri.fromFile(destinationFile));
        //.withAspectRatio(aspectRatioX, aspectRatioY)  //动态的设置图片的宽高比
        UCrop.Options options = new UCrop.Options();
        options.useSourceImageAspectRatio();                        //设置为图片原始宽高比列一样
        options.withMaxResultSize(500, 500);                        //设置将被载入裁剪图片的最大尺寸
        options.setCompressionFormat(Bitmap.CompressFormat.PNG);    //设置裁剪出来图片的格式
        options.setFreeStyleCropEnabled(true);                     //可以调整裁剪框
        if (showClipCircle == true) {
            options.setCircleDimmedLayer(true);                     //设置裁剪框圆形
            options.setShowCropFrame(false);                        //设置是否展示矩形裁剪框
            options.setShowCropGrid(false);                         //是否显示裁剪框网格
        } else {
            options.setShowCropFrame(true);                        //设置是否展示矩形裁剪框
            options.setShowCropGrid(true);
        }
        options.setToolbarColor(ContextCompat.getColor(context, actionBarColor));
        options.setStatusBarColor(ContextCompat.getColor(context, actionBarColor));
        uCrop.withOptions(options);
        fragment.startActivityForResult(uCrop.getIntent(context), UCrop.REQUEST_CROP);
    }
}
