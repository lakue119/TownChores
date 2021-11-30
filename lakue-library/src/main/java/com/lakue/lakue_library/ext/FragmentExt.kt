package com.lakue.lakue_library.ext

import androidx.fragment.app.Fragment
import com.lakue.lakue_library.ui.LakueActivity

fun Fragment.replaceFragmentInFragment(fragment: Fragment, frameId: Int) {
    childFragmentManager.transact {
        replace(frameId, fragment)
    }
}

fun Fragment.addFragmentInFragment(fragment: Fragment, frameId: Int) {
    childFragmentManager.transact {
        add(frameId, fragment)
    }
}

fun Fragment.showKeyboard() {
    (activity as? LakueActivity<*, *>)?.showKeyboard()
}

fun Fragment.hideKeyboard() {
    (activity as? LakueActivity<*, *>)?.hideKeyboard()
}

fun Fragment.showToast(msg: CharSequence, isLong: Boolean = false) {
    (activity as? LakueActivity<*, *>)?.showToast(msg, isLong)
}

fun Fragment.showToast(msgId: Int, isLong: Boolean = false) {
    (activity as? LakueActivity<*, *>)?.showToast(msgId, isLong)
}