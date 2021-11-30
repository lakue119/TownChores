package com.dn.nhc.ui.login.naver

import com.nhn.android.naverlogin.OAuthLogin;

interface NaverLoginState {
    fun onSuccess(result: OAuthLogin)
    fun onError(error: OAuthLogin)
}