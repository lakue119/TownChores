package com.dn.nhc.remote.impl

import com.dn.nhc.remote.model.CommonResponse
import com.dn.nhc.remote.model.UserResponse
import com.dn.nhc.remote.model.LoginResponse
import com.dn.nhc.remote.network.NeighborhoodChoresApi
import com.dn.nhc.repository.remote.UserRemoteDataSource
import com.dn.nhc.utils.UserManager
import com.lakue.lakue_library.network.ResultWrapper
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton

class UserRemoteDataSourceImpl @Inject constructor(
    private val neighborhoodChoresApi: NeighborhoodChoresApi,
    private val userManager: UserManager
): DataSourceImpl(), UserRemoteDataSource {
    val Tag = UserRemoteDataSourceImpl::class.java.name

    override suspend fun getLoginAuth(socialId: String, deviceToken: String): ResultWrapper<LoginResponse> {
        return safeApiCall(Dispatchers.IO) {
            neighborhoodChoresApi.getLoginAuth(socialId, deviceToken)
        }
    }

    override suspend fun postLogout(): ResultWrapper<CommonResponse> {
        return safeApiCall(Dispatchers.IO) {
            neighborhoodChoresApi.postLogout()
        }
    }

    override suspend fun postJoin(map: HashMap<String, Any>): ResultWrapper<UserResponse> {
        return safeApiCall(Dispatchers.IO) {
            neighborhoodChoresApi.postJoin(map)
        }
    }

}

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthRemoteDataSourceModule {

    @Binds
    @Singleton
    abstract fun bindAuthRemoteDataSource(userRemoteDataSourceImpl: UserRemoteDataSourceImpl): UserRemoteDataSource

}
