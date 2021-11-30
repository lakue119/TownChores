package com.lakue.lakue_library.ext

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

@BindingAdapter(value = ["loadUrl"])
fun ImageView.loadUrl(url: String?) {
    if(url == null) return

    Glide.with(this)
        .load(url)
        .thumbnail(0.1f)
        .into(this)
}

@BindingAdapter(value = ["loadUrlEmptyGone"])
fun ImageView.loadUrlEmptyGone(url: String?) {
    if(url == null) {
        this.visibility = View.GONE
        return
    }
    this.visibility = View.VISIBLE

    Glide.with(this)
        .load(url)
        .thumbnail(0.1f)
        .into(this)
}

@BindingAdapter(value = ["loadUrlCircle"])
fun ImageView.loadUrlCircle(url: String?) {
    if(url == null) return

    Glide.with(this)
        .load(url)
        .thumbnail(0.1f)
        .apply(RequestOptions.circleCropTransform())
        .into(this)
}

@BindingAdapter(value = ["loadBitmap"])
fun ImageView.loadBitmap(bitmap: Bitmap?) {
    if(bitmap == null) return

    Glide.with(this)
        .load(bitmap)
        .thumbnail(0.1f)
        .into(this)
}

@BindingAdapter(value = ["drawableResId"])
fun ImageView.setDrawableResId(resId: Int) {
    setImageResource(resId)
}


@BindingAdapter(value = ["drawable", "isVisible"])
fun ImageView.loadUrl(resId: Drawable?, isVisible: Boolean) {
    if(resId == null) return

    if(isVisible){
        this.visibility = View.VISIBLE
        Glide.with(this)
            .load(resId)
            .thumbnail(0.1f)
            .into(this)
    } else {
        this.visibility = View.GONE
    }
}