package com.dn.nhc.ui.common

import com.dn.nhc.base.NeighborHoodChoresViewModel
import com.dn.nhc.utils.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CommonViewModel @Inject constructor(
    private val userManager: UserManager
    ): NeighborHoodChoresViewModel(userManager) {

}