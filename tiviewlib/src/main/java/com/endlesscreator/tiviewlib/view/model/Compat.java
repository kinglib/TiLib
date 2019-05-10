package com.endlesscreator.tiviewlib.view.model;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;

/**
 * 版本兼容控制器
 */
public class Compat {

    private static final CompatPlusImpl IMPL;

    static {
        int version = Build.VERSION.SDK_INT;
        if (version >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            IMPL = new JbMr1CompatPlusImpl();
        } else {
            IMPL = new BaseCompatPlusImpl();
        }
    }

    public static int getPaddingStart(View view) {
        return IMPL.getPaddingStart(view);
    }

    public static int getPaddingEnd(View view) {
        return IMPL.getPaddingEnd(view);
    }

    private interface CompatPlusImpl {
        int getPaddingStart(View view);

        int getPaddingEnd(View view);
    }

    private static class BaseCompatPlusImpl implements CompatPlusImpl {
        @Override
        public int getPaddingStart(View view) {
            return view.getPaddingLeft();
        }

        @Override
        public int getPaddingEnd(View view) {
            return view.getPaddingRight();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static class JbMr1CompatPlusImpl extends BaseCompatPlusImpl {
        @Override
        public int getPaddingStart(View view) {
            return view.getPaddingStart();
        }

        @Override
        public int getPaddingEnd(View view) {
            return view.getPaddingEnd();
        }
    }
}
