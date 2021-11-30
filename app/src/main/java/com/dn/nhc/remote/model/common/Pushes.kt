package com.dn.nhc.remote.model.common

import com.google.gson.annotations.SerializedName

data class Pushes(
    @SerializedName("msg_division") val msgDivision: Int,
    @SerializedName("comment_id") val commentId: Int,
    @SerializedName("content") val content: String,
    @SerializedName("id") val id: Int,
    @SerializedName("is_read") var isRead: Boolean,
    @SerializedName("post_id") val postId: Int,
    @SerializedName("reference_id") val referenceId: Int,
    @SerializedName("title") val title: String,
    @SerializedName("user_id") val userId: Int
)