package com.dn.nhc.ui.login.naver

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.dn.nhc.R
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler


class NaverLogin(context: Context, naverLoginState: NaverLoginState) {

    var mOAuthLoginModule = OAuthLogin.getInstance()
    var mContext = context

    init {
        mOAuthLoginModule.init(
            context
            ,context.resources.getString(R.string.naver_client_id)
            ,context.resources.getString(R.string.naver_client_secret)
            ,context.resources.getString(R.string.naver_client_name)
            //,OAUTH_CALLBACK_INTENT
            // SDK 4.1.4 버전부터는 OAUTH_CALLBACK_INTENT변수를 사용하지 않습니다.
        )

        @SuppressLint("HandlerLeak")
        val mOAuthLoginHandler: OAuthLoginHandler = object : OAuthLoginHandler() {
            override fun run(success: Boolean) {
                if (success) {
                    val accessToken = mOAuthLoginModule.getAccessToken(context)
                    val refreshToken = mOAuthLoginModule.getRefreshToken(context)
                    val expiresAt = mOAuthLoginModule.getExpiresAt(context)
                    val tokenType = mOAuthLoginModule.getTokenType(context)

                    val args = Bundle()
                    args.putString("accessToken",accessToken)
                    args.putString("refreshToken",refreshToken)
                    args.putLong("expiresAt",expiresAt)
                    args.putString("tokenType",tokenType)
                    args.putString("sate",mOAuthLoginModule.getState(context).toString())
                    naverLoginState.onSuccess(mOAuthLoginModule)

                } else {
                    val errorCode = mOAuthLoginModule.getLastErrorCode(context).code
                    val errorDesc = mOAuthLoginModule.getLastErrorDesc(context)
                    naverLoginState.onError(mOAuthLoginModule)
                }
            }
        }

        mOAuthLoginModule.startOauthLoginActivity(context as Activity, mOAuthLoginHandler)
    }

    fun logout(){
        mOAuthLoginModule.logout(mContext);
    }
}