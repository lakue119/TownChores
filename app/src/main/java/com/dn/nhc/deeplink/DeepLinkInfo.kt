package com.dn.nhc.deeplink

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.annotation.StringRes
import com.dn.nhc.NCApplication
import com.dn.nhc.R
import com.dn.nhc.ui.main.MainActivity
import com.dn.nhc.ui.pheed.detail.PheedDetailActivity
import com.lakue.lakue_library.ext.logD

enum class DeepLinkInfo(@StringRes val hostStringResId: Int) {

    MAIN(R.string.scheme_host_main) {
        override fun getIntent(context: Context, deepLinkUri: Uri) =
            getMainIntent(context)
    },

    PHEED_DETAIL(R.string.scheme_host_pheed_detail) {
        override fun getIntent(context: Context, deepLinkUri: Uri) =
            PheedDetailActivity.getIntent(context, deepLinkUri)
    };

    private val host: String = NCApplication.getInstance().getString(hostStringResId)

    abstract fun getIntent(context: Context, deepLinkUri: Uri): Intent

    companion object {

        fun getMainIntent(context: Context) = MainActivity.getIntent(context)

        operator fun invoke(uri: Uri): DeepLinkInfo =
            values().find { it.host == uri.host } ?: run {
                logD("sadf","등록되지 않은 딥링크")
                logD("asdf","Not registered deep link host")
                MAIN
            }
    }
}