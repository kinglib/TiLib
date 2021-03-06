package com.endlesscreator.tilib.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.endlesscreator.titoollib.utils.InfoUtil;


public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(makeLayout());
        initView();
        initListener();
        initData();
    }

    protected abstract int makeLayout();

    protected void initView() {
    }

    protected void initListener() {
    }

    protected void initData() {
    }

    protected <T extends View> T find(@IdRes int id) {
        return super.findViewById(id);
    }

    protected void toAct(Class aClass) {
        startActivity(new Intent(this, aClass));
    }

    protected void show(String aInfo) {
        InfoUtil.INSTANCE.show(aInfo);
    }
}
