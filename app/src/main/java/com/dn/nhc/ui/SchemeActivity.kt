package com.dn.nhc.ui

import android.app.TaskStackBuilder
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dn.nhc.deeplink.DeepLinkInfo
import com.dn.nhc.ui.main.MainActivity
import com.lakue.lakue_library.ext.logD

class SchemeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleDeepLink()
    }

    private fun handleDeepLink() {
        val deepLinkUri = intent.data
        logD("asdf","deepLinkUri: $deepLinkUri")
        val deepLinkIntent = deepLinkUri?.let {
            DeepLinkInfo.invoke(deepLinkUri).getIntent(this, it)
        } ?: DeepLinkInfo.getMainIntent(this)

        if (isTaskRoot) {
            TaskStackBuilder.create(this).apply {
                if (needAddMainForParent(deepLinkIntent)) {
                    addNextIntentWithParentStack(DeepLinkInfo.getMainIntent(this@SchemeActivity))
                }
                addNextIntent(deepLinkIntent)
            }.startActivities()

        } else {
            startActivity(deepLinkIntent)
        }

        finish()
    }

    private fun needAddMainForParent(intent: Intent): Boolean =
        when (intent.component?.className) {
            MainActivity::class.java.name -> false
            else -> true
        }
}