package com.endlesscreator.tilib;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.endlesscreator.tibaselib.frame.TApp;
import com.endlesscreator.titoollib.utils.LogUtil;
import com.endlesscreator.tiviewlib.view.TiRefreshLayout;

public class MainActivity extends AppCompatActivity {

    private TiRefreshLayout mSmartRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TApp.onCreate(getApplication());
        LogUtil.isOutShow = true;

        mSmartRefreshLayout = findViewById(R.id.refresh_view);


        click(null);
    }

    public void click(View view) {
        LogUtil.i("------>>> isOpaque 1 = " + mSmartRefreshLayout.getState().isOpening);
        mSmartRefreshLayout.autoRefresh();
        LogUtil.i("------>>> isOpaque 2 = " + mSmartRefreshLayout.getState().isOpening);
        mSmartRefreshLayout.finishRefresh();

    }
}
