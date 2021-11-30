package com.dn.nhc.remote.model

import com.dn.nhc.remote.model.common.Pheed
import com.google.gson.annotations.SerializedName

data class PheedResponse(
    @SerializedName("response") val response: Response,
    @SerializedName("success") val success: Boolean
): ResponseModel(){
    data class Response(
        @SerializedName("posts") val posts: List<Pheed>
    ): ResponseModel()
}