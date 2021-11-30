package com.dn.nhc.remote.model.common

import com.dn.nhc.remote.model.ResponseModel
import com.google.gson.annotations.SerializedName

data class Hashtag(
    @SerializedName("id") val id: Long? = 0,
    @SerializedName("post_id") val postId: Long? = 0,
    @SerializedName("seq") val seq: Int? = 0,
    @SerializedName("keyword") val keyword: String? = ""
): ResponseModel()
