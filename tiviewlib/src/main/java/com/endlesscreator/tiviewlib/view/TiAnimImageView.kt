package com.endlesscreator.tiviewlib.view

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.util.AttributeSet
import android.view.View

class TiAnimImageView : android.support.v7.widget.AppCompatImageView {

    private var animDrawable: AnimationDrawable? = null
    var animAuto = true

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        when (visibility) {
            View.VISIBLE -> if (animAuto) animStart()
            else -> animStop()
        }
    }

    public fun animStart() {
        if (!isShown) return
        animDrawable = drawable as? AnimationDrawable
        if (animDrawable == null) animDrawable = background as? AnimationDrawable
        if (animDrawable?.isRunning == false) animDrawable?.start()
    }

    public fun animStop() {
        animDrawable?.let {
            if (it.isRunning) it.stop()
            animDrawable = null
        }
    }

    // 页面展示
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
    }

    // 页面销毁
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animStop()
    }

    // 在窗口上的显示状态变化
    override fun onWindowVisibilityChanged(visibility: Int) {
        super.onWindowVisibilityChanged(visibility)
    }
}
