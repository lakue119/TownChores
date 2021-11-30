package com.dn.nhc.ui.login.auth.phone

import android.os.Bundle
import android.view.View
import com.dn.nhc.R
import com.dn.nhc.base.NeighborHoodChoresActivity
import com.dn.nhc.databinding.ActivityAuthPhoneBinding
import com.dn.nhc.ui.login.join.JoinActivity
import com.lakue.lakue_library.ext.startActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthPhoneActivity : NeighborHoodChoresActivity<ActivityAuthPhoneBinding, AuthPhoneViewModel>(R.layout.activity_auth_phone) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun onSendAuth(){
        //TODO 문자 보내기 성공
        binding.apply{
            btnSendAuth.text = getString(R.string.sns_retry)
            etAuthNumber.visibility = View.VISIBLE
            btnCheckAuth.visibility = View.VISIBLE
        }
    }

    fun onCheckAuth(){
        //회원가입
        startActivity<JoinActivity>()
        //로그인 후 메인화면 이동
//        startActivity<MainActivity>()
    }

}