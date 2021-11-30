package com.dn.nhc.utils.converter

import android.util.DisplayMetrics
import com.dn.nhc.utils.BaseUtils.context

object UnitConverter {
    val displayMetrics: DisplayMetrics
        get() = context.resources.displayMetrics

    fun dpToPx(dp: Float): Float {
        return dp * displayMetrics.density
    }

    fun dpToPx(dp: Int): Int {
        return (dp * displayMetrics.density + 0.5f).toInt()
    }

    fun pxToDp(px: Float): Float {
        return px / displayMetrics.density
    }

    fun pxToDp(px: Int): Int {
        return (px / displayMetrics.density + 0.5f).toInt()
    }

    fun spToPx(sp: Float): Float {
        return sp * displayMetrics.scaledDensity
    }

    fun spToPx(sp: Int): Int {
        return (sp * displayMetrics.scaledDensity + 0.5f).toInt()
    }

    fun pxToSp(px: Float): Float {
        return px / displayMetrics.scaledDensity
    }

    fun pxToSp(px: Int): Int {
        return (px / displayMetrics.scaledDensity + 0.5f).toInt()
    }
}