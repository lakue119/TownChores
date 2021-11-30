package com.dn.nhc.extention

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.dn.nhc.R
import com.dn.nhc.remote.model.common.Image

@BindingAdapter("validationNotiRead")
fun ImageView.validationNotiRead(isRead: Boolean) {
    if(isRead){
        Glide.with(this).load(R.drawable.ic_noti_off).into(this)
    } else {
        Glide.with(this).load(R.drawable.ic_noti_on).into(this)
    }
}

@BindingAdapter(value = ["loadBitmap", "loadItem"])
fun ImageView.loadBitmap(bitmap: Bitmap?, item: Image?) {
    if(bitmap == null && item == null) return

    if(bitmap != null){
        Glide.with(this)
            .load(bitmap)
            .thumbnail(0.1f)
            .into(this)
    }

    if(item != null){
        Glide.with(this)
            .load(item.imageNameUrl)
            .thumbnail(0.1f)
            .into(this)
    }
}