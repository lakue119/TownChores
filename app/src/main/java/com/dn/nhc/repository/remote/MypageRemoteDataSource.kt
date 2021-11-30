package com.dn.nhc.repository.remote

import com.dn.nhc.remote.model.*
import com.lakue.lakue_library.network.ResultWrapper
import okhttp3.MultipartBody

interface MypageRemoteDataSource {
    suspend fun getMypageInfo(): ResultWrapper<LoginResponse>
    suspend fun getMypagePheed(size: Int, page: Int): ResultWrapper<PheedResponse>
    suspend fun putUpdateProfileImage(files: MultipartBody.Part): ResultWrapper<UserResponse>
    suspend fun getPushes(size: Int, page: Int): ResultWrapper<PushesResponse>
    suspend fun patchReadPush(pushId: Int): ResultWrapper<CommonResponse>
}