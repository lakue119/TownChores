package com.dn.nhc.remote.model.common

import com.dn.nhc.remote.model.ResponseModel
import com.google.gson.annotations.SerializedName

data class Image(
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("id") val id: Long?,
    @SerializedName("image_name_url") val imageNameUrl: String?,
    @SerializedName("post_id") val postId: Long?,
    @SerializedName("seq") val seq: Int?,
    @SerializedName("is_deleted") var isDeleted: Boolean?,
    @SerializedName("user_id") val userId: Long?
): ResponseModel()