package com.dn.nhc.remote.model

import com.google.gson.annotations.SerializedName

data class CommonResponse(
    @SerializedName("response") val response: Boolean,
    @SerializedName("success") val success: Boolean
): ResponseModel()