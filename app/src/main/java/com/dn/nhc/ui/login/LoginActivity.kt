package com.dn.nhc.ui.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import com.dn.nhc.R
import com.dn.nhc.base.NeighborHoodChoresActivity
import com.dn.nhc.databinding.ActivityLoginBinding
import com.dn.nhc.remote.model.LoginResponse
import com.dn.nhc.ui.login.facebook.FacebookLogin
import com.dn.nhc.ui.login.facebook.FacebookLoginState
import com.dn.nhc.ui.login.join.JoinActivity
import com.dn.nhc.ui.login.kakao.KakaoLogin
import com.dn.nhc.ui.login.kakao.KakaoLoginState
import com.dn.nhc.ui.login.naver.NaverLogin
import com.dn.nhc.ui.login.naver.NaverLoginState
import com.dn.nhc.ui.login.naver.NaverUserInfo
import com.dn.nhc.ui.main.MainActivity
import com.dn.nhc.utils.ActivityContract
import com.dn.nhc.utils.LoadingDialog
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.kakao.sdk.user.model.User
import com.lakue.lakue_library.ext.showToast
import com.lakue.lakue_library.ext.startActivity
import com.lakue.lakue_library.network.ErrorResponse
import com.nhn.android.naverlogin.OAuthLogin
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

@AndroidEntryPoint
class LoginActivity : NeighborHoodChoresActivity<ActivityLoginBinding, LoginViewModel>(R.layout.activity_login) {
    val TAG = "LoginActivity"

    lateinit var callbackManager: CallbackManager

    private var joinLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityContract()
    ) {
        it?.let {
            val isJoinSuccess = it.getBooleanExtra("EXTRA_IS_JOIN_SUCCESS", false)
            if (isJoinSuccess) {
                startActivity<MainActivity>()
                finish()
            }
        }.run {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        callbackManager = CallbackManager.Factory.create()

        setObserver()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setObserver() {
        viewModel.apply {
            liveError.observe(this@LoginActivity) { response ->
                val data = response as ErrorResponse
                LoadingDialog.hideLoading(this@LoginActivity)
                when (data.error?.status) {
                    404 -> {
                        val intent = Intent(this@LoginActivity, JoinActivity::class.java)
                        intent.putExtra(JoinActivity.PARAMS_SOCIAL_ID, viewModel.socialId)
                        intent.putExtra(JoinActivity.PARAMS_SOCIAL_TYPE, viewModel.socialType)
                        joinLauncher.launch(intent)
//                        startNewTaskActivity<JoinActivity>(
//                            Pair(JoinActivity.PARAMS_SOCIAL_ID, viewModel.socialId),
//                            Pair(JoinActivity.PARAMS_SOCIAL_TYPE, viewModel.socialType)
//                        )
                    }
                    else -> {
                        showDialog(
                            data.error?.clientMessage ?: getString(R.string.dialog_client_error)
                        )
                    }
                }
            }
            liveSuccess.observe(this@LoginActivity) { response ->
                LoadingDialog.hideLoading(this@LoginActivity)
                response as LoginResponse?
                if(response?.success == true){
                    startActivity<MainActivity>()
                    finish()
                }
            }
        }
    }


    fun onFacebookLogin() {
        LoadingDialog.showLoading(this)
        FacebookLogin(this, callbackManager, object : FacebookLoginState {
            override fun onSuccess(result: LoginResult) {
                val request: GraphRequest =
                    GraphRequest.newMeRequest(result.accessToken) { user, response ->
                    if (response.error != null) {
                    } else {
                        Firebase.messaging.token.addOnCompleteListener(OnCompleteListener { task ->
                            if (!task.isSuccessful) {
                                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                                return@OnCompleteListener
                            }

                            // Get new FCM registration token
                            val token = if(task.result == null) "" else task.result

                            val id = user.getString("id")
                            viewModel.startLogin(id, SocialType.FACEBOOK.type, token)
                        })
                    }
                }
                val parameters = Bundle()
                parameters.putString("fields", "id,name,email,gender,birthday")
                request.parameters = parameters
                request.executeAsync()
            }

            override fun onError(error: FacebookException) {
                LoadingDialog.hideLoading(this@LoginActivity)
                if (error is FacebookAuthorizationException) {
                    showToast("기존 아이디를 로그아웃합니다.")
                    if (AccessToken.getCurrentAccessToken() != null) {
                        LoginManager.getInstance().logOut()
                    }
                }
                showToast("페이스북 로그인 진행중 문제가 발생하였습니다.")
            }

            override fun onCancel() {
                LoadingDialog.hideLoading(this@LoginActivity)
                showToast("페이스북 로그인을 취소하였습니다.")
            }
        })
    }

    fun onKakaoLogin() {
        LoadingDialog.showLoading(this)
        KakaoLogin(this, object : KakaoLoginState {
            override fun onSuccess(user: User) {
                Firebase.messaging.token.addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                        return@OnCompleteListener
                    }

                    // Get new FCM registration token
                    val token = if(task.result == null) "" else task.result

                    viewModel.startLogin(user.id.toString(), SocialType.KAKAO.type, token)
                })
            }

            override fun onError(error: Throwable) {
                LoadingDialog.hideLoading(this@LoginActivity)
                showToast("카카오톡에 로그인 후 사용해주세요.")
            }

        })
    }

    fun onNaverLogin() {
        LoadingDialog.showLoading(this)
        NaverLogin(this, object : NaverLoginState {
            override fun onSuccess(result: OAuthLogin) {
                val accessToken = result.getAccessToken(this@LoginActivity)
                CoroutineScope(Dispatchers.Main).launch {
                    // Show progress from UI thread
                    var user_info: JSONObject
                    var response: JSONObject
                    withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
                        user_info = NaverUserInfo.getUserInfo(accessToken)
                        response = user_info.getJSONObject("response")
                        val id = response.getString("id")
                        Firebase.messaging.token.addOnCompleteListener(OnCompleteListener { task ->
                            if (!task.isSuccessful) {
                                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                                return@OnCompleteListener
                            }

                            // Get new FCM registration token
                            val token = if(task.result == null) "" else task.result

                            viewModel.startLogin(id, SocialType.NAVER.type, token)
                        })



                    }
                }
            }

            override fun onError(error: OAuthLogin) {
                val errorCode = error.getLastErrorCode(this@LoginActivity).code
                val errorDesc = error.getLastErrorDesc(this@LoginActivity)
                LoadingDialog.hideLoading(this@LoginActivity)
            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            callbackManager.onActivityResult(requestCode, resultCode, data)
        } catch (e: NullPointerException) {
            showToast("다시 시도해주세요.")
        }
    }

    enum class SocialType(val type: String){
        KAKAO("K"),
        FACEBOOK("F"),
        NAVER("N")
    }

}