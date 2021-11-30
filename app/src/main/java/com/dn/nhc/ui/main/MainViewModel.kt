package com.dn.nhc.ui.main

import com.dn.nhc.base.NeighborHoodChoresViewModel
import com.dn.nhc.utils.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userManager: UserManager
    ): NeighborHoodChoresViewModel(userManager) {

}