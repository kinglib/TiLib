package com.endlesscreator.titoollib.utils

import android.content.Context
import android.text.TextUtils
import android.view.Gravity
import android.widget.Toast
import com.endlesscreator.tibaselib.frame.SingleManager
import com.endlesscreator.tibaselib.frame.TApp
import java.util.*


object InfoUtil {

    private var mDurationType = Toast.LENGTH_LONG
    private var mGravityType = Gravity.BOTTOM
    //防止连续点击造成的一直Toast不消失
    private var mPreviousToast: Toast? = null
    private val mTopYOffset by lazy { AlgorithmUtil.dp2px(70) }

    private var mTimerExec: Timer? = null
    private var mTimerStop: Timer? = null

    fun setDurationType(aDurationType: Int) {
        mDurationType = if (aDurationType == Toast.LENGTH_SHORT) aDurationType else Toast.LENGTH_LONG
    }

    fun show(aInfo: String): Boolean {
        return show(null, aInfo)
    }

    fun show(aInfoResId: Int): Boolean {
        return show(null, aInfoResId)
    }

    fun show(aContext: Context?, aInfo: String): Boolean {
        if (TextUtils.isEmpty(aInfo)) return false
        if (!ThreadUtil.isMainThread) {
            SingleManager.POST_DELAYED_HANDLER.post { show(aInfo) }
            return false
        }
        mPreviousToast?.cancel()
        mPreviousToast = Toast.makeText(aContext ?: TApp.getInstance(), aInfo, mDurationType)
        mPreviousToast?.setGravity(mGravityType, 0, mTopYOffset)
        mPreviousToast?.show()
        return true
    }

    fun show(aContext: Context?, aInfoResId: Int): Boolean {
        if (!ThreadUtil.isMainThread) {
            SingleManager.POST_DELAYED_HANDLER.post { show(aInfoResId) }
            return false
        }
        mPreviousToast?.cancel()
        mPreviousToast = Toast.makeText(aContext ?: TApp.getInstance(), aInfoResId, mDurationType)
        mPreviousToast?.setGravity(mGravityType, 0, mTopYOffset)
        mPreviousToast?.show()
        return true
    }

    fun showTmp(aInfo: String): Boolean {
        return showTmp(null, aInfo)
    }

    fun showTmp(aContext: Context?, aInfo: String): Boolean {
        return show(aContext, "$aInfo（程序临时输出）")
    }

    /**
     * 自定义时长显示信息
     */
    fun showCustom(aInfo: String, aDuration: Long, aDelay: Long = 0, aPeriod: Long = 5000): Boolean {
        if (TextUtils.isEmpty(aInfo)) return false
        stopCustom()
        mTimerExec = Timer()
        mTimerStop = Timer()
        mPreviousToast = null
        mTimerExec?.schedule(object : TimerTask() {
            override fun run() {
                SingleManager.POST_DELAYED_HANDLER.post {
                    mPreviousToast?.cancel()
                    mPreviousToast = Toast.makeText(TApp.getInstance(), aInfo, Toast.LENGTH_LONG)
                    mPreviousToast?.setGravity(mGravityType, 0, mTopYOffset)

//                    mPreviousToast?.view?.setBackgroundResource(R.drawable.toast_bg_shape)
//                    mPreviousToast?.view?.findViewById<TextView>(android.R.id.message)?.let {
//                        it.setTextColor(Color.WHITE)
//                        it.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18.0f)
//                    }

                    mPreviousToast?.show()
                }
            }
        }, if (aDelay < 0) 0 else aDelay, if (aPeriod <= 0) 5000 else aPeriod)
        mTimerStop?.schedule(object : TimerTask() {
            override fun run() {
                stopCustom()
            }
        }, aDuration)
        return true
    }

    /**
     * 取消自定义显示
     */
    fun stopCustom() {
        mTimerExec?.cancel()
        mTimerStop?.cancel()
        mPreviousToast?.cancel()
        mTimerExec = null
        mTimerStop = null
        mPreviousToast = null
    }
}
