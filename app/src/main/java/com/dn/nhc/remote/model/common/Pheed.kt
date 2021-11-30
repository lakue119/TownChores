package com.dn.nhc.remote.model.common

import com.dn.nhc.remote.model.ResponseModel
import com.google.gson.annotations.SerializedName

data class Pheed(
    @SerializedName("area") val area: String?,
    @SerializedName("content") val content: String?,
    @SerializedName("created_date") val createdDate: String?,
    @SerializedName("created_time") val createdTime: String?,
    @SerializedName("id") val id: Long?,
    @SerializedName("images") val images: List<Image>?,
    @SerializedName("latitude") val latitude: String?,
    @SerializedName("longitude") val longitude: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("hashtags") val hashtags: List<Hashtag>?,
    @SerializedName("user") val user: User?,
    @SerializedName("is_deleted") val isDeleted: Boolean?,
    @SerializedName("is_mine") val isMine: Boolean?,
    @SerializedName("comment_count") val commentCount: Int?
): ResponseModel()
