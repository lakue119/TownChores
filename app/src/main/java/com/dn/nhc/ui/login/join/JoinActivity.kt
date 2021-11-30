package com.dn.nhc.ui.login.join

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.dn.nhc.R
import com.dn.nhc.base.NeighborHoodChoresActivity
import com.dn.nhc.databinding.ActivityJoinBinding
import com.dn.nhc.remote.model.LoginResponse
import com.dn.nhc.remote.model.UserResponse
import com.dn.nhc.ui.main.MainActivity
import com.dn.nhc.utils.LoadingDialog
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.lakue.lakue_library.ext.showToast
import com.lakue.lakue_library.ext.startNewTaskActivity
import com.lakue.lakue_library.network.ErrorResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JoinActivity : NeighborHoodChoresActivity<ActivityJoinBinding, JoinViewModel>(R.layout.activity_join){

    lateinit var socialId: String
    lateinit var socialType: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        socialId = intent.getStringExtra(PARAMS_SOCIAL_ID) ?: ""
        socialType = intent.getStringExtra(PARAMS_SOCIAL_TYPE) ?: ""
        setOberser()
    }

    fun onComplete(){
        startNewTaskActivity<MainActivity>()
    }

    fun onJoinAuth(){
        if(socialId.isNullOrEmpty() || socialType.isNullOrEmpty()){
            showToast(getString(R.string.global_retry_plz))
            return
        }

        if(viewModel.liveNickName.value.isNullOrEmpty()){
            showToast(getString(R.string.join_using_nickname))
            return
        }
        LoadingDialog.showLoading(this@JoinActivity)

        val map = HashMap<String, Any>()
        map[PARAMS_SOCIAL_ID] = socialId
        map[PARAMS_SOCIAL_TYPE] = socialType
        map[PARAMS_NICKNAME] = viewModel.liveNickName.value!!
        viewModel.joinAuth(map)
    }

    private fun setOberser(){
        viewModel.apply {
            liveError.observe(this@JoinActivity) { response ->
                val data = response as ErrorResponse
                LoadingDialog.hideLoading(this@JoinActivity)
                showDialog(
                    data.error?.clientMessage ?: getString(R.string.dialog_client_error)
                )
            }

            liveSuccess.observe(this@JoinActivity) { response ->
                LoadingDialog.hideLoading(this@JoinActivity)
                when(response){
                    is UserResponse -> {
                        response.let {
                            if(it.success){
                                var token = ""
                                Firebase.messaging.token.addOnCompleteListener(OnCompleteListener { task ->
                                    if (!task.isSuccessful) {
                                        return@OnCompleteListener
                                    }

                                    // Get new FCM registration token
                                    token = if(task.result == null) "" else task.result

                                })

                                showDialog("회원가입이 완료되었습니다.\n로그인을 해주세요."){
                                    if(token.isNotEmpty()){
                                        viewModel.startLogin(
                                            this@JoinActivity.socialId,
                                            this@JoinActivity.socialType,
                                            token)
                                    } else {
                                        finish()
                                    }
                                }
                            }
                        }
                    }
                    is LoginResponse -> {
                        if(response.success){
                            val resultIntent = Intent()
                            resultIntent.putExtra("EXTRA_IS_JOIN_SUCCESS", true)
                            setResult(Activity.RESULT_OK, resultIntent)
                            finish()

                        }
                    }
                }
            }
        }
    }

    companion object{
        const val PARAMS_SOCIAL_ID = "social_id"
        const val PARAMS_SOCIAL_TYPE = "social_type"
        const val PARAMS_NICKNAME = "nickname"
        const val EXTRA_IS_JOIN_SUCCESS = "EXTRA_IS_JOIN_SUCCESS"
    }

}