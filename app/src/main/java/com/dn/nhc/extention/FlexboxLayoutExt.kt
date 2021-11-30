package com.dn.nhc.extention

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Build
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.dn.nhc.R
import com.dn.nhc.remote.model.common.Hashtag
import com.dn.nhc.utils.converter.UnitConverter
import com.google.android.flexbox.FlexboxLayout


@SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
@BindingAdapter(value = ["addItem", "onHashTagClickAction"])
fun FlexboxLayout.addItem(hashtags: List<Hashtag>?, onHashTagClickAction: Function1<String, Unit>?) {
    if(hashtags == null || hashtags.isEmpty()){
        visibility = View.GONE
        return
    }
    visibility = View.VISIBLE
    //데이터 변경될때마다 삭제 후 생성
    this.removeAllViews()

    for (hashtag in hashtags) {
        //태그 텍스트뷰 생성
        val textView = TextView(context)
        textView.text = "#${hashtag.keyword}"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textView.foreground =
                context.resources.getDrawable(R.drawable.foreground_radius_15_stroke1, null)
            textView.foregroundTintList =
                ColorStateList.valueOf(context.resources.getColor(R.color.gray_810,null))
        }

        textView.setPadding(
            UnitConverter.dpToPx(8), //left
            UnitConverter.dpToPx(4), //top
            UnitConverter.dpToPx(8), //right
            UnitConverter.dpToPx(4)  //bottom
        )

        val param = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
        param.rightMargin = UnitConverter.dpToPx(4)
        param.topMargin = UnitConverter.dpToPx(2)
        param.bottomMargin = UnitConverter.dpToPx(2)

        textView.layoutParams = param

        textView.onThrottleClick {
            onHashTagClickAction?.let { it1 -> it1(hashtag.keyword!!) }
        }

        this.addView(textView)

    }

}

@SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
@BindingAdapter(value = ["addDeleteItem", "onTagClick", "onTagDeleteClick"])
fun FlexboxLayout.addDeleteItem(keywords: ArrayList<String>?,
                                functionTagClick: Function1<String, Unit>?,
                                functionTagDeleteClick: Function1<String, Unit>?) {
    if(keywords.isNullOrEmpty()){
        visibility = View.GONE
        return
    }
    visibility = View.VISIBLE

    //데이터 변경될때마다 삭제 후 생성
    this.removeAllViews()

    for (keyword in keywords) {
        //태그 텍스트뷰 생성
        //텍스트뷰 세팅
        val textView = TextView(context).apply{
            text = "#$keyword"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                foreground =
                    context.resources.getDrawable(R.drawable.foreground_radius_15_stroke1, null)
                foregroundTintList =
                    ColorStateList.valueOf(context.resources.getColor(R.color.gray_810,null))
            }
            setPadding(
                UnitConverter.dpToPx(8), //left
                UnitConverter.dpToPx(4), //top
                UnitConverter.dpToPx(8), //right
                UnitConverter.dpToPx(4)  //bottom
            )

            if (functionTagClick == null) setOnClickListener(null)
            else setOnClickListener {
                functionTagClick(keyword)
            }
        }

        //delete이미지뷰 세팅
        val deleteImageView = ImageView(context).apply{
            Glide.with(this)
                .load(R.drawable.ic_delete)
                .into(this)

            imageTintList = ColorStateList.valueOf(resources.getColor(R.color.black_1000_white))

            setPadding(
                UnitConverter.dpToPx(2), //left
                UnitConverter.dpToPx(2), //top
                UnitConverter.dpToPx(2), //right
                UnitConverter.dpToPx(2)  //bottom
            )

            val imageParam = LinearLayout.LayoutParams(UnitConverter.dpToPx(24),
                UnitConverter.dpToPx(24))
            imageParam.leftMargin = UnitConverter.dpToPx(2)

            layoutParams = imageParam

            if (functionTagDeleteClick == null) setOnClickListener(null)
            else setOnClickListener {
                functionTagDeleteClick(keyword)
            }

        }

        //리니어 레이아웃 세팅
        val linearLayoutParam = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT).apply{
            rightMargin = UnitConverter.dpToPx(8)
            topMargin = UnitConverter.dpToPx(4)
            bottomMargin = UnitConverter.dpToPx(4)
        }
        val linearLayout = LinearLayout(context).apply{
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            layoutParams = linearLayoutParam
            addView(textView)
            addView(deleteImageView)
        }

        this.addView(linearLayout)

    }

}