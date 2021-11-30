package com.dn.nhc.repository.remote

import com.dn.nhc.remote.model.*
import com.dn.nhc.remote.model.common.Image
import com.lakue.lakue_library.network.ResultWrapper
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.util.HashMap

interface PheedRemoteDataSource {
    suspend fun postPheedUpload(
        files: List<MultipartBody.Part>,
        tags: List<MultipartBody.Part>,
        partMap: HashMap<String, RequestBody>
    ): ResultWrapper<PheedUploadResponse>

    suspend fun putUpdatePheed(
        postId: String,
        files: List<MultipartBody.Part>,
        tags: List<MultipartBody.Part>,
        existingImages: List<Image>,
        partMap: HashMap<String, RequestBody>
    ): ResultWrapper<PheedUploadResponse>

    suspend fun deletePheed(
        postId: String,
    ): ResultWrapper<CommonResponse>

    suspend fun getPheedList(
        size: Int,
        page: Int,
        latitude: Double,
        longitude: Double
    ): ResultWrapper<PheedResponse>

    suspend fun getSearchContentPheedListResult(
        query: String,
        size: Int,
        page: Int,
        latitude: Double,
        longitude: Double
    ): ResultWrapper<PheedResponse>

    suspend fun getSearchHashTagPheedListResult(
        query: String,
        size: Int,
        page: Int,
        latitude: Double,
        longitude: Double
    ): ResultWrapper<PheedResponse>

    suspend fun getPheedDetail(
        postId: String,
    ): ResultWrapper<PheedUploadResponse>

    suspend fun getComment(
        postId: String,
    ): ResultWrapper<CommentListResponse>

    suspend fun getCommentDetail(
        commentId: String,
    ): ResultWrapper<CommentListResponse>

    suspend fun postComment(
        parameters: HashMap<String, Any>,
    ): ResultWrapper<CommentResponse>

    suspend fun deleteComment(
        commentId: String,
    ): ResultWrapper<CommonResponse>

    suspend fun postComplain(
        targetType: String,
        parameters: HashMap<String, Any>,
    ): ResultWrapper<CommonResponse>
}