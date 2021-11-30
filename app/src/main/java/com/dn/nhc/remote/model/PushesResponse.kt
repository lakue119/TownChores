package com.dn.nhc.remote.model

import com.dn.nhc.remote.model.common.Pushes
import com.google.gson.annotations.SerializedName

data class PushesResponse(
    @SerializedName("response") val response: Response?,
    @SerializedName("success") val success: Boolean
): ResponseModel() {
    data class Response(
        @SerializedName("pushes") val pushes: List<Pushes>?
    ): ResponseModel()
}