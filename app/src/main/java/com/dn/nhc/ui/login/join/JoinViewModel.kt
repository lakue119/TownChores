package com.dn.nhc.ui.login.join

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dn.nhc.repository.remote.UserRemoteDataSource
import com.dn.nhc.ui.login.LoginViewModel
import com.dn.nhc.utils.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JoinViewModel @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val userManager: UserManager
): LoginViewModel(userRemoteDataSource, userManager) {

    val liveNickName = MutableLiveData<String>("")

    fun joinAuth(map: HashMap<String, Any>){
        fethJoinAuth(map)
    }

    private fun fethJoinAuth(map: HashMap<String, Any>){
        viewModelScope.launch {
            val response = userRemoteDataSource.postJoin(map)

            responseValidation(response)
        }
    }
}