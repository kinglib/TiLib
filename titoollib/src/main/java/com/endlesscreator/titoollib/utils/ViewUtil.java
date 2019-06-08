package com.endlesscreator.titoollib.utils;

import android.view.View;
import android.widget.HorizontalScrollView;

public class ViewUtil {


    // 将 HorizontalScrollView 中的某个 View 滑动到中间位置
    public static void moveTo(HorizontalScrollView aScrollView, View aView) {
        if (aScrollView == null || aView == null) return;
        int lWidth = aView.getMeasuredWidth();
        if (lWidth == 0) return;
        int lLeft = aView.getLeft();
        int screenWidth = aScrollView.getMeasuredWidth();
        aScrollView.smoothScrollTo(lLeft + lWidth / 2 - screenWidth / 2, 0);//滑动ScrollView
    }


}
