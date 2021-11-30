package com.dn.nhc.ui.login.facebook

import com.facebook.FacebookException
import com.facebook.login.LoginResult

interface FacebookLoginState {
    fun onSuccess(result: LoginResult)
    fun onError(error: FacebookException)
    fun onCancel()
}