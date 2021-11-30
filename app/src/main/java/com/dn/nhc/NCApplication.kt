package com.dn.nhc

import android.app.Application
import android.os.Build
import android.webkit.WebView
import com.dn.nhc.utils.BaseUtils.init
import com.facebook.stetho.Stetho
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NCApplication: Application(){

    companion object{
        lateinit var NCApplication: NCApplication

        fun getInstance(): NCApplication{
            return NCApplication
        }
    }

    override fun onCreate() {
        super.onCreate()
        NCApplication = this
        init(this)
        KakaoSdk.init(this, getString(R.string.kakao_app_key))
        Stetho.initializeWithDefaults(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
    }
}