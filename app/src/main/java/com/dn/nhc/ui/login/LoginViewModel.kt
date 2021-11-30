package com.dn.nhc.ui.login

import androidx.lifecycle.viewModelScope
import com.dn.nhc.R
import com.dn.nhc.base.NeighborHoodChoresViewModel
import com.dn.nhc.repository.remote.UserRemoteDataSource
import com.dn.nhc.utils.BaseUtils
import com.dn.nhc.utils.UserManager
import com.lakue.lakue_library.network.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class LoginViewModel @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val userManager: UserManager
): NeighborHoodChoresViewModel(userManager) {


    var socialId = ""
    var socialType = ""

    fun startLogin(socialId: String, socialType: String, token: String) {
        this.socialId = socialId
        this.socialType = socialType

        fetchLogin(socialId, socialType, token)
    }

    private fun fetchLogin(socialId: String, socialType: String, token: String) {

        viewModelScope.launch {
            val response = userRemoteDataSource.getLoginAuth(socialId, token)

            when (response) {
                is ResultWrapper.NetworkError -> {
                    _liveNewWorkErrorDialog.postValue(BaseUtils.context.getString(R.string.dialog_network_error))
                }
                is ResultWrapper.Success -> {
                    val user = response.value.response
                    userManager.userId = user?.user?.id ?: ""
                    userManager.userToken = user?.token ?: ""
                    userManager.userType = user?.user?.role ?: ""
                    userManager.userSocialId = user?.user?.socialId ?: ""
                    userManager.userSocialType = user?.user?.socialType ?: ""
                    userManager.userLatitude = user?.user?.latitude ?: 0f
                    userManager.userLongitude = user?.user?.longitude ?: 0f
                    userManager.userProfileImage = user?.user?.profileImageUrl ?: ""

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
    }

}