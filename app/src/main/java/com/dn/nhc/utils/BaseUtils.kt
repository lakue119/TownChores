package com.dn.nhc.utils

import android.content.Context
import java.lang.ref.WeakReference

object BaseUtils {
    private const val ERROR_INIT = "Initialize BaseUtils with invoke init()"
    private var mWeakReferenceContext: WeakReference<Context>? = null

    /**
     * init in Application
     */
    @JvmStatic
    fun init(context: Context) {
        mWeakReferenceContext =
            WeakReference(context)
        //something to do...
    }

    @JvmStatic
    val context: Context
        get() {
            requireNotNull(mWeakReferenceContext) { ERROR_INIT }
            return mWeakReferenceContext!!.get()!!.applicationContext
        }
}