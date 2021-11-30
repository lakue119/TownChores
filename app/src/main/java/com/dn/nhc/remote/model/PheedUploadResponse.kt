package com.dn.nhc.remote.model

import com.dn.nhc.remote.model.common.Pheed
import com.google.gson.annotations.SerializedName

data class PheedUploadResponse(
    @SerializedName("response") val response: Response,
    @SerializedName("success") val success: Boolean
): ResponseModel(){
    data class Response(
        @SerializedName("post") val post: Pheed
    ): ResponseModel()
}