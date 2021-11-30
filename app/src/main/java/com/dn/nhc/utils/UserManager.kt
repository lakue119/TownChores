package com.dn.nhc.utils

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import javax.inject.Inject

class UserManager @Inject constructor(application: Application){
    val Tag = UserManager::class.java.name

    private val sharedPreferences =
        application.getSharedPreferences("session", Context.MODE_PRIVATE)

//    val isLogin: Boolean = userToken.isNotEmpty()

    fun getLoginCheck(): Boolean{
        return userToken.isNotEmpty()
    }

    var cookie: String
        get() = sharedPreferences.getString(KEY_COOKIE_SESSION, "") ?: ""
        set(value) {
            sharedPreferences.edit {
                putString(KEY_COOKIE_SESSION, value)
            }
        }

    var userId: String
        get() = sharedPreferences.getString(KEY_USER_ID, "") ?: ""
        set(value) {
            sharedPreferences.edit {
                putString(KEY_USER_ID, value)
            }
        }
    var userToken: String
        get() = sharedPreferences.getString(KEY_USER_TOKEN, "") ?: ""
        set(value) {
            sharedPreferences.edit {
                putString(KEY_USER_TOKEN, value)
            }
        }
    var userType: String
        get() = sharedPreferences.getString(KEY_USER_ROLE, "") ?: ""
        set(value) {
            sharedPreferences.edit {
                putString(KEY_USER_ROLE, value)
            }
        }
    var userNickname: String
        get() = sharedPreferences.getString(KEY_NICKNAME, "") ?: ""
        set(value) {
            sharedPreferences.edit {
                putString(KEY_NICKNAME, value)
            }
        }
    var userSocialId: String
        get() = sharedPreferences.getString(KEY_SOCIAL_ID, "") ?: ""
        set(value) {
            sharedPreferences.edit {
                putString(KEY_SOCIAL_ID, value)
            }
        }
    var userSocialType: String
        get() = sharedPreferences.getString(KEY_SOCIAL_TYPE, "") ?: ""
        set(value) {
            sharedPreferences.edit {
                putString(KEY_SOCIAL_TYPE, value)
            }
        }
    var userLatitude: Float
        get() = sharedPreferences.getFloat(KEY_LATITUDE, 0f) ?: 0f
        set(value) {
            sharedPreferences.edit {
                putFloat(KEY_LATITUDE, value)
            }
        }
    var userLongitude: Float
        get() = sharedPreferences.getFloat(KEY_LONGITUDE, 0f) ?: 0f
        set(value) {
            sharedPreferences.edit {
                putFloat(KEY_LONGITUDE, value)
            }
        }
    var userProfileImage: String
        get() = sharedPreferences.getString(KEY_PROFILE_IMAGE, "") ?: ""
        set(value) {
            sharedPreferences.edit {
                putString(KEY_PROFILE_IMAGE, value)
            }
        }

    fun logout(){
        cookie = ""
        userId = ""
        userToken = ""
        userType = ""
        userNickname = ""
        userSocialId = ""
        userSocialType = ""
        userLatitude = 0.0f
        userLongitude = 0.0f
        userProfileImage = ""

    }

    companion object {
        private const val KEY_USER_ID = "KEY_USER_ID"
        private const val KEY_USER_ROLE = "KEY_USER_ROLE"
        private const val KEY_USER_TOKEN = "KEY_USER_TOKEN"
        private const val KEY_NICKNAME = "KEY_NICKNAME"
        private const val KEY_SOCIAL_ID = "KEY_SOCIAL_ID"
        private const val KEY_SOCIAL_TYPE = "KEY_SOCIAL_TYPE"
        private const val KEY_LATITUDE = "KEY_LATITUDE"
        private const val KEY_LONGITUDE = "KEY_LONGITUDE"
        private const val KEY_PROFILE_IMAGE = "KEY_PROFILE_IMAGE"
        private const val KEY_COOKIE_SESSION = "KEY_COOKIE_SESSION"
    }

}