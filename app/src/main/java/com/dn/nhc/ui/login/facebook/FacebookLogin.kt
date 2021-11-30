package com.dn.nhc.ui.login.facebook

import android.app.Activity
import android.content.Context
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult

class FacebookLogin(context: Context, callbackManager: CallbackManager, facebookLoginState: FacebookLoginState){

    init {
        FacebookSdk.sdkInitialize(context)

        LoginManager.getInstance().logInWithReadPermissions(context as Activity,
            listOf("public_profile", "email"))
        LoginManager.getInstance().registerCallback(callbackManager, object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
//                Config.showProgress(false);
                facebookLoginState.onSuccess(result)
            }

            override fun onError(error: FacebookException) {
                facebookLoginState.onError(error)
            }

            override fun onCancel() {
                facebookLoginState.onCancel()
            }
        })
    }

    fun logout(){
        LoginManager.getInstance().logOut();
    }
}