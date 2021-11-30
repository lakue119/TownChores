package com.dn.nhc.ui.login.kakao

import android.content.Context
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.user.UserApiClient
import com.lakue.lakue_library.ext.logE
import com.lakue.lakue_library.ext.logI


class KakaoLogin(context: Context, kakaoLoginState: KakaoLoginState) {
    val TAG = "KakaoLogin"
    init {
        // 카카오계정으로 로그인
        if (LoginClient.instance.isKakaoTalkLoginAvailable(context)) {
            loginWithKakaoTalk(context, kakaoLoginState)
        } else {
            loginWithkakaoAccount(context, kakaoLoginState)
        }

    }

    fun loginWithKakaoTalk(context: Context, kakaoLoginState: KakaoLoginState){
        LoginClient.instance.loginWithKakaoTalk(context) { token, error ->
            if (error != null) {
                kakaoLoginState.onError(error)
            }
            else if (token != null) {
                getUserInfo(kakaoLoginState)
            }
        }
    }

    fun loginWithkakaoAccount(context: Context, kakaoLoginState: KakaoLoginState){
        LoginClient.instance.loginWithKakaoAccount(context) { token, error ->
            if (error != null) {
                kakaoLoginState.onError(error)
            }
            else if (token != null) {
                getUserInfo(kakaoLoginState)
            }
        }
    }

    fun getUserInfo(kakaoLoginState: KakaoLoginState){
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                kakaoLoginState.onError(error)
            }
            else if (user != null) {
                kakaoLoginState.onSuccess(user)
//                Log.i(TAG, "사용자 정보 요청 성공" +
//                        "\n회원번호: ${user.id}" +
//                        "\n이메일: ${user.kakaoAccount?.email}" +
//                        "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
//                        "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}")
            }
        }
    }

    fun logout(){
        UserApiClient.instance.logout { error ->
            if (error != null) {
                logE(TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error)
            }
            else {
                logI(TAG, "로그아웃 성공. SDK에서 토큰 삭제됨")
            }
        }
    }
}