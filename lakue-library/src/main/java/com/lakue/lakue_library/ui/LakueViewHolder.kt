package com.lakue.lakue_library.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class LakueViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    open fun onBind(item: Any, position: Int) {}
}