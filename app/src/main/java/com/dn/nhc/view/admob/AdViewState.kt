package com.dn.nhc.view.admob

interface AdViewState {
    fun onAdLoaded()
    fun onFailed(code: Int)
    fun onAdOpened()
    fun onAdClicked()
    fun onAdLeftApplication()
    fun onAdClosed()
}