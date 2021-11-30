package com.dn.nhc.ui.main

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.os.Bundle
import android.util.Base64
import android.util.Log
import com.dn.nhc.R
import com.dn.nhc.base.NeighborHoodChoresActivity
import com.dn.nhc.databinding.ActivityMainBinding
import com.dn.nhc.global.NowLocation
import com.dn.nhc.ui.main.home.HomeFragment
import com.dn.nhc.ui.main.mypage.MypageFragment
import com.dn.nhc.ui.main.notification.NotificationFragment
import com.dn.nhc.ui.main.search.SearchFragment
import com.dn.nhc.ui.pheed.upload.PheedUploadActivity
import com.dn.nhc.utils.camera.CameraUtil.Companion.REQUEST_CAMERA_PERMISSION
import com.lakue.lakue_library.ext.replaceFragmentInActivity
import com.lakue.lakue_library.ext.showToast
import com.lakue.lakue_library.ext.startActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


@AndroidEntryPoint
class MainActivity : NeighborHoodChoresActivity<ActivityMainBinding, MainViewModel>(R.layout.activity_main) {

    lateinit var homeFragment: HomeFragment
    lateinit var mypageFragment: MypageFragment
    lateinit var searchFragment: SearchFragment
    lateinit var notificationFragment: NotificationFragment
    private var time: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoroutineScope(Dispatchers.Main).launch {
            getLocation{ latitude, longitude, area ->
                NowLocation.latitude = latitude
                NowLocation.longitude = longitude
                NowLocation.area = area

                if(latitude == 0.0 && longitude == 0.0){
                    activityRefresh()
                    return@getLocation
                }

                initFragment()

                onNavigationItemSelected()
            }
        }
        setNavigationEmptyViewClickEnabled()
        getHashKey()
    }

    fun setNavigationEmptyViewClickEnabled(){
        binding.flNavigationBackground.setOnTouchListener { p0, p1 -> true }
    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() - time >= 2000) {
            time = System.currentTimeMillis()
            showToast(getString(R.string.back_press))
        } else if (System.currentTimeMillis() - time < 2000) {
            finish()
        }
    }

    fun initFragment() {
        homeFragment = HomeFragment.newInstance()
        mypageFragment = MypageFragment.newInstance()
        searchFragment = SearchFragment.newInstance()
        notificationFragment = NotificationFragment.newInstance()

        replaceFragmentInActivity(homeFragment, binding.fcvMain.id)
    }

    private fun getHashKey() {
        var packageInfo: PackageInfo? = null
        try {
            packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
        } catch (e: NameNotFoundException) {
            e.printStackTrace()
        }
        if (packageInfo == null) Log.e("KeyHash", "KeyHash:null")
        for (signature in packageInfo!!.signatures) {
            try {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            } catch (e: NoSuchAlgorithmException) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=$signature", e)
            }
        }
    }


    fun asyncNowFragmentToBottomNavigationView() {
        if (supportFragmentManager.fragments[0] is HomeFragment) {
            binding.bottomNavigationView.selectedItemId = R.id.menu_home
        } else if (supportFragmentManager.fragments[0] is MypageFragment) {
            binding.bottomNavigationView.selectedItemId = R.id.menu_search
        }
    }

    fun onNavigationItemSelected() {
        binding.bottomNavigationView.run {
            setOnItemSelectedListener { item ->
                var selectPosition = true
                when (item.itemId) {
                    R.id.menu_home -> {
                        replaceFragmentInActivity(homeFragment, binding.fcvMain.id)
                    }
                    R.id.menu_search -> {
                        replaceFragmentInActivity(searchFragment, binding.fcvMain.id)
                    }
                    R.id.menu_upload -> {
                        startActivity<PheedUploadActivity>()
                        selectPosition = false
                    }
                    R.id.menu_alarm -> {
                        replaceFragmentInActivity(notificationFragment, binding.fcvMain.id)
                    }
                    R.id.menu_mypage -> {
                        replaceFragmentInActivity(mypageFragment, binding.fcvMain.id)
                    }
                }
                selectPosition
            }
        }
    }

    companion object {

        fun getIntent(context: Context): Intent =
            Intent(context, MainActivity::class.java).apply {
                addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP
                            or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            or Intent.FLAG_ACTIVITY_CLEAR_TASK
                )
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            REQUEST_CAMERA_PERMISSION -> {
                if(grantResults.isNotEmpty()){
                    mypageFragment.showCameraPermission()
                }
            }
        }
    }

}