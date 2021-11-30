package com.dn.nhc.extention

import android.text.Editable
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.databinding.BindingAdapter


fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

@BindingAdapter("onEditorTextBlankTagAction")
fun EditText.onEditorTextBlankTagAction(f: Function1<String, Unit>?) {

    if (f == null) addTextChangedListener(null)
    else addTextChangedListener {
        if(it?.contains(" ")!! || it.contains("\n")){
            f(it.trim().toString())
            this.text = "".toEditable()
        }
    }
}