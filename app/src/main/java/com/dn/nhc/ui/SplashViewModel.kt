package com.dn.nhc.ui

import com.dn.nhc.base.NeighborHoodChoresViewModel
import com.dn.nhc.utils.SharedPreferencesManager
import com.dn.nhc.utils.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userManager: UserManager,
    private val sharedPreferencesManager: SharedPreferencesManager
): NeighborHoodChoresViewModel(userManager) {

    fun isAutoLogin(): Boolean{
        return userManager.getLoginCheck()
    }

    fun isShowIntro(): Boolean {
        return sharedPreferencesManager.isShowIntro
    }
}