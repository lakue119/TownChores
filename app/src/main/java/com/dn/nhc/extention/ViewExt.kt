package com.dn.nhc.extention

import android.view.View
import androidx.databinding.BindingAdapter
import com.dn.nhc.R
import com.dn.nhc.utils.OnThrottleClickListener

@BindingAdapter("backgroundOnOff")
fun View.backgroundOnOff(isRead: Boolean) {
    if(isRead){
        setBackgroundResource(R.color.white_black)
    } else {
        setBackgroundResource(R.color.green_50_green_1100)
    }
}

@BindingAdapter("onThrottleClick")
fun View.onThrottleClick(action: (v: View) -> Unit) {
    val listener = View.OnClickListener { action(it) }
    setOnClickListener(OnThrottleClickListener(listener))
}