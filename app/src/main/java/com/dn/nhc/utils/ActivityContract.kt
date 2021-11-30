package com.dn.nhc.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class ActivityContract : ActivityResultContract<Intent, Intent>() {
    override fun createIntent(context: Context, input: Intent): Intent {
        return input
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Intent? {
        return when (resultCode) {
            Activity.RESULT_OK -> intent
            else -> null
        }
    }
}