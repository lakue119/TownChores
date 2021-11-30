package com.lakue.lakue_library.network

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("error") val error: ErrorData?,
) {
    data class ErrorData(
        @SerializedName("error_message") val errorMessage: String?,
        @SerializedName("client_message") val clientMessage: String?,
        @SerializedName("client_message_type") val clientMessageType: String?,
        @SerializedName("status") val status: Int?
    )
}