package com.dn.nhc.remote.impl

import com.dn.nhc.remote.model.*
import com.dn.nhc.remote.network.NeighborhoodChoresApi
import com.dn.nhc.repository.remote.MypageRemoteDataSource
import com.dn.nhc.utils.UserManager
import com.lakue.lakue_library.network.ResultWrapper
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

class MypageRemoteDataSourceImpl @Inject constructor(
    private val neighborhoodChoresApi: NeighborhoodChoresApi,
    private val userManager: UserManager
): DataSourceImpl(), MypageRemoteDataSource {
    val Tag = MypageRemoteDataSourceImpl::class.java.name

    override suspend fun getMypageInfo(): ResultWrapper<LoginResponse> {
        return safeApiCall(Dispatchers.IO) {
            neighborhoodChoresApi.getMypageInfo()
        }
    }

    override suspend fun getMypagePheed(size: Int, page: Int): ResultWrapper<PheedResponse> {
        return safeApiCall(Dispatchers.IO) {
            neighborhoodChoresApi.getMypagePheed(size, page)
        }
    }

    override suspend fun putUpdateProfileImage(files: MultipartBody.Part): ResultWrapper<UserResponse> {
        return safeApiCall(Dispatchers.IO) {
            neighborhoodChoresApi.putUpdateProfileImage(files)
        }
    }

    override suspend fun getPushes(size: Int, page: Int): ResultWrapper<PushesResponse> {
        return safeApiCall(Dispatchers.IO) {
            neighborhoodChoresApi.getPushes(size, page)
        }
    }

    override suspend fun patchReadPush(pushId: Int): ResultWrapper<CommonResponse> {
        return safeApiCall(Dispatchers.IO) {
            neighborhoodChoresApi.patchReadPush(pushId)
        }
    }


}

@Module
@InstallIn(SingletonComponent::class)
abstract class MypageRemoteDataSourceModule {

    @Binds
    @Singleton
    abstract fun bindMypageRemoteDataSource(mypageRemoteDataSourceImpl: MypageRemoteDataSourceImpl): MypageRemoteDataSource

}
