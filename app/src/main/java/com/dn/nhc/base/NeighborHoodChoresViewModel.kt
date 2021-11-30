package com.dn.nhc.base

import com.dn.nhc.R
import com.dn.nhc.utils.BaseUtils
import com.dn.nhc.utils.UserManager
import com.lakue.lakue_library.network.ResultWrapper
import com.lakue.lakue_library.ui.LakueViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class NeighborHoodChoresViewModel @Inject constructor(
    private val userManager: UserManager
): LakueViewModel() {

    fun responseValidation(response: Any){
        when(response){
            is ResultWrapper.NetworkError -> {
                _liveNewWorkErrorDialog.postValue(BaseUtils.context.getString(R.string.dialog_network_error))
            }
            is ResultWrapper.Success<*> -> {
                _liveSuccess.postValue(response.value)
            }
            is ResultWrapper.TimeOutError -> {
                _liveNewWorkErrorDialog.postValue(BaseUtils.context.getString(R.string.dialog_timeout_error))
            }
            is ResultWrapper.ServerError -> {
                _liveNewWorkErrorDialog.postValue(BaseUtils.context.getString(R.string.dialog_server_error))
            }
            is ResultWrapper.GenericError -> {
                if (response.error == null) {
                    _liveNewWorkErrorDialog.postValue(BaseUtils.context.getString(R.string.dialog_server_error))
                } else {
                    _liveError.postValue(response.error)
                }

            }
        }
    }

    fun dbLogout(){
        userManager.logout()
    }

}