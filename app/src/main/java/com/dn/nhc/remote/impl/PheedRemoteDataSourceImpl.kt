package com.dn.nhc.remote.impl

import com.dn.nhc.remote.model.*
import com.dn.nhc.remote.model.common.Image
import com.dn.nhc.remote.network.NeighborhoodChoresApi
import com.dn.nhc.repository.remote.PheedRemoteDataSource
import com.dn.nhc.utils.UserManager
import com.lakue.lakue_library.network.ResultWrapper
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.util.HashMap
import javax.inject.Inject
import javax.inject.Singleton

class PheedRemoteDataSourceImpl @Inject constructor(
    private val neighborhoodChoresApi: NeighborhoodChoresApi,
    private val userManager: UserManager
): DataSourceImpl(), PheedRemoteDataSource {

    override suspend fun postPheedUpload(
        files: List<MultipartBody.Part>,
        tags: List<MultipartBody.Part>,
        partMap: HashMap<String, RequestBody>
    ): ResultWrapper<PheedUploadResponse> {
        return safeApiCall(Dispatchers.IO) {
            neighborhoodChoresApi.postPheedUpload(files, tags, partMap)
        }
    }

    override suspend fun putUpdatePheed(
        postId: String,
        files: List<MultipartBody.Part>,
        tags: List<MultipartBody.Part>,
        existingImages: List<Image>,
        partMap: HashMap<String, RequestBody>
    ): ResultWrapper<PheedUploadResponse> {
        return safeApiCall(Dispatchers.IO) {
            neighborhoodChoresApi.putUpdatePheed(postId, files, tags, existingImages, partMap)
        }
    }

    override suspend fun deletePheed(postId: String): ResultWrapper<CommonResponse> {
        return safeApiCall(Dispatchers.IO) {
            neighborhoodChoresApi.deletePheed(postId)
        }
    }

    override suspend fun getPheedList(
        size: Int,
        page: Int,
        latitude: Double,
        longitude: Double
    ): ResultWrapper<PheedResponse> {
        return safeApiCall(Dispatchers.IO) {
            neighborhoodChoresApi.getPheedList(size, page, latitude, longitude)
        }
    }

    override suspend fun getSearchContentPheedListResult(
        query: String,
        size: Int,
        page: Int,
        latitude: Double,
        longitude: Double
    ): ResultWrapper<PheedResponse> {
        return safeApiCall(Dispatchers.IO) {
            neighborhoodChoresApi.getSearchContentPheedListResult(query, size, page, latitude, longitude)
        }
    }

    override suspend fun getSearchHashTagPheedListResult(
        query: String,
        size: Int,
        page: Int,
        latitude: Double,
        longitude: Double
    ): ResultWrapper<PheedResponse> {
        return safeApiCall(Dispatchers.IO) {
            neighborhoodChoresApi.getSearchHashTagPheedListResult(query, size, page, latitude, longitude)
        }
    }

    override suspend fun getPheedDetail(postId: String): ResultWrapper<PheedUploadResponse> {
        return safeApiCall(Dispatchers.IO) {
            neighborhoodChoresApi.getPheedDetail(postId)
        }
    }

    override suspend fun getComment(postId: String): ResultWrapper<CommentListResponse> {
        return safeApiCall(Dispatchers.IO) {
            neighborhoodChoresApi.getComment(postId)
        }
    }

    override suspend fun getCommentDetail(commentId: String): ResultWrapper<CommentListResponse> {
        return safeApiCall(Dispatchers.IO) {
            neighborhoodChoresApi.getCommentDetail(commentId)
        }
    }

    override suspend fun postComment(parameters: HashMap<String, Any>): ResultWrapper<CommentResponse> {
        return safeApiCall(Dispatchers.IO) {
            neighborhoodChoresApi.postComment(parameters)
        }
    }

    override suspend fun deleteComment(commentId: String): ResultWrapper<CommonResponse> {
        return safeApiCall(Dispatchers.IO) {
            neighborhoodChoresApi.deleteComment(commentId)
        }
    }

    override suspend fun postComplain(
        targetType: String,
        parameters: HashMap<String, Any>
    ): ResultWrapper<CommonResponse> {
        return safeApiCall(Dispatchers.IO) {
            neighborhoodChoresApi.postComplain(targetType, parameters)
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class PheedRemoteDataSourceModule {

    @Binds
    @Singleton
    abstract fun bindPheedRemoteDataSource(pheedRemoteDataSourceImpl: PheedRemoteDataSourceImpl): PheedRemoteDataSource

}
