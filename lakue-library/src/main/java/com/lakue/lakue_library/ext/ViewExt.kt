package com.lakue.lakue_library.ext

import android.view.View
import androidx.databinding.BindingAdapter
import java.util.concurrent.TimeUnit

@BindingAdapter(value = ["selected"])
fun View.setSelected(selected: Boolean) {
    isSelected = selected
}

@BindingAdapter(value = ["enabled"])
fun View.setEnabled(enabled: Boolean) {
    isEnabled = enabled
}

@BindingAdapter(value = ["visible"])
fun View.setVisible(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

//@BindingAdapter(value = ["onBlockClick"])
//fun View.setOnBlockClick(listener: View.OnClickListener) {
//    RxView.clicks(this)
//        .throttleFirst(1000L, TimeUnit.MILLISECONDS)
//        .subscribe {
//            listener.onClick(this)
//        }
//}

@BindingAdapter(value = ["backgroundResId"])
fun View.setBackgroundResId(resId: Int) {
    setBackgroundResource(resId)
}


/**
 * 점선은 따로 View에서 hardwareAccelerated를 false로 해야 해서 BindingAdapter로 선언
 */
@BindingAdapter(value = ["hardwareAccelerated"])
fun View.setHardwareAccelerated(enabled: Boolean) {
    setLayerType(if (enabled) View.LAYER_TYPE_HARDWARE else View.LAYER_TYPE_SOFTWARE, null)
}