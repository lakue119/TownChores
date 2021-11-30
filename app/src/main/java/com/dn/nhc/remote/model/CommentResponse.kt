package com.dn.nhc.remote.model

import com.dn.nhc.remote.model.common.Comment
import com.google.gson.annotations.SerializedName

data class CommentResponse(
    @SerializedName("response") val response: Response,
    @SerializedName("success") val success: Boolean
): ResponseModel() {
    data class Response(
        @SerializedName("comment") val comment: Comment?
    ): ResponseModel()
}