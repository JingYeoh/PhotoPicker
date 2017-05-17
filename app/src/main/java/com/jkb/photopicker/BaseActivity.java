package com.jkb.photopicker;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.jkb.support.ui.SupportActivity;
import com.jkb.support.utils.LogUtils;

/**
 * Created by yj on 2017/5/17.
 */

public abstract class BaseActivity extends SupportActivity {

    //system
    protected Context context;
    protected Context applicationContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.d(TAG, "onCreate");
        context = this;
        applicationContext = getApplicationContext();
        setContentView(getRootViewId());
        init(savedInstanceState);
    }

    @LayoutRes
    protected abstract int getRootViewId();

    public void init(Bundle savedInstanceState) {
        initView();
        initData(savedInstanceState);
        initListener();
    }

    protected abstract void initView();

    protected abstract void initData(Bundle savedInstanceState);

    protected abstract void initListener();

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void showShortToast(String value) {
        if (value != null && !value.trim().isEmpty()) {
            Toast.makeText(context, value, Toast.LENGTH_SHORT).show();
        }
    }
}
