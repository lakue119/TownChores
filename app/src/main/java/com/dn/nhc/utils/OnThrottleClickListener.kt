package com.dn.nhc.utils

import android.view.View
import com.lakue.lakue_library.ext.logD


/**
 * 중복클릭 방지 리스너
 */
class OnThrottleClickListener(
    private val clickListener: View.OnClickListener,
    private val interval: Long = 300
) :
    View.OnClickListener {

    val TAG = "OnThrottleClickListener"

    private var clickable = true
    // clickable 플래그를 이 클래스가 아니라 더 상위 클래스에 두면
    // 여러 뷰에 대한 중복 클릭 방지할 수 있다.

    override fun onClick(v: View?) {
        logD(TAG, "OnThrottleClickListener")
        if (clickable) {
            clickable = false
            v?.run {
                postDelayed({
                    clickable = true
                }, interval)
                clickListener.onClick(v)
            }
        } else {
            logD(TAG, "onClick: waiting for a while")
        }
    }
}