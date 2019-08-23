package com.endlesscreator.titoollib.utils

import android.text.Html
import android.text.Spanned

object TextUtil {

    fun fromHtml(aContent: String?, aCompact: Boolean = true): Spanned? {
        if (aContent.isNullOrEmpty()) return null
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            /*
                FROM_HTML_MODE_COMPACT：html块元素之间使用一个换行符分隔
                FROM_HTML_MODE_LEGACY：html块元素之间使用两个换行符分隔
             */
            Html.fromHtml(aContent, if (aCompact) Html.FROM_HTML_MODE_COMPACT else Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(aContent)
        }
    }

}
