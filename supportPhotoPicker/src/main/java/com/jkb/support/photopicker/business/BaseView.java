package com.jkb.support.photopicker.business;

/**
 * View基类
 * Created by yj on 2017/5/17.
 */

public interface BaseView<P> {
    /**
     * 是否被销毁
     */
    boolean isActive();

    /**
     * 显示Toast
     */
    void showShortToast(String value);

    /**
     * 注入P层
     */
    void setPresenter(P presenter);
}
