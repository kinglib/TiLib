package com.endlesscreator.tiviewlib.view.model.tiroundlayout;

import android.graphics.Canvas;

public interface ITiRoundView {
    void onSizeChangedSuper(int w, int h, int oldw, int oldh);
    void dispatchDrawSuper(Canvas canvas);
    void drawSuper(Canvas canvas);
}
