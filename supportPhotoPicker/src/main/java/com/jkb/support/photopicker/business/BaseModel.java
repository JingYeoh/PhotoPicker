package com.jkb.support.photopicker.business;

/**
 * 基类：Model
 * Created by yj on 2017/5/17.
 */

public interface BaseModel {
    /**
     * 设置缓存过期
     */
    void cacheExpired();

    /**
     * 销毁页面
     */
    void destroy();

    /**
     * 加载数据的回调接口
     *
     * @param <T> 数据类型，使用泛型
     */
    interface LoadDataCallBack<T> {
        /**
         * 数据返回成功时回调
         */
        void onDataLoaded(T data);

        /**
         * 数据加载失败的时候回调
         *
         * @param errData 错误信息
         */
        void onDataNotAvailable(String errData);
    }
}
