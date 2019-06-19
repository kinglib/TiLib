package com.endlesscreator.tilib.module.bounce;

import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.endlesscreator.tilib.MainActivity;
import com.endlesscreator.tilib.R;
import com.endlesscreator.tilib.base.BaseActivity;
import com.endlesscreator.tiviewlib.view.TiBounceView;
import com.endlesscreator.tiviewlib.view.model.tiexpandabletextview.model.ExpandableTool;

public class TestBounceActivity extends BaseActivity {

    private ImageView ivPub;
    private TiBounceView bounceView;

    @Override
    protected int makeLayout() {
        return R.layout.activity_test_bounce;
    }

    @Override
    protected void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            // 部分机型的statusbar会有半透明的黑色背景,添加下面两行
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        ivPub = findViewById(R.id.iv_pub);
    }

    @Override
    protected void initListener() {

        ivPub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bounceView.startAnim();
            }
        });

        bounceView = findViewById(R.id.bounce);
        bounceView.setBgAnimStyle(TiBounceView.Style.ROUND);      // Style.ALPHA, Style.TRANS, Style.NONE
//        bounceView.setBounceBgColor(getResources().getColor(R.color.colorAccent));    // 设置背景色
//        bounceView.setBounceBgDrawable(drawable);     // 设置背景
//        bounceView.setBgAnimDuration(300);        // 设置背景动画时间,默认300ms
        bounceView.setOnPubCloseListener(new TiBounceView.OnPubCloseListener() {
            @Override
            public void onPubClose() {
                // 动画关闭后操作
                Toast.makeText(TestBounceActivity.this, "动画结束了", Toast.LENGTH_SHORT).show();
            }
        });
        bounceView.setContentAnimDuration(400);     // 设置内容动画时间,默认300ms
        bounceView.setBounceBgBlur(true);       // 设置截取当前窗口并模糊
        bounceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bounceView.closeAnim();
            }
        });

        findViewById(R.id.ll_folder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TestBounceActivity.this, "folder click", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.ll_note).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TestBounceActivity.this, "note click", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.ll_picture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TestBounceActivity.this, "picture click", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.ll_wallet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TestBounceActivity.this, "wallet click", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
