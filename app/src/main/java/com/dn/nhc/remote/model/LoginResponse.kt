package com.dn.nhc.remote.model

import com.dn.nhc.remote.model.common.User
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("response") val response: Response?,
    @SerializedName("success") val success: Boolean
): ResponseModel(){
    data class Response(
        @SerializedName("token") val token: String?,
        @SerializedName("user") val user: User?
    ): ResponseModel()
}