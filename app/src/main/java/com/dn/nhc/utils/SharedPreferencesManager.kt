package com.dn.nhc.utils

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import javax.inject.Inject

class SharedPreferencesManager @Inject constructor(application: Application){
    val Tag = SharedPreferencesManager::class.java.name

    private val sharedPreferences =
        application.getSharedPreferences("nhc-shared", Context.MODE_PRIVATE)

    var isShowIntro: Boolean
        get() = sharedPreferences.getBoolean(KEY_IS_SHOW_INTRO, true)
        set(value) {
            sharedPreferences.edit {
                putBoolean(KEY_IS_SHOW_INTRO, value)
            }
        }

    companion object {
        private const val KEY_IS_SHOW_INTRO = "KEY_IS_SHOW_INTRO"
    }

}