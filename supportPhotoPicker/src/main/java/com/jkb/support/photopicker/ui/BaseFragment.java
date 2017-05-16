package com.jkb.support.photopicker.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jkb.support.ui.SupportFragment;

/**
 * Fragment的基类
 * Created by yj on 2017/5/16.
 */

@SuppressLint("ValidFragment")
abstract class BaseFragment extends SupportFragment {
    //view
    protected View rootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(getRootViewId(), container, false);
        return rootView;
    }

    /**
     * 返回根视图id
     */
    @LayoutRes
    protected abstract int getRootViewId();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void init(Bundle savedInstanceState) {
        initView();
        initData(savedInstanceState);
        initListener();
    }

    /**
     * 初始化视图
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData(Bundle savedInstanceState);

    /**
     * 初始化监听器
     */
    protected abstract void initListener();

    public View findViewById(@IdRes int id) {
        return rootView.findViewById(id);
    }

    public boolean isActive() {
        return isAdded();
    }

    public void showShortToast(String value) {
        if (TextUtils.isEmpty(value)) return;
        Toast.makeText(mContext, value, Toast.LENGTH_SHORT).show();
    }
}
