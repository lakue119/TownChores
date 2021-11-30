package com.lakue.lakue_library.ext

import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import com.lakue.lakue_library.util.ProgressBarAnimation

@BindingAdapter(value = ["pbMax", "pbProgress"])
fun ProgressBar.setValue(pbMax: Int, pbProgress: Int) {
    max = pbMax
    startAnimation(ProgressBarAnimation(this, 0, pbProgress))
}