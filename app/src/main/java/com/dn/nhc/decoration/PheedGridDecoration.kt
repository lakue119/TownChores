package com.dn.nhc.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class PheedGridDecoration(val maxCount: Int) : RecyclerView.ItemDecoration() {
    val Tag = PheedGridDecoration::class.java.name

    private var space = 20

    fun SpacesItemDecoration(space: Int) {
        this.space = space
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {

        when(maxCount){
            2 -> {
                when(parent.getChildLayoutPosition(view)){
                    0 -> {
                        outRect.right = space/2
                    }
                    1 -> {
                        outRect.left = space/2
                    }
                }
            }
            3 -> {
                when(parent.getChildLayoutPosition(view)){
                    1 -> {
                        outRect.right = space/2
                        outRect.top = space
                    }
                    2 -> {
                        outRect.left = space/2
                        outRect.top = space
                    }
                }
            }
            else -> {
                when(parent.getChildLayoutPosition(view)){
                    0 -> {outRect.right = space/2}
                    1 -> {outRect.left = space/2}
                    2 -> {
                        outRect.right = space/2
                        outRect.top = space
                    }
                    3 -> {
                        outRect.left = space/2
                        outRect.top = space
                    }
                }
            }
//            else -> {
//                when(parent.getChildLayoutPosition(view)){
//                    0 -> {outRect.right = space/2}
//                    1 -> {outRect.left = space/2}
//                    2 -> {
//                        outRect.right = space/2
//                        outRect.top = space
//                    }
//                    3 -> {
//                        outRect.left = space/4
//                        outRect.right = space/4
//                        outRect.top = space
//                    }
//                    4 -> {
//                        outRect.left = space/2
//                        outRect.top = space
//                    }
//                }
//            }
        }
    }
}