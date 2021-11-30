package com.dn.nhc.base

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import com.dn.nhc.R
import com.dn.nhc.ui.SplashActivity
import com.dn.nhc.utils.LakueDialog
import com.dn.nhc.utils.LoadingDialog
import com.lakue.lakue_library.ext.showToast
import com.lakue.lakue_library.network.ErrorResponse
import com.lakue.lakue_library.ui.LakueActivity
import com.lakue.lakue_library.ui.LakueFragment

open class NeighborHoodChoresFragment<B : ViewDataBinding, VM : NeighborHoodChoresViewModel>(layoutResId: Int) :
    LakueFragment<B, VM>(layoutResId) {

    var rvloading = false
    var isLastPage = false
    var isInit = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.liveNewWorkErrorDialog.observe(viewLifecycleOwner) {
            rvloading = false
            LoadingDialog.hideLoading(requireActivity() as LakueActivity<*, *>)
            if (it != null && it.isNotEmpty()) {
                showNetworkErrorDialog(errorMsg = it) {
                    activityRefresh()
                }
            }
        }
        viewModel.liveError.observe(viewLifecycleOwner) { response ->
            rvloading = false
            LoadingDialog.hideLoading(requireActivity() as LakueActivity<*, *>)
            if(response == null){
                return@observe
            }
            val data = response as ErrorResponse
            when(data.error?.clientMessageType){
                "toast" -> {
                    if (!data.error?.clientMessage.isNullOrEmpty()) {
                        showToast(data.error?.clientMessage!!)
                    }
                }
                "popup" -> {
                    if (!data.error?.clientMessage.isNullOrEmpty()) {
                        showDialog(
                            data.error?.clientMessage ?: getString(R.string.dialog_client_error)
                        )
                    }
                }
            }

            if(data.error?.status == 401){
                viewModel.dbLogout()
                activity?.finish()
                val intent = Intent(context, SplashActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
                startActivity(intent)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i("LKQJWRKLQWJRL", "reset")
        viewModel.resetValue()
    }

    protected fun showNetworkErrorDialog(errorMsg: String, action: () -> Unit) {
        LoadingDialog.hideLoading(activity as LakueActivity<*, *>)
        val dialog = LakueDialog.Builder(requireContext())
            .setTitle(getString(R.string.global_error))
            .setContent(errorMsg)
            .setAgreeText(getString(R.string.global_retry))
            .setDisagreeText(getString(R.string.global_close))
            .setDisAgreeClickListener(object : LakueDialog.OnDisagreeClickListener {
                override fun onDisagree(dialog: LakueDialog) {
                    dialog.dismiss()
                }
            })
            .setAgreeClickListener(object : LakueDialog.OnAgreeClickListener {
                override fun onAgree(dialog: LakueDialog) {
                    action()
                }
            }).build()
        dialog.show()
    }


    protected fun activityRefresh() {
        (activity as LakueActivity<*, *>).apply{
            finish()
            overridePendingTransition(0, 0)
            startActivity(intent)
            overridePendingTransition(0, 0);
        }
    }


    fun showDialog(msg: String, action: (() -> Unit?)? = null) {
        val dialog = LakueDialog.Builder(requireContext())
            .setTitle(msg)
            .setAgreeText(getString(R.string.global_ok))
            .setAgreeClickListener(object : LakueDialog.OnAgreeClickListener {
                override fun onAgree(dialog: LakueDialog) {
                    if (action != null) {
                        dialog.dismiss()
                        action.invoke()
                    } else {
                        dialog.dismiss()
                    }
                }
            }).build()
        dialog.show()
    }
}