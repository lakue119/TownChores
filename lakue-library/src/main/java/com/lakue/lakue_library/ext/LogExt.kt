package com.lakue.lakue_library.ext

import android.util.Log
import com.lakue.lakue_library.BuildConfig

const val LOG_TAG = "lakue_log"

fun logV(tag: String = LOG_TAG, msg: String, showCallStack: Boolean = false) {
    if (BuildConfig.DEBUG) {
        if (showCallStack) {
            Log.v(tag, msg, Throwable())
        } else {
            Log.v(tag, msg)
        }
    }
}

fun logD(tag: String = LOG_TAG, msg: String, showCallStack: Boolean = false) {
    if (BuildConfig.DEBUG) {
        if (showCallStack) {
            Log.d(tag, msg, Throwable())
        } else {
            Log.d(tag, msg)
        }
    }
}

fun logI(tag: String = LOG_TAG, msg: String, showCallStack: Boolean = false) {
    if (BuildConfig.DEBUG) {
        if (showCallStack) {
            Log.i(tag, msg, Throwable())
        } else {
            Log.i(tag, msg)
        }
    }
}

fun logW(tag: String = LOG_TAG, msg: String, showCallStack: Boolean = false) {
    if (BuildConfig.DEBUG) {
        if (showCallStack) {
            Log.w(tag, msg, Throwable())
        } else {
            Log.w(tag, msg)
        }
    }
}

fun logE(tag: String = LOG_TAG, msg: String, showCallStack: Boolean = false) {
    if (BuildConfig.DEBUG) {
        if (showCallStack) {
            Log.e(tag, msg, Throwable())
        } else {
            Log.e(tag, msg)
        }
    }
}

fun logE(tag: String = LOG_TAG, msg: String, tr: Throwable?, showCallStack: Boolean = false) {
    if (BuildConfig.DEBUG) {
        if (showCallStack) {
            Log.e(
                tag, """
     $msg
     ${Log.getStackTraceString(tr)}
     """.trimIndent()
            )
        } else {
            Log.e(tag, msg)
        }
    }
}
