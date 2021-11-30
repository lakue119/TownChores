package com.dn.nhc.remote.model.common

import com.dn.nhc.remote.model.ResponseModel
import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("created_date") val createdDate: String?,
    @SerializedName("id") val id: String?,
    @SerializedName("latitude") val latitude: Float?,
    @SerializedName("longitude") val longitude: Float?,
    @SerializedName("mobile_number") val mobileNumber: String?,
    @SerializedName("nickname") val nickname: String?,
    @SerializedName("profile_image_url") val profileImageUrl: String?,
    @SerializedName("role") val role: String?,
    @SerializedName("social_id") val socialId: String?,
    @SerializedName("social_type") val socialType: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("updated_at") val updatedAt: String?
): ResponseModel()
