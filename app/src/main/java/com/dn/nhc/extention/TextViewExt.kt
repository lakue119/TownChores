package com.dn.nhc.extention

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.dn.nhc.R

@BindingAdapter("setCommentCount")
fun TextView.setCommentCount(commentCount: Int?) {
    if(commentCount == null){
        this.text = resources.getString(R.string.edit_comment)
        return
    }
    if(commentCount > 0){
        this.text = "댓글 $commentCount"
    } else {
        this.text = resources.getString(R.string.edit_comment)
    }
}