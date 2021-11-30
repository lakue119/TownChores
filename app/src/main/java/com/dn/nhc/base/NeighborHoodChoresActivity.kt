package com.dn.nhc.base

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import com.dn.nhc.R
import com.dn.nhc.ui.SplashActivity
import com.dn.nhc.ui.search.SearchActivity
import com.dn.nhc.utils.LakueDialog
import com.dn.nhc.utils.LoadingDialog
import com.dn.nhc.utils.location.ConvertAddress
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.lakue.lakue_library.ext.showToast
import com.lakue.lakue_library.ext.startActivity
import com.lakue.lakue_library.network.ErrorResponse
import com.lakue.lakue_library.ui.LakueActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

open class NeighborHoodChoresActivity<B : ViewDataBinding, VM : NeighborHoodChoresViewModel>(
    @LayoutRes private val layoutResId: Int,
) : LakueActivity<B, VM>(layoutResId) {

    lateinit var mLocMan: LocationManager // 위치 관리자
    val REQUEST_LOCATION_PERMISSION = 1001

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    val onHashTagClickAction: Function1<String, Unit> = this::onHashTagClickAction

    var isEditTextTouchHide = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSavedInstanceState(savedInstanceState)

        viewModel.liveNewWorkErrorDialog.observe(this) {
            if (it.isNotEmpty()) {
                showNetworkErrorDialog(errorMsg = it) {
                    activityRefresh()
                }
            }
        }
        viewModel.liveError.observe(this@NeighborHoodChoresActivity) { response ->
            LoadingDialog.hideLoading(this)
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
                finish()
                val intent = Intent(this, SplashActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
                startActivity(intent)
            }

        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

    }

    @SuppressLint("MissingPermission")
    suspend fun getLocation(action: (latitude: Double, longitude: Double, area: String) -> Unit) = GlobalScope.async {
        // Got last known location. In some rare situations this can be null.
        withContext(Dispatchers.Default) {
            showLocationPermission {
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location ->
                        // Got last known location. In some rare situations this can be null.
                        val latitude = location.latitude
                        val longitude = location.longitude
                        val area =
                            ConvertAddress.getCurrentAddress(latitude, longitude)

                        action(latitude, longitude, area)
                    }
            }
        }
    }.await()

    override fun onStart() {
        super.onStart()
        //현재 앱 GPS켜진 상태인지 확인 / 권한과는 별개
        mLocMan = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!mLocMan.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
        }
    }

    protected fun activityRefresh() {
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }


    fun showDialog(msg: String, action: (() -> Unit?)? = null) {
        val dialog = LakueDialog.Builder(this@NeighborHoodChoresActivity)
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


    protected fun showNetworkErrorDialog(errorMsg: String, action: () -> Unit) {
        LoadingDialog.hideLoading(this@NeighborHoodChoresActivity)
        val dialog = LakueDialog.Builder(this@NeighborHoodChoresActivity)
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

    protected fun buildAlertMessageNoGps() {
        val dialog = LakueDialog.Builder(this@NeighborHoodChoresActivity)
            .setTitle(getString(R.string.location_service_dialog_title))
            .setContent(getString(R.string.location_service_dialog_content))
            .setAgreeText(getString(R.string.location_service_dialog_ok))
            .setDisagreeText(getString(R.string.location_service_dialog_no))
            .setDisAgreeClickListener(object : LakueDialog.OnDisagreeClickListener {
                override fun onDisagree(dialog: LakueDialog) {
                    dialog.dismiss()
                    android.os.Process.killProcess(android.os.Process.myPid())
                }
            })
            .setAgreeClickListener(object : LakueDialog.OnAgreeClickListener {
                override fun onAgree(dialog: LakueDialog) {
                    dialog.dismiss()
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
            }).build()
        dialog.show()

    }

    //위치권한 체크
    protected fun showLocationPermission(
        successAction: () -> Unit
    ) {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                checkLocationPermissionAPI28(REQUEST_LOCATION_PERMISSION, successAction)
            }
            Build.VERSION.SDK_INT == Build.VERSION_CODES.R -> {
                checkLocationPermissionAPI29(REQUEST_LOCATION_PERMISSION, successAction)
            }
            else -> {
                checkBackgroundLocationPermissionAPI30(REQUEST_LOCATION_PERMISSION, successAction)
            }
        }
    }

    @TargetApi(28)
    fun Context.checkLocationPermissionAPI28(
        locationRequestCode: Int,
        successAction: () -> Unit
    ) {
        if (!checkSinglePermission(Manifest.permission.ACCESS_FINE_LOCATION) ||
            !checkSinglePermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            val permList = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            requestPermissions(permList, locationRequestCode)
        } else {
            successAction()
        }
    }

    @TargetApi(29)
    private fun Context.checkLocationPermissionAPI29(
        locationRequestCode: Int,
        successAction: () -> Unit
    ) {
        if (checkSinglePermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkSinglePermission(Manifest.permission.ACCESS_COARSE_LOCATION) &&
            checkSinglePermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        ) {
            successAction()
            return
        }
        val permList = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )
        requestPermissions(permList, locationRequestCode)

    }

    @TargetApi(30)
    private fun Context.checkBackgroundLocationPermissionAPI30(
        backgroundLocationRequestCode: Int,
        successAction: () -> Unit
    ) {
        if (checkSinglePermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
            successAction()
            return
        }
        AlertDialog.Builder(this)
            .setTitle(R.string.location_service_dialog_title)
            .setMessage(R.string.location_service_dialog_content)
            .setPositiveButton(R.string.location_service_dialog_ok) { _, _ ->
                // this request will take user to Application's Setting page
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                    backgroundLocationRequestCode
                )
            }
            .setNegativeButton(R.string.location_service_dialog_no) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()

    }

    private fun Context.checkSinglePermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    protected fun getBundle(savedInstanceState: Bundle?): Bundle? =
        savedInstanceState ?: intent.extras


    protected open fun setSavedInstanceState(savedInstanceState: Bundle?) {
        // no-op
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if(isEditTextTouchHide){
            val focusView = currentFocus
            if (focusView != null) {
                val rect = Rect()
                focusView.getGlobalVisibleRect(rect)
                val x = ev.x.toInt()
                val y = ev.y.toInt()
                if (!rect.contains(x, y)) {
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(focusView.windowToken, 0)
                    focusView.clearFocus()
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun onHashTagClickAction(keyword: String){
        startActivity<SearchActivity>(
            Pair(
                SearchActivity.EXTRA_SEARCH_KEYWORD, keyword
            )
        )
    }
}