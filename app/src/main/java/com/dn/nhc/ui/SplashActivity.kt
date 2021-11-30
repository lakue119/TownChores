package com.dn.nhc.ui

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.dn.nhc.R
import com.dn.nhc.base.NeighborHoodChoresActivity
import com.dn.nhc.databinding.ActivitySplashBinding
import com.dn.nhc.ui.intro.IntroActivity
import com.dn.nhc.ui.login.LoginActivity
import com.dn.nhc.ui.main.MainActivity
import com.dn.nhc.utils.LakueDialog
import com.lakue.lakue_library.ext.startActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity :
    NeighborHoodChoresActivity<ActivitySplashBinding, SplashViewModel>(R.layout.activity_splash) {

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->
            if (map[android.Manifest.permission.ACCESS_FINE_LOCATION]!!
                && map[android.Manifest.permission.ACCESS_COARSE_LOCATION]!!
            ) {
                viewModel.apply {
                    if (isAutoLogin()) {
                        //자동로그인 데이터 있을 시 메인화면으로
                        startActivity<MainActivity>()
                        finish()
                    } else {
                        //로그인정보 없을 시 로그인화면으로
                        startActivity<IntroActivity>()
                        finish()
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showLocationPermission{
            nextStep()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults[0] == 0) {
                nextStep()
            } else {
                LakueDialog.Builder(this@SplashActivity)
                    .setTitle(getString(R.string.location_popup_title))
                    .setContent(getString(R.string.location_popup_content))
                    .setAgreeText(getString(R.string.global_retry))
                    .setDisagreeText(getString(R.string.app_exit))
                    .setDisAgreeClickListener(object : LakueDialog.OnDisagreeClickListener {
                        override fun onDisagree(dialog: LakueDialog) {
                            dialog.dismiss()
                            android.os.Process.killProcess(android.os.Process.myPid())
                        }
                    })
                    .setAgreeClickListener(object : LakueDialog.OnAgreeClickListener {
                        override fun onAgree(dialog: LakueDialog) {
                            dialog.dismiss()
                            showLocationPermission{
                                nextStep()
                            }
                        }
                    }).build().show()
            }
        }
    }

    private fun nextStep() {
        viewModel.apply {
            if (isAutoLogin()) {
                //자동로그인 데이터 있을 시 메인화면으로
                startActivity<MainActivity>()
                finish()
            } else {
                //로그인정보 없을 시 로그인화면으로
                if(isShowIntro()){
                    startActivity<IntroActivity>()
                    finish()
                } else {
                    startActivity<LoginActivity>()
                    finish()
                }
            }
        }
    }
}