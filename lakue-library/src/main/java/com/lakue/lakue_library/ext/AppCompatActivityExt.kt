package com.lakue.lakue_library.ext

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment

fun AppCompatActivity.isAvailableActivity(block: () -> Unit) {
    if(!isDestroyed && !isFinishing) {
        block()
    }
}

inline fun <reified T> AppCompatActivity.startActivity(
    vararg pairs: Pair<String, Any?>,
) {
    startActivity(Intent(this, T::class.java).apply {
        putExtras(bundleOf(*pairs))
    })
}

inline fun <reified T> AppCompatActivity.startNewTaskActivity(
    vararg pairs: Pair<String, Any?>,
) {
    startActivity(Intent(this, T::class.java).apply {
        putExtras(bundleOf(*pairs))
        addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
    })
}

fun AppCompatActivity.replaceFragmentInActivity(fragment: Fragment, frameId: Int) {
    val tag = fragment.javaClass.simpleName
    supportFragmentManager.transact {
        replace(frameId, fragment)
        addToBackStack(tag)
    }
}

fun AppCompatActivity.addFragmentInActivity(fragment: Fragment, frameId: Int) {
    supportFragmentManager.transact {
        add(frameId, fragment)
    }
}

fun AppCompatActivity.showToast(msg: CharSequence, isLong: Boolean = false) {
    Toast.makeText(
        applicationContext,
        msg,
        if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
    )
        .show()
}

fun AppCompatActivity.showToast(msgId: Int, isLong: Boolean = false) {
    showToast(getString(msgId), isLong)
}
