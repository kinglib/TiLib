package com.endlesscreator.tilib.module.drag;

import android.view.View;
import android.widget.ScrollView;

import com.endlesscreator.tilib.R;
import com.endlesscreator.tilib.base.BaseActivity;
import com.endlesscreator.tilib.module.drag.view.ContentImageView;
import com.endlesscreator.tilib.module.drag.view.ProxyImageView;
import com.endlesscreator.titoollib.utils.InfoUtil;

public class TestDragActivity extends BaseActivity {

    private ContentImageView mContent;
    private ProxyImageView mProxy;

    @Override
    protected int makeLayout() {
        return R.layout.activity_test_drag;
    }

    @Override
    protected void initView() {
        mContent = find(R.id.drag_content);
        mProxy = find(R.id.drag_proxy);
    }

    @Override
    protected void initListener() {
        mContent.setProxyImageView(mProxy);
    }

    public void click(View view) {
        InfoUtil.INSTANCE.show("click");
    }
}
