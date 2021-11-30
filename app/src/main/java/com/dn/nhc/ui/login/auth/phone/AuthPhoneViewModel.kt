package com.dn.nhc.ui.login.auth.phone

import com.dn.nhc.base.NeighborHoodChoresViewModel
import com.dn.nhc.utils.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthPhoneViewModel @Inject constructor(
    private val userManager: UserManager
    ): NeighborHoodChoresViewModel(userManager) {

}