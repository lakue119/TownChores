package com.dn.nhc.utils

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import com.dn.nhc.R
import com.dn.nhc.databinding.LakueDialogBinding


class LakueDialog(builder: Builder) : Dialog(builder.context){
    val Tag = LakueDialog::class.java.name

    lateinit var binding: LakueDialogBinding
    private var builder = Builder(context)

    interface OnAgreeClickListener {
        fun onAgree(dialog: LakueDialog)
    }

    interface OnDisagreeClickListener {
        fun onDisagree(dialog: LakueDialog)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val window = window
        //title remove
        window!!.requestFeature(Window.FEATURE_NO_TITLE)

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            com.dn.nhc.R.layout.lakue_dialog,
            null,
            false
        )
        setContentView(binding.root)

        // 일단 기본적으로 non-cancelable
        setCancelable(false)

        if (window != null) {
            val windowParams = window.attributes
            windowParams.dimAmount = 0.8f
            windowParams.flags = windowParams.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
            window.attributes = windowParams
            window.setBackgroundDrawableResource(R.drawable.shape_bg_dialog_common)
            window.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            window.setGravity(Gravity.CENTER)
        }
        initViews()
    }

    private fun initViews(){
        binding.apply{
            dialog = this@LakueDialog
            title = builder.title ?: ""
            content = builder.content ?: ""
            image = builder.imgRes
            agree = builder.agreeText ?: ""
            disagree = builder.disagreeText ?: ""
        }
    }

    fun onDisagreeClick(){
        if(builder.disagreeClickListener != null){
            builder.disagreeClickListener!!.onDisagree(this)
        }
    }

    fun onAgreeClick(){
        if(builder.agreeClickListener != null){
            builder.agreeClickListener!!.onAgree(this)
        }
    }

    class Builder(val context: Context) {
        var imgRes: Drawable? = null
        var title: String = ""
        var content: String = ""
        var agreeText: String = ""
        var disagreeText: String = ""
        var agreeClickListener: OnAgreeClickListener? = null
        var disagreeClickListener: OnDisagreeClickListener? = null

        fun build(): LakueDialog{
            return LakueDialog(this)
        }

        fun setImgRes(imgRes: Drawable): Builder{
            this.imgRes = imgRes
            return this
        }

        fun setTitle(title: String): Builder {
            this.title = title
            return this
        }

        fun setContent(content: String): Builder {
            this.content = content
            return this
        }

        fun setAgreeText(agreeText: String): Builder {
            this.agreeText = agreeText
            return this
        }

        fun setDisagreeText(disagreeText: String): Builder {
            this.disagreeText = disagreeText
            return this
        }

        fun setAgreeClickListener(agreeClickListener: OnAgreeClickListener): Builder {
            this.agreeClickListener = agreeClickListener
            return this
        }

        fun setDisAgreeClickListener(disagreeClickListener: OnDisagreeClickListener): Builder {
            this.disagreeClickListener = disagreeClickListener
            return this
        }
    }

    init {
        this.builder = builder
    }
}