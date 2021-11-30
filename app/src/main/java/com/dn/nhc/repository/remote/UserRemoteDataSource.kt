package com.dn.nhc.repository.remote

import com.dn.nhc.remote.model.CommonResponse
import com.dn.nhc.remote.model.UserResponse
import com.dn.nhc.remote.model.LoginResponse
import com.lakue.lakue_library.network.ResultWrapper

interface UserRemoteDataSource {
    suspend fun getLoginAuth(socialId: String, deviceToken: String): ResultWrapper<LoginResponse>
    suspend fun postLogout(): ResultWrapper<CommonResponse>
    suspend fun postJoin(map: HashMap<String, Any>): ResultWrapper<UserResponse>
}