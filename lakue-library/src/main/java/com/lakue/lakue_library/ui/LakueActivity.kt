package com.lakue.lakue_library.ui

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelLazy
import com.lakue.lakue_library.ext.showToast
import com.lakue.lakue_library.ui.LakueViewModel
import com.lakue.lakue_library.util.Event
import java.lang.reflect.ParameterizedType
import com.lakue.lakue_library.BR

@Suppress("UNCHECKED_CAST")
abstract class LakueActivity<B : ViewDataBinding, VM : LakueViewModel>(
    @LayoutRes private val layoutResId: Int,
) : AppCompatActivity() {

    protected lateinit var binding: B

    private val viewModelClass = ((javaClass.genericSuperclass as ParameterizedType?)
        ?.actualTypeArguments
        ?.get(1) as Class<VM>).kotlin

    protected open val viewModel by ViewModelLazy(
        viewModelClass,
        { viewModelStore },
        { defaultViewModelProviderFactory }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutResId)
        binding {
            lifecycleOwner = this@LakueActivity
            setVariable(BR.vm, viewModel)
            setVariable(BR.activity, this@LakueActivity)
        }
        viewModel {
            liveToast.observe(this@LakueActivity) { this@LakueActivity.showToast(it) }
        }
    }

    protected fun binding(action: B.() -> Unit) {
        binding.run(action)
    }

    protected fun viewModel(action: VM.() -> Unit) {
        viewModel.run(action)
    }

    protected infix fun <T> LiveData<T>.observe(action: (T) -> Unit) {
        observe(this@LakueActivity, action)
    }

    protected infix fun <T> LiveData<Event<T>>.eventObserve(action: (T) -> Unit) {
        observe(this@LakueActivity, {
            it.get(action)
        })
    }

}
