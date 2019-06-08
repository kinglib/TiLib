package com.endlesscreator.tilib.module.etv;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.TextUtils;

import com.endlesscreator.tilib.base.BaseActivity;
import com.endlesscreator.tilib.R;
import com.endlesscreator.titoollib.utils.InfoUtil;
import com.endlesscreator.tiviewlib.view.TiExpandableTextView;
import com.endlesscreator.tiviewlib.view.model.tiexpandabletextview.app.LinkType;
import com.endlesscreator.tiviewlib.view.model.tiexpandabletextview.model.ExpandableTool;


public class TestETVActivity extends BaseActivity implements TiExpandableTextView.OnLinkOperateListener {

    public static final String[] EXPANDABLE_OPERATE = {"mention", "big", "small"};

    private TiExpandableTextView etv;

    @Override
    protected int makeLayout() {
        return R.layout.activity_test_etv;
    }

    @Override
    protected void initView() {
        etv = find(R.id.etv);
    }

    @Override
    protected void initListener() {
        etv.setLinkClickListener(this);
    }

    @Override
    protected void initData() {
        etv.setContent(
                "字体测试字体 " +
                        ExpandableTool.makeSelfLink("@习大大:", EXPANDABLE_OPERATE[0]) +
                        ExpandableTool.makeSelfLink("字体变大", EXPANDABLE_OPERATE[1]) +
                        ExpandableTool.makeSelfLink("2019-5-16 01:36:46", EXPANDABLE_OPERATE[2]) +
                        "测试 测试 测试 测试 测试 测试 测试 测试 测试 测试 测试 测试 测试 测试 测试 测试 测试 测试 测试 测试 \n\n");
    }

    @Override
    public void onUpdateDrawStateListener(LinkType type, String content, String selfContent, TextPaint ds) {
        if (type == LinkType.SELF) {
            if (TextUtils.equals(EXPANDABLE_OPERATE[0], selfContent)) {
                ds.setColor(Color.RED);
                ds.setFakeBoldText(true);
                return;
            }

            if (TextUtils.equals(EXPANDABLE_OPERATE[1], selfContent)) {
                ds.setTextSize(ds.getTextSize() * 1.12f); // 注意，尽量不要使用字体缩放，会导致宽度测量和文字实际宽度不相等，右侧文字展示不全或换行错位的情况，控件待优化
                return;
            }

            if (TextUtils.equals(EXPANDABLE_OPERATE[2], selfContent)) {
                ds.setTextSize(ds.getTextSize() / 2);
                return;
            }


        }
    }

    @Override
    public void onLinkClickListener(LinkType type, String content, String selfContent) {
        show("type=" + type + ",content=" + content + ",selfContent=" + selfContent);
    }
}
