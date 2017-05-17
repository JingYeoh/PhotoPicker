package com.jkb.photopicker;

import android.os.Bundle;

import com.jkb.support.photopicker.config.PhotoPicker;
import com.jkb.support.photopicker.ui.PhotoPickFragment;

public class MainActivity extends BaseActivity {

    @Override
    protected int getRootViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            PhotoPickFragment fragment = new PhotoPicker.Builder()
                    .imageLoader(new PhotoImageLoader()).build().createPick();
            startFragment(fragment);
        }
    }

    @Override
    protected void initListener() {

    }

    @Override
    public int getFragmentContentId() {
        return R.id.mainFrame;
    }
}
